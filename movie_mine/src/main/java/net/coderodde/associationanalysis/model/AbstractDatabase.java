package net.coderodde.associationanalysis.model;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/**
 * This abstract class defines the API for a database.
 * 
 * @author Rodion Efremov
 * @version 1.6
 * 
 * @param <O> the type of transaction owner.
 * @param <I> the type of an item.
 */
public abstract class AbstractDatabase<O, I> {
    
    /**
     * Returns a list of transactions. Those transactions, whose owners do not
     * pass any of the predicates, is not included in the output list.
     * 
     * @param  predicates an array of predicates.
     * @return a list of transactions.
     */
    public abstract List<Set<I>> select(final Predicate<O>... predicates);
    
    /**
     * Returns the amount of transactions in this database.
     * 
     * @return the size of this database.
     */
    public abstract int size();
}
