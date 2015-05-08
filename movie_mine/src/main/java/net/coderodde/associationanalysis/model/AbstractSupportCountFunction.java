package net.coderodde.associationanalysis.model;

import java.util.Set;

/**
 * This abstract class defines the API for support count functions.
 * 
 * @author Rodion Efremov
 * @version 1.6
 * @param <I> the actual item type.
 */
public abstract class AbstractSupportCountFunction<I> {
    
    /**
     * Returns the current support count for <code>itemset</code>.
     * 
     * @param  itemset the itemset whose support count to retrieve.
     * @return the support count of <code>itemset</code> or zero if there is no
     *         mapping for <code>itemset</code>.
     */
    public abstract int getSupportCount(final Set<I> itemset);
    
    /**
     * Sets a support count for <code>itemset</code>.
     * 
     * @param itemset      the target itemset.
     * @param supportCount the support count for <code>itemset</code>.
     */
    public abstract void putSupportCount(final Set<I> itemset, 
                                         final int supportCount);
    
    /**
     * Increments the support count of <code>itemset</code> by one.
     * 
     * @param itemset the target itemset.
     */
    public void increaseSupportCount(final Set<I> itemset) {
        putSupportCount(itemset, getSupportCount(itemset) + 1);
    }
}
