package net.coderodde.associationanalysis.model.support;

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

    /**
     * Constructs this itemset generator. The underlying algorithm is 
     * "Apriori algorithm".
     * 
     * @param comparator the comparator for items.
     */
    public AprioriFrequentItemsetGenerator(final Comparator<I> comparator) {
        super(comparator);
    }
    
    /**
     * Mines frequent patterns from the transaction list. A frequent pattern is
     * any itemset having support at least <code>minimumSupport</code>.
     * 
     * @param transactionList the list of target transactions.
     * @param minimumSupport  the minimum support.
     * @return a data object describing the frequent patterns and their support
     *         counts.
     */
    @Override
    public FrequentItemsetData<I> 
        findFrequentItemsets(final List<Set<I>> transactionList,
                             final double minimumSupport) {
        final AprioriSupportCountFunction<I> supportCountFunction = 
                new AprioriSupportCountFunction<>(transactionList.size());
        
        final Map<Integer, Set<Set<I>>> map = new HashMap<>();
        
        map.put(1, new LinkedHashSet<Set<I>>());
        
        int line = 0;
        
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
            
            ++line;
            
            if (line % 10000 == 0) {
                System.out.println("Ln: " + line);
            }
        }
        
        int k = 1;
        
        do {
            ++k;
            
            System.out.println("k = " + k);
            
            final Set<Set<I>> candidateList = 
                    generateCandidates(map.get(k - 1));
            
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
                                       transactionList.size()));
            
        } while (!map.get(k).isEmpty());
        
        return new FrequentItemsetData<>(extractFrequentItemsets(map),
                                         supportCountFunction,
                                         transactionList.size());
    }
}
