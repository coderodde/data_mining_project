package net.coderodde.associationanalysis.model.support;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.coderodde.associationanalysis.model.AbstractSupportCountFunction;

/**
 * This class implements a support count function by using a map from itemset to
 * its support count.
 * 
 * @author Rodion Efremov
 * @version 1.6
 * @param <I> the actual item type.
 */
public class AprioriSupportCountFunction<I> 
extends AbstractSupportCountFunction<I>{

    /**
     * The map mapping each itemset to its support count.
     */
    private final Map<Set<I>, Integer> map;
    
    /**
     * Constructs this support count function.
     */
    public AprioriSupportCountFunction() {
        this.map = new HashMap<>();
    }
    
    /**
     * {@inheritDoc }
     * 
     * @param  itemset the target itemset.
     * @return the support count of the target itemset.
     */
    @Override
    public int getSupportCount(final Set<I> itemset) {
        if (!map.containsKey(itemset)) {
            return 0;
        }
        
        return map.get(itemset);
        //return map.getOrDefault(itemset, 0);
    }

    /**
     * {@inheritDoc }
     * 
     * @param itemset      the target itemset.
     * @param supportCount the new support count of the target itemset.
     */
    @Override
    public void putSupportCount(Set<I> itemset, int supportCount) {
        map.put(itemset, supportCount);
    }
}
