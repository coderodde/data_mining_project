package net.coderodde.associationanalysis.model.support;

import java.util.ArrayList;
import java.util.HashMap;
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
public class FPGrowthFrequentItemsetGenerator<I> 
extends AbstractFrequentItemsetGenerator<I> {

    public FPGrowthFrequentItemsetGenerator() {
        super(null);
    }
    
    @Override
    public FrequentItemsetData<I> 
        findFrequentItemsets(final List<Set<I>> transactionList,
                             final double minimumSupport) {
        final List<I> frequentItems = findFrequentItems(transactionList,
                                                        minimumSupport);
        
        return null;
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
        
        for (final I item : map.keySet()) {
            final double support = 1.0 * map.get(item) / transactionList.size();
            
            if (support >= minimumSupport) {
                ret.add(item);
            }
        }
        
        return ret;
    }
}
