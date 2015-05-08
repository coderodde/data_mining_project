package net.coderodde.associationanalysis.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * This abstract class defines the API for frequent itemset generation 
 * algorithms.
 * 
 * @author Rodion Efremov
 * @version 1.6
 * @param <I> the actual item type.
 */
public abstract class 
        AbstractFrequentItemsetGenerator<I> {
    
    /**
     * The item comparator. Required in the routine generating next itemsets.
     */
    protected final Comparator<I> itemComparator;
    
    /**
     * Construct this frequent itemset generator with given comparator.
     * 
     * @param itemComparator the item comparator.
     */
    public AbstractFrequentItemsetGenerator
        (final Comparator<I> itemComparator) {
        this.itemComparator = itemComparator;
    }
    
    /**
     * The algorithm for generation of frequent itemsets.
     * 
     * @param  transactionList the list of transactions to mine.
     * @param  minimumSupport  the minimum support for mining task.
     * @return the object describing the frequent itemsets and support count
     *         function.
     */
    public abstract FrequentItemsetData<I>
        findFrequentItemsets(final List<Set<I>> transactionList,
                             final double minimumSupport);
        
    /**
     * Generates k+1 -itemset candidate from k-itemsets. This is so called
     * <tt>F_{k - 1} x F_{k - 1}</tt> routine.
     * 
     * @param  itemsetList the list of itemsets.
     * @return the list of next itemsets.
     */
    protected List<Set<I>> generateCandidates(final List<Set<I>> itemsetList) {
        final List<List<I>> list = new ArrayList<>(itemsetList.size());
        
        for (final Set<I> itemset : itemsetList) {
            final List<I> l = new ArrayList<>(itemset);
            Collections.<I>sort(l, itemComparator);
            list.add(l);
        }
        
        final int N = list.size();
        final List<Set<I>> ret = new ArrayList<>(N);
        
        for (int i = 0; i < N; ++i) {
            for (int j = i + 1; j < N; ++j) {
                final Set<I> candidate =
                        tryMergeItemsets(list.get(i), list.get(j));
                
                if (candidate != null) {
                    ret.add(candidate);
                }
            }
        }
        
        return ret;
    }
    
    /**
     * Tries to merge two <tt>k</tt>-itemsets into one <tt>k + 1</tt>-itemset.
     * It does this only if <tt>N - 1</tt> first elements in both itemsets are
     * same.
     * 
     * @param  itemset1 the first itemset.
     * @param  itemset2 the second itemset.
     * @return <code>null</code> if the two input itemsets cannot be merged into
     *         one. Otherwise the merged itemset is returned.
     */
    protected Set<I> tryMergeItemsets(final List<I> itemset1, 
                                      final List<I> itemset2) {
        final int length = itemset1.size();
        
        for (int i = 0; i < length - 1; ++i) {
            if (!itemset1.get(i).equals(itemset2.get(i))) {
                return null;
            }
        }
        
        if (itemset1.get(length - 1).equals(itemset2.get(length - 1))) {
            throw new IllegalStateException("Please, debug me.");
        }
        
        final Set<I> itemset = new HashSet<>(itemset1.size());
        
        for (int i = 0; i < length - 1; ++i) {
            itemset.add(itemset1.get(i));
        }
        
        itemset.add(itemset1.get(length - 1));
        itemset.add(itemset2.get(length - 1));
        return itemset;
    }
    
    /**
     * Returns the list of those itemsets whose support is no less than
     * <code>minimumSupport</code>.
     * 
     * @param candidateList        the list of candidate itemsets.
     * @param supportCountFunction the support count function.
     * @param minimumSupport       the minimum support.
     * @param transactionList      the list of target transactions.
     * @return the list of frequent itemsets.
     */
    protected List<Set<I>> 
        getNextItemsets(
                final List<Set<I>> candidateList,
                final AbstractSupportCountFunction<I> supportCountFunction,
                final double minimumSupport,
                final List<Set<I>> transactionList) {
        final List<Set<I>> ret = new ArrayList<>(candidateList.size());
        
        for (final Set<I> itemset : candidateList) {
            final int supportCount = 
                    supportCountFunction.getSupportCount(itemset);
            
            final double support = 1.0 * supportCount / transactionList.size();
            
            if (support >= minimumSupport) {
                ret.add(itemset);
            }
        }
        
        return ret;
    }
        
    /**
     * Returns the list of those itemsets, that are completely contained in 
     * <code>transaction</code>.
     * 
     * @param  candidateList the list of candidate itemsets.
     * @param  transaction   the transaction.
     * @return a list of itemsets.
     */
    protected List<Set<I>> subset(final List<Set<I>> candidateList,
                                  final Set<I> transaction) {
        final List<Set<I>> ret = new ArrayList<>(candidateList.size());
        
        for (final Set<I> candidate : candidateList) {
            if (transaction.containsAll(candidate)) {
                ret.add(candidate);
            }
        }
        
        return ret;
    }
    
    /**
     * Extracts all the frequent itemsets.
     * 
     * @param  map the map mapping the size of itemset to a list of all itemsets
     *             with same size.
     * @return the list of frequent itemsets in no particular order.
     */
    protected List<Set<I>> 
        extractFrequentItemsets(final Map<Integer, List<Set<I>>> map) {
        final List<Set<I>> ret = new ArrayList<>();
        
        for (final List<Set<I>> itemsetList : map.values()) {
            ret.addAll(itemsetList);
        }
        
        return ret;
    }
}
