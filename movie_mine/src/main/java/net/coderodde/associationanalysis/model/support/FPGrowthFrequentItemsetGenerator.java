package net.coderodde.associationanalysis.model.support;

import java.util.ArrayList;
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
        fpGrowth(tree, frequentItemsetList);
        
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
        
    private static <I extends Comparable<? super I>> 
    void fpGrowth(FPTree<I> tree, List<Set<I>> alpha) {
        final int minimumCount = tree.getPathSupportCount();
        
        if (minimumCount > 0) {
            alpha.addAll(extractItemsets(tree));
            return;
        }
        
        final Object[] itemArray = tree.getHeaderItems();
        
        for (final Object itemObject : itemArray) {
            final I item = (I) itemObject;
            
        }
    }
     
}
