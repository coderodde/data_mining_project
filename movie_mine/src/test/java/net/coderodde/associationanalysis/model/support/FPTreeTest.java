package net.coderodde.associationanalysis.model.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;

public class FPTreeTest {

    @Test
    public void testPutSupportCount() {
        final FPTree<String> tree = new FPTree<>(3, 3);
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
        
        final FPTree<String> tree = new FPTree<>(10, 2);
        
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
        
        final FPTree<String> other = tree.getConditionalFPTree(e);
        
        System.out.println("Yeah!!");
    }
    
    public static <I> Set asSet(I... items) {
        return new HashSet<>(Arrays.asList(items));
    }
}
