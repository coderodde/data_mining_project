package net.coderodde.associationanalysis.model.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
        findFrequentItemsets(final List<Set<I>> transactionList,
                             final double minimumSupport) {
        final FPTree<I> tree = new FPTree<>(transactionList.size());
        final Set<I> infrequentItems = findInfrequentItems(transactionList,
                                                           minimumSupport);
        
        for (final Set<I> itemset : transactionList) {
            if (!Collections.disjoint(infrequentItems, itemset)) {
                // 'itemset' contains an infrequent item. Cannot be frequent.
                continue;
            }
            
            // The second argument is ignored by FPTree.
            tree.putSupportCount(itemset, Integer.MIN_VALUE);
        }
        
        
        
        return null;
    }
    
    private Set<I> findInfrequentItems(List<Set<I>> transactionList,
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
        
        final Set<I> ret = new HashSet<>();
        
        for (final I item : map.keySet()) {
            final double support = 1.0 * map.get(item) / transactionList.size();
            
            if (support < minimumSupport) {
                ret.add(item);
            }
        }
        
        return ret;
    }
        
    /**
     * Returns the list of frequent <b>items</b> (<b>not</b> itemsets). The 
     * routine prunes all infrequent items as all itemsets containing at least 
     * one of them cannot be frequent by Apriori principle.
     * 
     * @param  transactionList the list of transactions (itemsets).
     * @param  minimumSupport  the minimum support.
     * @return the list of frequent items.
     */
    private List<I> findFrequentItems(final List<Set<I>> transactionList,
                                      final double minimumSupport) {
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
        
        final List<I> ret = new ArrayList<>();
        final Map<I, Double> map2 = new HashMap<>();
        
        for (final I item : map.keySet()) {
            final double support = 1.0 * map.get(item) / transactionList.size();
            
            if (support >= minimumSupport) {
                ret.add(item);
                map2.put(item, support);
            }
        }
        
        // Sort the list of frequent *items*. The higher support items come 
        // first.
        Collections.sort(ret, new Comparator<I>() {
            @Override
            public int compare(I item1, I item2) {
                return Double.compare(map2.get(item2), map2.get(item1));
            }
        });
        
        return ret;
    }
}
