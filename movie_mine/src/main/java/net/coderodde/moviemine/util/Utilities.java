package net.coderodde.moviemine.util;

import java.util.Comparator;
import java.util.Set;
import net.coderodde.associationanalysis.model.AbstractSupportCountFunction;
import net.coderodde.moviemine.model.AssociationRule;
import net.coderodde.moviemine.model.Movie;

/**
 * This class contains general utility methods.
 * 
 * @author Rodion Efremov
 * @version 1.6
 */
public class Utilities {
    
    /**
     * This method returns the string representation of a movie itemset.
     * 
     * @param  set the itemset.
     * @return the string representation.
     */
    public static String toString(final Set<Movie> set) {
        final StringBuilder sb = new StringBuilder("{");
        
        int index = 0;
        
        for (final Movie movie : set) {
            sb.append("\"").append(movie.getTitle()).append("\"");
            
            if (index++ < set.size() - 1) {
                sb.append(", ");
            }
        }
        
        return sb.append("}").toString();
    }
    
    /**
     * This method returns the string representation of a movie association
     * rule.
     * 
     * @param  rule the rule.
     * @return the string representation.
     */
    public static String toString(final AssociationRule<Movie> rule) {
        return toString(rule.getAntecedent()) + " -> " 
                                              + toString(rule.getConsequent());
    }
    
    /**
     * This class implements an itemset comparator. The implied order 
     * is descending by support.
     * 
     * @param <I> the item type.
     */
    public static class ItemsetComparatorBySupport<I>
    implements Comparator<Set<I>> {

        /**
         * The support count function.
         */
        private final AbstractSupportCountFunction<I> supportCountFunction;
        
        /**
         * Constructs this comparator.
         * 
         * @param supportCountFunction the support count function.
         */
        public ItemsetComparatorBySupport
        (final AbstractSupportCountFunction<I> supportCountFunction) {
            this.supportCountFunction = supportCountFunction;
        }
        
        /**
         * Compares the two input itemsets.
         * 
         * @param  o1 the first itemset.
         * @param  o2 another itemset.
         * @return an order value. The itemset with larger support will be
         *         considered to precede the other.
         */
        @Override
        public int compare(final Set<I> o1, final Set<I> o2) {
            return Double.compare(supportCountFunction.getSupport(o2),
                                  supportCountFunction.getSupport(o1));
        }
    }
    
    /**
     * This class implements an association rule comparator. The implied order
     * is descending by confidence.
     * 
     * @param <I> the item type.
     */
    public static class AssociationRuleComparatorByConfidence<I>
    implements Comparator<AssociationRule<I>> {
        
        /**
         * The support count function.
         */
        private final AbstractSupportCountFunction<I> supportCountFunction;
        
        /**
         * Constructs this comparator.
         * 
         * @param supportCountFunction the support count function.
         */
        public AssociationRuleComparatorByConfidence
        (final AbstractSupportCountFunction<I> supportCountFunction) {
            this.supportCountFunction = supportCountFunction;
        }
        
        /**
         * Compares the two input association rules.
         * 
         * @param  o1 the first association rule.
         * @param  o2 another association rule.
         * @return an order value. The rule with larger confidence will be 
         *         considered to precede the other.
         */
        @Override
        public int compare(final AssociationRule<I> o1,
                           final AssociationRule<I> o2) {
            return Double.compare(supportCountFunction.getConfidence(o2), 
                                  supportCountFunction.getConfidence(o1));
        }
    }
}
