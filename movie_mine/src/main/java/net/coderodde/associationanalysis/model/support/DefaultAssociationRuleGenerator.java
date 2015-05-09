package net.coderodde.associationanalysis.model.support;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.coderodde.associationanalysis.model.AbstractAssociationRuleGenerator;
import net.coderodde.associationanalysis.model.FrequentItemsetData;
import net.coderodde.moviemine.model.AssociationRule;

/**
 * This class implements a default algorithm for association rule extraction.
 * 
 * @author Rodion Efremov
 * @version 1.6 
 */
public class DefaultAssociationRuleGenerator<I> 
extends AbstractAssociationRuleGenerator<I> {

    /**
     * {@inheritDoc }
     * 
     * @param data              the itemset and support count map data.
     * @param minimumConfidence the minimum confidence.
     * @return the list of association rules.
     */
    @Override
    public List<AssociationRule<I>> mine(final FrequentItemsetData<I> data, 
                                         final double minimumConfidence) {
        final List<AssociationRule<I>> inputRuleList =
                extractPreliminaryRules(data);
        
        final List<AssociationRule<I>> associationRuleList = 
                new ArrayList<>();
        
        for (final Set<I> itemset : data.getFrequentItemsets()) {
            if (itemset.size() < 2) {
                // Any association rule requires at least 2 items. Not available
                // so skip this one.
                continue;
            }
            
            associationRuleList.addAll(
                    generateAssociationRules(itemset,        
                                             inputRuleList,
                                             data,
                                             minimumConfidence));
        }
        
        return associationRuleList;
    }
    
    /**
     * Extracts all possible association rules with one-element consequents.
     * 
     * @param  data the frequent pattern data.
     * @return the list of association rules.
     */
    private List<AssociationRule<I>> 
        extractPreliminaryRules(final FrequentItemsetData<I> data) {
        final List<AssociationRule<I>> ret = new ArrayList<>();
        final Set<AssociationRule<I>> set = new HashSet<>();
        final Set<I> workSet = new HashSet<>(1);
        
        final Set<I> antecedent = new HashSet<>();
        final Set<I> consequent = new HashSet<>(1);
        
        for (final Set<I> itemset : data.getFrequentItemsets()) {
            if (itemset.size() < 2) {
                // Not possible to make an association rule out of an itemset
                // of 0 or 1 items.
                continue;
            }
            
            for (final I item : itemset) {
                antecedent.clear();
                consequent.clear();
                
                antecedent.addAll(itemset);
                antecedent.remove(item);
                consequent.add(item);
                
                final AssociationRule<I> rule = 
                        new AssociationRule<>(antecedent,
                                              consequent);
                
                set.add(rule);
                ret.add(rule);
            }
        }
        
        System.out.println("extractPreliminaryRules - ret: " + ret.size() +
                           ", set: " + set.size());
        
        return ret;
    }
        
    private List<AssociationRule<I>>
    generateAssociationRules(final Set<I> itemset,
                             final List<AssociationRule<I>> rules,
                             final FrequentItemsetData<I> data,
                             final double minimumConfidence) {
        final Set<AssociationRule<I>> set = new HashSet<>();
        final int itemsetSize = itemset.size();
        final int consequentSize = rules.get(0).getConsequent().size();
        final Set<I> workSet = new HashSet<>();
        
        if (itemsetSize > consequentSize + 1) {
            // There is room for ripping an item from antecedent and putting it 
            // in the consequent.
            final List<AssociationRule<I>> nextRuleList = 
                    generateNextRules(rules);
            
            final Iterator<AssociationRule<I>> iterator =
                    nextRuleList.iterator();
            
            while (iterator.hasNext()) {
                final AssociationRule<I> rule = iterator.next();
                
                final int itemsetSupportCount = data.getSupportCountFunction()
                                                    .getSupportCount(itemset);
                workSet.clear();
                workSet.addAll(itemset);
                workSet.removeAll(rule.getConsequent());
                
                final int antecedentSupportCount = 
                        data.getSupportCountFunction()
                            .getSupportCount(workSet);
                
                final double confidence = 1.0 * itemsetSupportCount 
                                              / antecedentSupportCount;
                
                if (confidence >= minimumConfidence) {
                    set.add(rule);
                } else {
                    // Remove 'rule'.
                    iterator.remove();
                }
            }
        }
        
        return new ArrayList<>(set);
    }
    
    private List<AssociationRule<I>>
    generateNextRules(final List<AssociationRule<I>> ruleList) {
        final Set<AssociationRule<I>> set = new HashSet<>();
        
        for (final AssociationRule<I> rule : ruleList) {
            if (rule.getAntecedent().size() < 2) {
                // The rule antecedent does not have enough items to move
                // from the antecedent to consequent.
                continue;
            }
            
            for (final I antecedentItem : rule.getAntecedent()) {
                final Set<I> newAntecedent = 
                        new HashSet<>(rule.getAntecedent());
                
                final Set<I> newConsequent = 
                        new HashSet<>(rule.getConsequent().size() + 1);
                
                newAntecedent.remove(antecedentItem);
                newConsequent.addAll(rule.getConsequent());
                newConsequent.add(antecedentItem);
                
                final AssociationRule<I> newRule =
                        new AssociationRule<>(newAntecedent,
                                              newConsequent);
                
                set.add(newRule);
            }
        }
        
        return new ArrayList<>(set);
    }
}
