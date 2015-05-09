package net.coderodde.moviemine.model;

import java.util.Collections;
import java.util.Set;

/**
 * This class models an association rule.
 * 
 * @author Rodion Efremov
 * @version 1.6
 * @param <I> the item type.
 */
public class AssociationRule<I> {
    
    private final Set<I> antecedent;
    private final Set<I> consequent;
    
    public AssociationRule(final Set<I> antecedent,
                           final Set<I> consequent) {
        this.antecedent = antecedent;
        this.consequent = consequent;
    }
    
    public Set<I> getAntecedent() {
        return Collections.unmodifiableSet(antecedent);
    }
    
    public Set<I> getConsequent() {
        return Collections.unmodifiableSet(consequent);
    }
    
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
}
