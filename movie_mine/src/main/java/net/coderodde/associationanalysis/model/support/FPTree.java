package net.coderodde.associationanalysis.model.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
        FPTreeNode(I item, FPTreeNode<I> parent) {
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
        
        @Override
        public int hashCode() {
            return item.hashCode() ^ childMap.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof FPTreeNode)) {
                return false;
            }
            
            final FPTreeNode<I> other = (FPTreeNode<I>) obj;
            return this.item.equals(other.item) 
                    && this.childMap.equals(other.childMap);
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
     * The minimum support count for guaranteeing frequentness.
     */
    private final int minimumSupportCount;
    
    /**
     * Constructs a new FP-tree.
     * 
     * @param transactionAmount   the amount of transactions this tree covers.
     * @param minimumSupportCount the minimum support count.
     */
    public FPTree(int transactionAmount, int minimumSupportCount) {
        super(transactionAmount);
        this.map = new HashMap<>();
        this.root = new FPTreeNode(null, null);
        this.minimumSupportCount = minimumSupportCount;
    }
    
    /**
     * Constructs a copy of this input FP-tree.
     * 
     * @param other the FP-tree to copy.
     */
    public FPTree(FPTree<I> other) {
        this(other.transactionAmount, other.minimumSupportCount);
        copyFPTreeNode(root, other.root);
    }
    
    /**
     * Checks whether this FP-tree is empty.
     * 
     * @return <code>true</code> if and only if this tree.
     */
    public boolean isEmpty() {
        return root.childMap.isEmpty();
    }
    
    /**
     * If this tree is simply a path, returns the minimum support count of nodes
     * in this tree (path). Otherwise returns -1.
     * 
     * @return the minimum count of this path, or -1 if this tree is not a path.
     */
    public int getPathSupportCount() {
        if (!isPath()) {
            return -1;
        }
        
        int minimumCount = Integer.MAX_VALUE;
        
        for (FPTreeNode<I> node : map.values()) {
            minimumCount = Math.min(minimumCount, node.count);
        }
        
        return minimumCount;
    }
    
    /**
     * Returns the list containing unique items of this tree.
     * 
     * @return the list of unique items.
     */
    public List<I> toItemList() {
        final Set<I> set = new HashSet<>();
        
        for (final FPTreeNode<I> node : root.childMap.values()) {
            toItemList(node, set);
        }
        
        return new ArrayList<>(set);
    }
    
    /**
     * Returns an array of header items.
     * 
     * @return an array.
     */
    public Object[] getHeaderItems() {
        final Object[] itemArray = map.keySet().toArray();
        final Map<Object, Integer> countMap = new HashMap<>();
        
        FPTreeNode<I> node;
        
        for (final Object itemObject : itemArray) {
            final I item = (I) itemObject;
            int count = 0;
            
            for (node = map.get(item); node != null; node = node.next) {
                count += node.count;
                System.out.println(count);
                
                if (count >= 2000) {
                    System.exit(-1);
                }
            }
            
            countMap.put(item, count);
        }
        
        
        // Sorts such that the items with smaller counts appear at the beginning
        // of the item array.
        Arrays.sort(itemArray, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return Integer.compare(countMap.get(o1), countMap.get(o2));
            }
        });
        
        return itemArray;
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
     * Returns the support count of <code>itemset</code>.
     * 
     * @param  itemset the target itemset.
     * @return the support count.
     */
    @Override
    public int getSupportCount(Set<I> itemset) {
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
        
        while (itemIndex < itemlist.size() 
                && (child = current.getChildNode(itemlist.get(itemIndex))) 
                != null) {
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
            final FPTreeNode<I> node = new FPTreeNode<>(item, current);
            
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
     * Generates and returns a conditional FP-tree from <code>item</code>.
     * 
     * @param  item the item to exclude.
     * @return a conditional FP-tree.
     */
    public FPTree<I> getConditionalFPTree(I item) {
        final FPTree<I> tree = clone();
        
        // Update the counts.
        tree.updateCounts(item);
        
        // Remove the chain for 'item'.
        removeChain(tree.root, item);
        
        // Remove infrequent items.
        tree.removeInfrequentItemChains();
        return tree;
    }
    
    /**
     * Updates the counts of this tree prior to removing all nodes holding 
     * <code>item</code>.
     * 
     * @param item the target item.
     */
    private void updateCounts(I item) {
        this.clearCount();
        updateCounts(item, root);
    }
    
    /**
     * Implements the counter update.
     * 
     * @param item the target item.
     * @param node the FP-tree node to process.
     * @return 
     */
    private int updateCounts(I item, FPTreeNode<I> node) {
        if (node.item != null && node.item.equals(item)) {
            return 1;
        }
        
        int sum = 0;
        
        for (final FPTreeNode<I> child : node.childMap.values()) {
            sum += updateCounts(item, child);
        }
        
        node.count = sum;
        return sum;
    }
    
    /**
     * Prunes the infrequent items from this tree.
     */
    private void removeInfrequentItemChains() {
        final Map<FPTreeNode<I>, FPTreeNode<I>> parentMap = getParentMap();
        final Set<I> keysToRemove = new HashSet<>();
        
        for (final FPTreeNode<I> node : map.values()) {
            int count = 0;
            
            // Count the support count for 'node.item'.
            for (FPTreeNode<I> tmp = node; tmp != null; tmp = tmp.next) {
                count += node.count;
            }
            
            if (count < minimumSupportCount) {
                // Remove all the nodes with items 'tmp.item'.
                for (FPTreeNode<I> tmp = node; tmp != null; tmp = tmp.next) {
                    final FPTreeNode<I> parent = parentMap.get(tmp);
                    
                    if (parent == null) {
                        // tmp is the root (also called null) node.
                        root.childMap.remove(tmp.item);
                        
                        for (final Map.Entry<I, FPTreeNode<I>> entry :
                                tmp.childMap.entrySet()) {
                            root.childMap.put(entry.getKey(), entry.getValue());
                        }
                    } else {
                        parent.childMap.remove(tmp.item);

                        for (final Map.Entry<I, FPTreeNode<I>> entry : 
                                tmp.childMap.entrySet()) {
                            parent.childMap.put(entry.getKey(), 
                                                entry.getValue());
                        }
                    }
                }
                
                keysToRemove.add(node.item);
            }
        }
        
        for (final I itemKey : keysToRemove) {
            map.remove(itemKey);
        }
    }
    
    /**
     * Computes a map which maps each FP-tree node to its parent FP-tree node.
     * 
     * @return a map.
     */
    private Map<FPTreeNode<I>, FPTreeNode<I>> getParentMap() {
        final Map<FPTreeNode<I>, FPTreeNode<I>> ret = new HashMap<>();
        final Map<I, FPTreeNode<I>> tmp = root.childMap;
        
        Iterator<Map.Entry<I, FPTreeNode<I>>> it = tmp.entrySet().iterator();
        FPTreeNode<I> current = it.next().getValue();
        
        ret.put(current, null);
        getParentMap(ret, current);
        
        return ret;
    }
    
    /**
     * Implements <code>getParentMap</code>.
     * 
     * @param map  the map for accumulating the mappings.
     * @param node the node to process.
     */
    private void getParentMap(Map<FPTreeNode<I>, FPTreeNode<I>> map, 
                              FPTreeNode<I> node) {
        for (final FPTreeNode<I> child : node.childMap.values()) {
            map.put(child, node);
            // Recur to the lower tree levels.
            getParentMap(map, child);
        }
    }
    
    private void removeChain(FPTreeNode<I> node, I item) {
        if (node.childMap.containsKey(item)) {
            node.childMap.remove(item);
        } else {
            for (final FPTreeNode<I> child : node.childMap.values()) {
                removeChain(child, item);
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
    
    /**
     * Checks whether this tree contains just a single branch. In other words,
     * checks whether this tree is simply a path (with one root and one leaf
     * node).
     * 
     * @return <code>true</code> if this tree consists of a single path.
     */
    private boolean isPath() {
        for (final Map.Entry<I, FPTreeNode<I>> entry : map.entrySet()) {
            if (entry.getValue().next != null) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Clears the counts of each node in this tree.
     */
    private void clearCount() {
        clearCount(root);
    }
    
    /**
     * Implements the routine for zeroing tree node counts.
     * 
     * @param node the root node of the subtree to clear.
     */
    private void clearCount(FPTreeNode<I> node) {
        node.count = 0;
        
        for (final FPTreeNode<I> child : node.childMap.values()) {
            clearCount(child);
        }
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
        for (final FPTreeNode<I> n : otherTreeNode.childMap.values()) {
            final FPTreeNode<I> newnode = new FPTreeNode<>(n.item, 
                                                           thisTreeNode);
            newnode.count = n.count;
            
            if (map.containsKey(newnode.item)) {
                newnode.next = map.get(newnode.item);
            }
            
            map.put(newnode.item, newnode);
            thisTreeNode.childMap.put(newnode.item, newnode);
            copyFPTreeNode(newnode, n);
        }
    }
    
    private void toItemList(FPTreeNode<I> node, Set<I> set) {
        set.add(node.item);
        
        for (FPTreeNode<I> childNode : node.childMap.values()) {
            toItemList(childNode, set);
        }
    }
}
