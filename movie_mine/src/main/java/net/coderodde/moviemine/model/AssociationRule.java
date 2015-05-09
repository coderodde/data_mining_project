package net.coderodde.moviemine.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * This class models an association rule.
 * 
 * @author Rodion Efremov
 * @version 1.6
 * @param <I> the item type.
 */
    public class AssociationRule<I> {
    
    /**
     * The set of items in the antecedent of this rule.
     */
    private final Set<I> antecedent;
    
    /**
     * The set of items in the consequent of this rule.
     */
    private final Set<I> consequent;
    
    /**
     * Creates a new association rule.
     * 
     * @param antecedent the antecedent.
     * @param consequent the consequent.
     */
    public AssociationRule(final Set<I> antecedent,
                           final Set<I> consequent) {
        this.antecedent = new HashSet<>(antecedent);
        this.consequent = new HashSet<>(consequent);
    }
    
    /**
     * Returns an unmodifiable view of the antecedent of this association rule.
     * 
     * @return the antecedent.
     */
    public Set<I> getAntecedent() {
        return Collections.unmodifiableSet(antecedent);
    }
    
    /**
     * Returns an unmodifiable view of the consequent of this association rule.
     * 
     * @return the consequent.
     */
    public Set<I> getConsequent() {
        return Collections.unmodifiableSet(consequent);
    }
    
    /**
     * Returns the string representation of this association rule.
     * 
     * @return the string representation.
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        
        int index = 0;
        
        for (final I item : antecedent) {
            sb.append(item);
            
            if (index < antecedent.size() - 1) {
                sb.append(", ");
            }
            
            ++index;
        }
        
        sb.append("} -> {");
        
        index = 0;
        
        for (final I item : consequent ){
            sb.append(item);
            
            if (index < consequent.size() - 1) {
                sb.append(", ");
            }
            
            ++index;
        }
        
        return sb.append("}").toString();
    }
    
    /**
     * Returns the hash code of this association rule which depends only on
     * rule antecedent and consequent.
     * @return 
     */
    @Override
    public int hashCode() {
        return antecedent.hashCode() ^ consequent.hashCode();
    }

    /**
     * Returns true if this rule and <code>obj</code> encode the same 
     * association rule.
     * 
     * @param  obj the object to test for equality.
     * @return <code>true</code> if the two objects encode the same rule.
     */
    @Override
    public boolean equals(Object obj) {
        final AssociationRule<I> other = (AssociationRule<I>) obj;
        
        return antecedent.equals(other.antecedent)
                && consequent.equals(other.consequent);
    }
}
