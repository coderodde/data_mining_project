package net.coderodde.associationanalysis.model;

import java.util.List;
import net.coderodde.moviemine.model.AssociationRule;

/**
 * This abstract class defines the API for an association rule generator.
 * 
 * @author Rodion Efremov
 * @version 1.6
 * @param <I> the actual type of items.
 */
public abstract class AbstractAssociationRuleGenerator<I> {
    
    /**
     * Mines all possible association rules from <code>data</code>, each with 
     * confidence no less than <code>minimumConfidence</code>.
     * 
     * @param data              the data describing the support function and
     *                          the list of frequent itemsets.
     * @param minimumConfidence the minimum confidence.
     * @return the list of association rules.
     */
    public abstract List<AssociationRule<I>> 
        mine(final FrequentItemsetData<I> data,
             final double minimumConfidence);
}
