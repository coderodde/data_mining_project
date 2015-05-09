package net.coderodde.associationanalysis.model;

import java.util.HashSet;
import java.util.Set;
import net.coderodde.moviemine.model.AssociationRule;

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
    
    /**
     * Returns the confidence of the rule <code>rule</code>.
     * 
     * @param  rule the target rule.
     * @return confidence value.
     */
    public double confidence(final AssociationRule<I> rule) {
        final Set<I> set = new HashSet<>(rule.getAntecedent().size() + 
                                         rule.getConsequent().size());
        set.addAll(rule.getAntecedent());
        set.addAll(rule.getConsequent());
        
        final int itemsetSupportCount = getSupportCount(set);
        final int antecedentSupportCount = 
                getSupportCount(rule.getAntecedent());
        
        return 1.0 * itemsetSupportCount / antecedentSupportCount;
    }
}
