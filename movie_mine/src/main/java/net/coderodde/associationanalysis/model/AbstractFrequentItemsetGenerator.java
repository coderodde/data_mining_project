package net.coderodde.associationanalysis.model;

import java.util.ArrayList;
import java.util.Collections;
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
        AbstractFrequentItemsetGenerator<I extends Comparable<? super I>> {
    
    /**
     * The algorithm for generation of frequent itemsets.
     * 
     * @param  database the actual database.
     * @param  minimumSupport the minimum support for mining task.
     * @return the object describing the frequent itemsets and support count
     *         function.
     */
    public abstract FrequentItemsetData<I>
        findFrequentItemsets(final AbstractDatabase<?, I> database,
                             final double minimumSupport);
        
    /**
     * Generates k+1 -itemset candidate from k-itemsets.
     * 
     * @param  itemsetList the list of itemsets.
     * @return the list of next itemsets.
     */
    protected List<Set<I>> generateCandidates(final List<Set<I>> itemsetList) {
        final List<List<I>> list = new ArrayList<>(itemsetList.size());
        
        for (final Set<I> itemset : itemsetList) {
            final List<I> l = new ArrayList<>(itemset);
            Collections.<I>sort(l);
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
     * @param database             the transaction database.
     * @return the list of frequent itemsets.
     */
    protected List<Set<I>> 
        getNextItemsets(
                final List<Set<I>> candidateList,
                final AbstractSupportCountFunction<I> supportCountFunction,
                final double minimumSupport,
                final AbstractDatabase<?, I> database) {
        final List<Set<I>> ret = new ArrayList<>(candidateList.size());
        
        for (final Set<I> itemset : candidateList) {
            final int supportCount = 
                    supportCountFunction.getSupportCount(itemset);
            
            final double support = 1.0 * supportCount / database.size();
            
            if (support >= minimumSupport) {
                ret.add(itemset);
            }
        }
        
        return ret;
    }
        
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
    
    protected List<Set<I>> 
        extractFrequentItemsets(final Map<Integer, List<Set<I>>> map) {
        final List<Set<I>> ret = new ArrayList<>();
        
        for (final List<Set<I>> itemsetList : map.values()) {
            ret.addAll(itemsetList);
        }
        
        return ret;
    }
}
