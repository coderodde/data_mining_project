package net.coderodde.associationanalysis.model.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
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
     * The comparator comparing items by their support counts. The element with
     * higher support count will occupy a smaller index within array or list.
     */
    private Comparator<I> supportCountComparator;
    
    /**
     * The root node of this tree. Does not store items.
     */
    private final FPTreeNode<I> root;
    
    /**
     * Maps each item to the first FP-tree node in this tree.
     */
    private final Map<I, FPTreeNode<I>> map;
    
    /**
     * Maps each item to its support count.
     */
    private final Map<I, Integer> countMap;
    
    /**
     * The minimum support count for guaranteeing frequentness.
     */
    private final int minimumSupportCount;
    
    /**
     * Constructs a new FP-tree.
     * 
     * @param transactionAmount   the amount of transactions this tree covers.
     * @param minimumSupportCount the minimum support count.
     * @param countMap            the map mapping each item to its support 
     *                            count.
     */
    public FPTree(int transactionAmount, 
                  int minimumSupportCount,
                  Map<I, Integer> countMap) {
        super(transactionAmount);
        this.map = new HashMap<>();
        this.root = new FPTreeNode(null);
        this.countMap = countMap;
        this.minimumSupportCount = minimumSupportCount;
        this.supportCountComparator = 
                new ItemComparatorBySupportCount<>(countMap);
    }
    
    /**
     * Constructs a copy of this input FP-tree.
     * 
     * @param other the FP-tree to copy.
     */
    public FPTree(FPTree<I> other) {
        this(other.transactionAmount, 
             other.minimumSupportCount, 
             other.countMap);
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
        final List<I> itemlist = new ArrayList<>(itemset);
        final Set<I> workSet = new HashSet<>(itemset);
        
        Collections.sort(itemlist, supportCountComparator);
        
        FPTreeNode<I> node = map.get(itemlist.get(0));
        int count = 0;
        
        for (; node != null; node = node.next) {
            count += countFrom(node, workSet);
        }
        
        return count;
    }
    
    private int countFrom(FPTreeNode<I> node, Set<I> workSet) {
        boolean nodeInWorkSet = false;
        
        if (workSet.contains(node.item)) {
            nodeInWorkSet = true;
            workSet.remove(node.item);
        }
        
        if (workSet.isEmpty()) {
            return node.count;
        }
        
        int count = 0;
        
        for (final FPTreeNode<I> child : node.childMap.values()) {
            count += countFrom(child, workSet);
        }
        
        if (nodeInWorkSet) {
            workSet.add(node.item);
        }
            
        return count;
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
        Collections.sort(itemlist, supportCountComparator);
        
        FPTreeNode<I> current = root;
        FPTreeNode<I> child;
        
        int itemIndex = 0;
        final int itemAmount = itemlist.size();
        
        outer:
        while (itemIndex < itemAmount 
                && (child = current.getChildNode(itemlist.get(itemIndex))) 
                != null) {
            current = child;
            current.count++;
            itemIndex++;
            
            if (!map.containsKey(current.item)) {
                map.put(current.item, current);
            } else {
                FPTreeNode<I> tmp = map.get(current.item);
                
                for (; tmp != null; tmp = tmp.next) {
                    if (tmp == current) {
                        // 'current' is already linked to a chain.
                        continue outer;
                    }
                }
                
                // 'current' is not present in a chain.
                current.next = map.get(current.item);
                map.put(current.item, current);
            }
        } 
        
        while (itemIndex < itemAmount) {
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
    
    private boolean hasCycle() {
        for (final FPTreeNode<I> node : map.values()) {
            FPTreeNode<I> tmp = node;
            
            Set<FPTreeNode<I>> set = new HashSet<>();
            
            while (tmp != null) {
                if (set.contains(tmp)) {
                    return true;
                }
                
                set.add(tmp);
            }
        }
        
        return false;
    }
    
    /**
     * Generates and returns a conditional FP-tree from <code>item</code>.
     * 
     * @param  item the item to exclude.
     * @return a conditional FP-tree.
     */
    public FPTree<I> getConditionalFPTree(I item) {
        final FPTree<I> tree = clone();
        
        // Update the counts of 'tree'.
        tree.updateCounts(item);

        // Remove all nodes containing 'item'.
        tree.removeItem(item);
        
        // Remove all linked lists with small total count.
        tree.pruneLists();
                
        // Remove all nodes with zero count.
        tree.pruneZeroCountNodes();
        return tree;
    }
    
    private void pruneZeroCountNodes() {
        final Map<FPTreeNode<I>, FPTreeNode<I>> parentMap = getParentMap();
        
        for (final FPTreeNode<I> start : map.values()) {
            final Map<FPTreeNode<I>, FPTreeNode<I>> precedenceMap =
                    getPrecedenceMap(start.item);
            
            final List<FPTreeNode<I>> list = new ArrayList<>();
            
            for (FPTreeNode<I> node = start; node != null; node = node.next) {
                if (node.count == 0) {
                    list.add(node);
                }
            }
            
            for (final FPTreeNode<I> node : list) {
                omit(node, parentMap.get(node));
                
                final FPTreeNode<I> predecessor = precedenceMap.get(node);
                
                if (predecessor == null) {
                    map.put(start.item, node.next);
                    precedenceMap.put(node.next, null);
                } else {
                    predecessor.next = node.next;
                    precedenceMap.put(node.next, predecessor);
                }
            }
        }
    }
    
    /**
     * Creates the precedence map for a particular linked list of 
     * <code>item</code>. The result map maps each FP-tree node to its 
     * predecessor FP-tree node.
     * 
     * @param  item the linked list selector.
     * @return precedence map.
     */
    private Map<FPTreeNode<I>, FPTreeNode<I>> getPrecedenceMap(I item) {
        final Map<FPTreeNode<I>, FPTreeNode<I>> ret = new HashMap<>();
        
        FPTreeNode<I> prev = null;
        FPTreeNode<I> current = map.get(item);
        
        while (current != null) {
            ret.put(current, prev);
            prev = current;
            current = current.next;
        }
        
        return ret;
    }
    
    private void pruneLists() {
        final Set<I> keys = new HashSet<>();
        
        for (FPTreeNode<I> start : map.values()) {
            final int count = getListCount(start);
            
            if (count < minimumSupportCount) {
                keys.add(start.item);
            }
        }
        
        for (final I key : keys) {
            removeLinkedList(key);
        }
    }
    
    private void removeLinkedList(I key) {
        final Map<FPTreeNode<I>, FPTreeNode<I>> parentMap = getParentMap();
        
        for (FPTreeNode<I> node = map.get(key); 
                node != null; 
                node = node.next) {
            final FPTreeNode<I> parent = parentMap.get(node);
            omit(node, parent);
        }
        
        map.remove(key);
    }
    
    private int getListCount(FPTreeNode<I> start) {
        int count = 0;
        
        for (; start != null; start = start.next) {
            count += start.count;
        }
        
        return count;
    }
    
    private void removeItem(I item) {   
        final Map<FPTreeNode<I>, FPTreeNode<I>> parentMap = getParentMap();
        FPTreeNode<I> itemNode = map.get(item);
        
        for (; itemNode != null; itemNode = itemNode.next) {
            omit(itemNode, parentMap.get(itemNode));
        }
        
        map.remove(item);
    }
    
    /**
     * Updates the counts of this tree prior to removing all nodes holding 
     * <code>item</code>.
     * 
     * @param item the target item.
     */
    private void updateCounts(I item) {
        clearCount(item);
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
            return node.count;
        }
        
        int sum = 0;
        
        for (final FPTreeNode<I> child : node.childMap.values()) {
            sum += updateCounts(item, child);
        }
        
        node.count = sum;
        return sum;
    }
    
    private void omit(FPTreeNode<I> node, FPTreeNode<I> parent) {
        if (parent == null) {
            return;
        }
        
        parent.childMap.remove(node.item);
        
        for (final FPTreeNode<I> childOfNode : node.childMap.values()) {
            parent.childMap.put(childOfNode.item, childOfNode);
        }
    }
    
    /**
     * Computes a map which maps each FP-tree node to its parent FP-tree node.
     * 
     * @return a map.
     */
    private Map<FPTreeNode<I>, FPTreeNode<I>> getParentMap() {
        final Map<FPTreeNode<I>, FPTreeNode<I>> ret = new HashMap<>();
        
        for (final FPTreeNode<I> node : root.childMap.values()) {
            ret.put(node, root);
            getParentMap(ret, node);
        }
        
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
    private void clearCount(I item) {
        clearCount(root, item);
    }
    
    /**
     * Implements the routine for zeroing tree node counts.
     * 
     * @param node the root node of the subtree to clear.
     */
    private void clearCount(FPTreeNode<I> node, I excludeItem) {
        if (node.item != null && !node.item.equals(excludeItem)) {
            node.count = 0;
        }
        
        for (final FPTreeNode<I> child : node.childMap.values()) {
            clearCount(child, excludeItem);
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
    
    private void toItemList(FPTreeNode<I> node, Set<I> set) {
        set.add(node.item);
        
        for (FPTreeNode<I> childNode : node.childMap.values()) {
            toItemList(childNode, set);
        }
    }
    
    private static class
            ItemComparatorBySupportCount<I extends Comparable<? super I>> 
    implements Comparator<I> {

        private final Map<I, Integer> supportCountMap;
        
        ItemComparatorBySupportCount(Map<I, Integer> supportCountMap) {
            this.supportCountMap = supportCountMap;
        }
        
        @Override
        public int compare(I o1, I o2) {
            final int cmp = Integer.compare(supportCountMap.get(o2),
                                            supportCountMap.get(o1));
            if (cmp != 0) {
                return cmp;
            }
            
            // Same support count, sort by item names.
            return o1.compareTo(o2);
        }
    }
}
