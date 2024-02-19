package JavaGenericsMatrix;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.NavigableMap;
import java.util.TreeMap;

public class TestNavigableMatrix {

    @Test
    public void testValue() {
        NavigableMap<Indexes, Integer> matrix = new TreeMap<>();
        matrix.put(new Indexes(0, 0), 1);
        matrix.put(new Indexes(1, 1), 2);
        NavigableMatrix<Integer> navMatrix = NavigableMatrix.from(matrix, 0);

        assertEquals(Integer.valueOf(1), navMatrix.value(new Indexes(0, 0)));
        assertEquals(Integer.valueOf(2), navMatrix.value(new Indexes(1, 1)));
        assertEquals(Integer.valueOf(0), navMatrix.value(new Indexes(2, 2))); 
    }

    @Test
    public void testZero() {
        NavigableMatrix<Integer> navMatrix = NavigableMatrix.constant(2, 2, 5, 0);
        assertEquals(Integer.valueOf(0), navMatrix.zero());
    }

    @Test
    public void testRepresentation() {
        NavigableMap<Indexes, String> matrix = new TreeMap<>();
        matrix.put(new Indexes(0, 0), "A");
        matrix.put(new Indexes(1, 1), "B");
        NavigableMatrix<String> navMatrix = NavigableMatrix.from(matrix, "");

        NavigableMap<Indexes, String> representation = navMatrix.representation();
        assertEquals(2, representation.size());
        assertEquals("A", representation.get(new Indexes(0, 0)));
        assertEquals("B", representation.get(new Indexes(1, 1)));
    }

    @Test
    public void testInstanceMethod() {
        Integer[][] intMatrix = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };
        NavigableMatrix<Integer> intNavigableMatrix = NavigableMatrix.from(intMatrix, 0);
        assertEquals(1, (int) intNavigableMatrix.value(new Indexes(0, 0)));
        assertEquals(5, (int) intNavigableMatrix.value(new Indexes(1, 1)));
        assertEquals(9, (int) intNavigableMatrix.value(new Indexes(2, 2)));

        // Test instance method with string matrix
        String[][] stringMatrix = {
            {"a", "b", "c"},
            {"d", "e", "f"},
            {"g", "h", "i"}
        };
        NavigableMatrix<String> stringNavigableMatrix = NavigableMatrix.from(stringMatrix, "");
        assertEquals("a", stringNavigableMatrix.value(new Indexes(0, 0)));
        assertEquals("e", stringNavigableMatrix.value(new Indexes(1, 1)));
        assertEquals("i", stringNavigableMatrix.value(new Indexes(2, 2)));
    }

    @Test
    public void testConstantMethod() {
        // Test constant method
        NavigableMatrix<Integer> constantMatrix = NavigableMatrix.constant(3, 3, 5, 0);
        assertEquals(5, (int) constantMatrix.value(new Indexes(0, 0)));
        assertEquals(5, (int) constantMatrix.value(new Indexes(2, 2)));
    }

    @Test
    public void testIdentityMethod() {
        NavigableMatrix<Integer> identityMatrix = NavigableMatrix.identity(3, 0, 1);
        assertEquals(1, (int) identityMatrix.value(new Indexes(0, 0)));
        assertEquals(1, (int) identityMatrix.value(new Indexes(1, 1)));
        assertEquals(1, (int) identityMatrix.value(new Indexes(2, 2)));
        assertEquals(0, (int) identityMatrix.value(new Indexes(0, 1)));
    }

    @Test
    public void testFromArray() {
        String[][] array = {{"A", "B"}, {"C", "D"}};
        NavigableMatrix<String> navMatrix = NavigableMatrix.from(array, "");

        assertEquals("A", navMatrix.value(new Indexes(0, 0)));
        assertEquals("B", navMatrix.value(new Indexes(0, 1)));
        assertEquals("C", navMatrix.value(new Indexes(1, 0)));
        assertEquals("D", navMatrix.value(new Indexes(1, 1)));
    }

    @Test
    public void testFromNavigableMap() {
        // Create a sample NavigableMap
        NavigableMap<Indexes, String> map = new TreeMap<>();
        map.put(new Indexes(0, 0), "A");
        map.put(new Indexes(1, 1), "B");
        NavigableMatrix<String> navMatrix = NavigableMatrix.from(map, "");

        // Test from method with NavigableMap
        assertEquals("A", navMatrix.value(new Indexes(0, 0)));
        assertEquals("B", navMatrix.value(new Indexes(1, 1)));
    }

    @Test
    public void testMerge1() {
        NavigableMap<Indexes, Integer> matrix1 = new TreeMap<>();
        matrix1.put(new Indexes(0, 0), 1);
        matrix1.put(new Indexes(1, 1), 2);
        matrix1.put(new Indexes(2, 2), 3);
        matrix1.put(new Indexes(1, 2), 3);
        NavigableMatrix<Integer> navigableMatrix1 = NavigableMatrix.from(matrix1, 0);

        NavigableMap<Indexes, Integer> matrix2 = new TreeMap<>();
        matrix2.put(new Indexes(0, 0), 4);
        matrix2.put(new Indexes(1, 1), 5);
        NavigableMatrix<Integer> navigableMatrix2 = NavigableMatrix.from(matrix2, 0);

        NavigableMatrix<Integer> mergedMatrix = navigableMatrix1.merge(navigableMatrix2, Integer::sum);

        NavigableMap<Indexes, Integer> expectedMatrix = new TreeMap<>();
        expectedMatrix.put(new Indexes(0, 0), 5);
        expectedMatrix.put(new Indexes(1, 1), 7);
        expectedMatrix.put(new Indexes(2, 2), 3);    
        expectedMatrix.put(new Indexes(1, 2), 3);

        assertEquals(expectedMatrix, mergedMatrix.representation());
    }
}
