package net.coderodde.associationanalysis.model;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import net.coderodde.moviemine.util.Utilities;

/**
 * This class holds the data needed to generate association rules.
 * 
 * @param <I> the actual item type.
 */
public class FrequentItemsetData<I> {

    /**
     * Holds all frequent itemsets.
     */
    private final List<Set<I>> frequentItemsets;

    /**
     * Holds the support count function.
     */
    private final AbstractSupportCountFunction<I> supportCountFunction;

    /**
     * The amount of distinct transactions considered in order to generate this
     * data.
     */
    private final int transactionAmount;
    
    /**
     * Constructs this data holder.
     * 
     * @param frequentItemsets     the list of frequent itemsets.
     * @param supportCountFunction the support count function.
     * @param transactionAmount    the amount of transactions.
     */
    public FrequentItemsetData(
            final List<Set<I>> frequentItemsets,
            final AbstractSupportCountFunction<I> supportCountFunction,
            final int transactionAmount) {
        this.frequentItemsets = frequentItemsets;
        this.supportCountFunction = supportCountFunction;
        this.transactionAmount = transactionAmount;
        Collections.sort(frequentItemsets,
                         new Utilities.MovieItemsetComparatorBySupport
                         (this.supportCountFunction));
    }
    
    /**
     * Returns the string representation of the input itemset.
     * 
     * @param <I>     the item type.
     * @param itemset the target itemset.
     * @return        a string.
     */
    public static <I> String toString(final Set<I> itemset) {
        final StringBuilder sb = new StringBuilder("{");
        
        int index = 0;
        
        for (final I item : itemset) {
            sb.append(item);
            
            if (index < itemset.size() - 1) {
                sb.append(", ");
            }
            
            ++index;
        }
        
        return sb.append('}').toString();
    }

    /**
     * Returns the list of frequent itemsets.
     * 
     * @return the list of itemsets.
     */
    public List<Set<I>> getFrequentItemsets() {
        return frequentItemsets;
    }

    /**
     * Returns the support count function.
     * 
     * @return the support count function.
     */
    public AbstractSupportCountFunction<I> getSupportCountFunction() {
        return supportCountFunction;
    }
    
    /**
     * Returns the amount of transactions considered for this data.
     * 
     * @return the amount of transactions.
     */
    public int getTransactionAmount() {
        return transactionAmount;
    }
}