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
}
