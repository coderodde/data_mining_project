package net.coderodde.associationanalysis.model.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.coderodde.associationanalysis.model.AbstractSupportCountFunction;

/**
 * This class implements a FP-tree.
 * 
 * @author Rodion Efremov
 * @version 1.6
 * @param <I> the type of actual items.
 */
public class FPTree<I extends Comparable<? super I>> 
extends AbstractSupportCountFunction<I> {

    /**
     * This class implements a node of a FP-tree.
     * 
     * @param <I> the actual item type.
     */
    private static class FPTreeNode<I> {
        
        /**
         * The item this node holds.
         */
        private final I item;
        
        /**
         * The count of this tree.
         */
        private int count;
        
        /**
         * Link to the next node holding the same item.
         */
        private FPTreeNode<I> next;
        
        /**
         * Maps each child to the tree node containing it.
         */
        private final Map<I, FPTreeNode<I>> childMap;
        
        /**
         * Constructs a new FP-tree node with <code>item</code>.
         * 
         * @param item the item for this node.
         */
        FPTreeNode(I item) {
            this.item = item;
            this.count = 1;
            this.childMap = new HashMap<>();
        }
        
        /**
         * Returns the tree node containing <code>item</code>.
         * 
         * @param  item the child item.
         * @return the node containing <code>item</code> or <code>null</code> if
         *         there is no such.
         */
        FPTreeNode<I> getChildNode(I item) {
            return childMap.get(item);
        }
    }
    
    /**
     * The root node of this tree. Does not store items.
     */
    private final FPTreeNode<I> root;
    
    /**
     * Maps each item to the first FP-tree node in this tree.
     */
    private final Map<I, FPTreeNode<I>> map;
    
    /**
     * Constructs a new FP-tree.
     * 
     * @param transactionAmount the amount of transactions this tree covers.
     */
    public FPTree(int transactionAmount) {
        super(transactionAmount);
        this.map = new HashMap<>();
        this.root = new FPTreeNode(null);
    }
    
    /**
     * Constructs a copy of this input FP-tree.
     * 
     * @param other the FP-tree to copy.
     */
    public FPTree(FPTree<I> other) {
        this(other.transactionAmount);
        copyFPTreeNode(root, other.root);
    }
    
    /**
     * Checks whether this tree and <code>o</code> represent the same FP-tree.
     * 
     * @param  o the object for equality test. 
     * @return <code>true</code> only if <code>o</code> is a FP-tree and has the
     *         same contents as this FP-tree.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof FPTree)) {
            return false;
        }
        
        final FPTree<I> other = (FPTree<I>) o;
        
        return equals(this.root, other.root);
    }
    
    /**
     * Implements the FP-tree equality test.
     * 
     * @param thisTreeNode  the node being tested for equality.
     * @param otherTreeNode the node in the second tree and at the same 
     *                      location. In other words, <code>otherTreeNode</code>
     *                      has the same depth as the first input node and the 
     *                      same location within the tree level.
     * @return <code>true</code> if the two subtrees rooted at
     *         <code>thisTreeNode</code> and <code>otherTreeNode</code> are 
     *         identical.
     */
    private boolean equals(FPTreeNode<I> thisTreeNode, 
                           FPTreeNode<I> otherTreeNode) {
        if (thisTreeNode.item != otherTreeNode.item) {
            return false;
        }
        
        if (thisTreeNode.count != otherTreeNode.count) {
            return false;
        }
        
        if (thisTreeNode.childMap.size() != otherTreeNode.childMap.size()) {
            return false;
        }
        
        for (final I item : thisTreeNode.childMap.keySet()) {
            if (!otherTreeNode.childMap.containsKey(item)) {
                return false;
            }
            
            if (!equals(thisTreeNode.childMap.get(item),
                        otherTreeNode.childMap.get(item))) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Copies children of <code>nodeOfOther</code> to <code>nodeOfThis</code>.
     * 
     * @param thisTreeNode  the current node of this FP-tree.
     * @param otherTreeNode the node of the other FP-tree.
     */
    private void copyFPTreeNode(FPTreeNode<I> thisTreeNode,
                                FPTreeNode<I> otherTreeNode) {
        int i = 0;
        ++i;
        for (final FPTreeNode<I> n : otherTreeNode.childMap.values()) {
            final FPTreeNode<I> newnode = new FPTreeNode<>(n.item);
            newnode.count = n.count;
            
            if (map.containsKey(newnode.item)) {
                newnode.next = map.get(newnode.item);
            }
            
            map.put(newnode.item, newnode);
            thisTreeNode.childMap.put(newnode.item, newnode);
            copyFPTreeNode(newnode, n);
        }
    }
    
    /**
     * Returns the support count of <code>itemset</code>.
     * 
     * @param  itemset the target itemset.
     * @return the support count.
     */
    @Override
    public int getSupportCount(final Set<I> itemset) {
        return Integer.MIN_VALUE;
    }

    /**
     * Adds the <code>itemset</code> to this FP-tree.
     * 
     * @param itemset      the itemset to add.
     * @param supportCount ignored.
     */
    @Override
    public void putSupportCount(Set<I> itemset, int supportCount) {
        final List<I> itemlist = new ArrayList<>(itemset);
        Collections.sort(itemlist);
        
        FPTreeNode<I> current = root;
        FPTreeNode<I> child;
        
        int itemIndex = 0;
        
        while ((child = current
                        .getChildNode(itemlist.get(itemIndex))) != null) {
            current = child;
            current.count++;
            itemIndex++;
            
            if (!map.containsKey(current.item)) {
                map.put(current.item, current);
            } else if (map.get(current.item) != current) {
                current.next = map.get(current.item);
                map.put(current.item, current);
            }
        } 
        
        while (itemIndex < itemlist.size()) {
            final I item = itemlist.get(itemIndex++);
            final FPTreeNode<I> node = new FPTreeNode<>(item);
            
            current.childMap.put(item, node);
            current = node;
            
            if (!map.containsKey(item)) {
                map.put(item, node);
            } else {
                node.next = map.get(item);
                map.put(item, node);
            }
        }
    }
    
    /**
     * Clones this FP-tree and returns the clone.
     * 
     * @return a FP-tree.
     */
    @Override
    public FPTree<I> clone() {
        return new FPTree<>(this);
    }
}
