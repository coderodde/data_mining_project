package net.coderodde.associationanalysis.model;

import java.util.List;
import java.util.Set;

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
     * Constructs this data holder.
     * 
     * @param frequentItemsets     the list of frequent itemsets.
     * @param supportCountFunction the support count function.
     */
    public FrequentItemsetData(
            final List<Set<I>> frequentItemsets,
            final AbstractSupportCountFunction<I> supportCountFunction) {
        this.frequentItemsets = frequentItemsets;
        this.supportCountFunction = supportCountFunction;
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
}