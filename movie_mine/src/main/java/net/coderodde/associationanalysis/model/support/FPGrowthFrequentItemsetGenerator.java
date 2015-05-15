package net.coderodde.associationanalysis.model.support;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.coderodde.associationanalysis.model.AbstractFrequentItemsetGenerator;
import net.coderodde.associationanalysis.model.FrequentItemsetData;

/**
 * This class implements the FP-Growth algorithm for frequent pattern discovery.
 * 
 * @author Rodion Efremov
 * @version 1.6
 * @param <I> the actual type of items.
 */
public class FPGrowthFrequentItemsetGenerator<I extends Comparable<? super I>> 
extends AbstractFrequentItemsetGenerator<I> {

    public FPGrowthFrequentItemsetGenerator() {
        super(null);
    }
    
    @Override
    public FrequentItemsetData<I> 
        findFrequentItemsets(List<Set<I>> transactionList,
                             double minimumSupport) {
        // FP-growth uses support counts instead of supports.
        final int minimumSupportCount = 
                (int) Math.ceil(transactionList.size() * minimumSupport);
        
        // The initial FP-tree.
        final FPTree<I> tree = new FPTree<>(transactionList.size(), 
                                            minimumSupportCount);
        // Find infrequent items (not itemsets!).
        final ItemCategories<I> categories = 
                splitItems(transactionList,
                                    minimumSupport);
        final Set<I> infrequentSet = categories.infrequentItems;
        
        // Build the FP-tree.
        for (final Set<I> itemset : transactionList) {
            if (!Collections.disjoint(infrequentSet, itemset)) {
                // 'itemset' contains an infrequent item. Cannot be frequent.
                continue;
            }
            
            // The second argument is ignored by FPTree.
            tree.putSupportCount(itemset, Integer.MIN_VALUE);
        }
        
        // A collection of frequent patterns beint populated.
        final List<Set<I>> frequentItemsetList = new ArrayList<>();
        
        // The actual computation begins here.
        fpGrowth(tree, new HashSet<I>(), frequentItemsetList);
        
        // Return the results of the computation.
        return new FrequentItemsetData<>(frequentItemsetList, 
                                         tree, 
                                         transactionList.size());
    }
    
    /**
     * Holds a set of frequent items and a set of infrequent items.
     * 
     * @param <T> the actual item type.
     */
    private static class ItemCategories<T> {
        final Set<T> frequentItems;
        final Set<T> infrequentItems;
        
        ItemCategories(Set<T> frequentItems, Set<T> infrequentItems) {
            this.frequentItems = frequentItems;
            this.infrequentItems = infrequentItems;
        }
    }
        
    /**
     * Splits the items contained in <code>transactionList</code> into two sets:
     * one for frequent items and another for infrequent items.
     * 
     * @param <I>             the actual item type.
     * @param transactionList the list of transactions to mine.
     * @param minimumSupport  the minimum support.
     * @return two sets.
     */
    private static <I> ItemCategories<I> 
    splitItems(List<Set<I>> transactionList, 
               double minimumSupport) {
        final Map<I, Integer> map = new HashMap<>();
        
        for (final Set<I> transaction : transactionList) {
            for (final I item : transaction) {
                if (!map.containsKey(item)) {
                    map.put(item, 1);
                } else {
                    map.put(item, map.get(item) + 1);
                }
            }
        }
        
        final Set<I> frequentSet = new HashSet<>();
        final Set<I> infrequentSet = new HashSet<>();
        
        for (final I item : map.keySet()) {
            final double support = 1.0 * map.get(item) / transactionList.size();
            (support < minimumSupport ? infrequentSet : frequentSet).add(item);
        }
        
        return new ItemCategories<>(frequentSet, infrequentSet);
    }
        
    /**
     * The actual implementation routine of the FP-growth algorithm.
     * 
     * @param <I>   the actual item type.
     * @param tree  the tree to process.
     * @param alpha the itemset to append to.
     * @param list  the list for accumulating the frequent itemsets.
     */
    private static <I extends Comparable<? super I>> 
    void fpGrowth(FPTree<I> tree, Set<I> alpha, List<Set<I>> list) {
        final int minimumCount = tree.getPathSupportCount();
        
        if (minimumCount > 0) {
            list.addAll(extractItemsetsFromPath(alpha, tree));
            return;
        }
        
        // Header items in descending order by counts.
        final Object[] itemArray = tree.getHeaderItems();
        
        // Recur.
        for (final Object itemObject : itemArray) {
            final I item = (I) itemObject;
            final Set<I> beta = new HashSet<>(alpha.size() + 1);
            
            beta.addAll(alpha);
            beta.add(item);
            
            final FPTree<I> nextTree = tree.getConditionalFPTree(item);
            
            if (!nextTree.isEmpty()) {
                fpGrowth(nextTree, betas, list);
            }
        }
    }
     
    /**
     * Generates all the itemsets containing <code>alpha</code> and one
     * combination of items in <code>path</code>. It is not check, but 
     * <code>path</code> should actually consist of a single branch.
     * 
     * @param <I>   the actual item type.
     * @param alpha the itemset to append to.
     * @param path  the path form which to generate the combinations.
     * @return      a list of itemsets.
     */
    private static <I extends Comparable<? super I>> 
        List<Set<I>> extractItemsetsFromPath(Set<I> alpha,
                                             FPTree<I> path) {
        final List<I> list = path.toItemList();
        final BitSet bs = new BitSet(list.size());
        final List<Set<I>> ret = new ArrayList<>();
        final long combinationAmount = pow2(list.size());
        
        for (long l = 0; l < combinationAmount; ++l) {
            final Set<I> combination = extractCombination(list, bs);
            final Set<I> tmp = new HashSet<>(combination.size() + alpha.size());
            tmp.addAll(alpha);
            tmp.addAll(combination);    
            ret.add(tmp);
            bitsetIncrement(bs);
        }
        
        return ret;
    }
        
    /**
     * Extracts an item combination  (itemset) from <code>itemList</code>. If 
     * for any <code>i</code> <code>bs.get(i)</code> is true, 
     * <code>itemList.get(i)</code> will be included in the combination 
     * (itemset).
     * 
     * @param <I>      the actual item type.
     * @param itemList the list of items.
     * @param bs       the bit set.
     * @return         a combination.
     */
    private static <I extends Comparable<? super I>> 
    Set<I> extractCombination(List<I> itemList, BitSet bs) {
        final Set<I> ret = new HashSet<>(bs.size());
        
        for (int i = 0; i < bs.size(); ++i) {
            if (bs.get(i)) {
                ret.add(itemList.get(i));
            }
        }
        
        return ret;
    }
        
    /**
     * Increments the "binary number" represented by the input bit set.
     * 
     * @param bs the target bit set.
     */
    private static void bitsetIncrement(BitSet bs) {
        for (int i = 0; i < bs.size(); ++i) {
            if (!bs.get(i)) {
                bs.set(i);
                return;
            } else {
                bs.clear(i);
            }
        }
    }
    
    /**
     * Returns 2 to the power of <code>exponent</code>.
     * 
     * @param  exponent the exponent.
     * @return a <code>long</code> value.
     */
    private static long pow2(int exponent) {
        long ret = 1L;
        
        for (int i = 0; i < exponent; ++i) {
            ret *= 2L;
        }
        
        return ret;
    }
}
