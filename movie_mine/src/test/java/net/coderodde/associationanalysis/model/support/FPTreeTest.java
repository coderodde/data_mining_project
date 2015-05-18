package net.coderodde.associationanalysis.model.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;

public class FPTreeTest {

    @Test
    public void testPutSupportCount() {
        final Map<String, Integer> countMap = new HashMap<>();
        
        countMap.put("A", 2);
        countMap.put("B", 2);
        countMap.put("C", 2);
        countMap.put("D", 2);
        countMap.put("E", 1);
        
        final FPTree<String> tree = new FPTree<>(3, 3, countMap);
        final List<Set<String>> transactions = new ArrayList<>();
        
        transactions.add(new HashSet<>(Arrays.asList("A", "B")));
        transactions.add(new HashSet<>(Arrays.asList("B", "C", "D")));
        transactions.add(new HashSet<>(Arrays.asList("A", "C", "D", "E")));
        
        for (final Set<String> transaction : transactions) {
            tree.putSupportCount(transaction, 0);
        }
        
        final FPTree<String> clone = tree.clone();
        
        assertEquals(tree, clone);
    }
    
    @Test
    public void testDebug() {
        final String a = "a";
        final String b = "b";
        final String c = "c";
        final String d = "d";
        final String e = "e";
        
        final Map<String, Integer> countMap = new HashMap<>();
        
        countMap.put("a", 8);
        countMap.put("b", 4);
        countMap.put("c", 6);
        countMap.put("d", 5);
        countMap.put("e", 3);
        
        final FPTree<String> tree = new FPTree<>(10, 2, countMap);
        
        tree.putSupportCount(asSet(a, b), 0);
        tree.putSupportCount(asSet(b, c, d), 0);
        tree.putSupportCount(asSet(a, c, d, e), 0);
        tree.putSupportCount(asSet(a, d, e), 0);
        tree.putSupportCount(asSet(a, b, c), 0);
        tree.putSupportCount(asSet(a, b, c, d), 0);
        tree.putSupportCount(asSet(a), 0);
        tree.putSupportCount(asSet(a, b, c), 0);
        tree.putSupportCount(asSet(a, b, d), 0);
        tree.putSupportCount(asSet(b, c, e), 0);
        
        final FPTree<String> other = tree.getConditionalFPTree(c);
    }
    
    public static <I> Set asSet(I... items) {
        return new HashSet<>(Arrays.asList(items));
    }
}
