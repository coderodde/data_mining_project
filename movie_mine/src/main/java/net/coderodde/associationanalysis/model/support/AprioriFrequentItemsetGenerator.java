package net.coderodde.associationanalysis.model.support;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.coderodde.associationanalysis.model.AbstractFrequentItemsetGenerator;
import net.coderodde.associationanalysis.model.FrequentItemsetData;

/**
 * This class implements the Apriori algorithm for frequent itemsets generation.
 * 
 * @author Rodion Efremov
 * @version 1.6
 * @param <I> the actual item type.
 */
public class AprioriFrequentItemsetGenerator<I> 
extends AbstractFrequentItemsetGenerator<I> {

    public AprioriFrequentItemsetGenerator(final Comparator<I> comparator) {
        super(comparator);
    }
    
    @Override
    public FrequentItemsetData<I> 
        findFrequentItemsets(final List<Set<I>> transactionList,
                             final double minimumSupport) {
        final AprioriSupportCountFunction<I> supportCountFunction = 
                new AprioriSupportCountFunction<>();
        
        final Map<Integer, Set<Set<I>>> map = new HashMap<>();
        
        map.put(1, new LinkedHashSet<Set<I>>());
        
        for (final Set<I> itemset : transactionList) {
            for (final I item : itemset) {
                final Set<I> oneItemset = new HashSet<>(1);
                oneItemset.add(item);
                supportCountFunction.increaseSupportCount(oneItemset);
                
                final double support = 
                        1.0 * supportCountFunction.getSupportCount(oneItemset)
                            / transactionList.size();
                
                if (support >= minimumSupport) {
                    map.get(1).add(oneItemset);
                }
            } 
        }
        
        int k = 1;
        
        do {
            ++k;
            
            final Set<Set<I>> candidateList = 
                    generateCandidates(map.get(k - 1));
            
            System.out.println("k = " + k);
            
            for (final Set<I> transaction : transactionList) {
                final Set<Set<I>> candidateList2 = subset(candidateList,
                                                          transaction);
                
                for (final Set<I> itemset : candidateList2) {
                    supportCountFunction.increaseSupportCount(itemset);
                }
            }
            
            map.put(k, getNextItemsets(candidateList,
                                       supportCountFunction,
                                       minimumSupport,
                                       transactionList));
            
        } while (!map.get(k).isEmpty());
        
        return new FrequentItemsetData<>(extractFrequentItemsets(map),
                                         supportCountFunction);
    }
}
