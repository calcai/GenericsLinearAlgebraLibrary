package Matrix;

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
        AbstractMatrix<Indexes, Integer> navMatrix = NavigableMatrix.from(matrix, 0);

        assertEquals(Integer.valueOf(1), navMatrix.value(new Indexes(0, 0)));
        assertEquals(Integer.valueOf(2), navMatrix.value(new Indexes(1, 1)));
        assertEquals(Integer.valueOf(0), navMatrix.value(new Indexes(2, 2))); 
    }

    @Test
    public void testZero() {
        AbstractMatrix<Indexes, Integer> navMatrix = NavigableMatrix.constant(2, 2, 5, 0);
        assertEquals(Integer.valueOf(0), navMatrix.zero());
    }

    @Test
    public void testRepresentation() {
        NavigableMap<Indexes, String> matrix = new TreeMap<>();
        matrix.put(new Indexes(0, 0), "A");
        matrix.put(new Indexes(1, 1), "B");
        AbstractMatrix<Indexes, String> navMatrix = NavigableMatrix.from(matrix, "");

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
        AbstractMatrix<Indexes, Integer> intNavigableMatrix = NavigableMatrix.from(intMatrix, 0);
        assertEquals(1, (int) intNavigableMatrix.value(new Indexes(0, 0)));
        assertEquals(5, (int) intNavigableMatrix.value(new Indexes(1, 1)));
        assertEquals(9, (int) intNavigableMatrix.value(new Indexes(2, 2)));

        // Test instance method with string matrix
        String[][] stringMatrix = {
            {"a", "b", "c"},
            {"d", "e", "f"},
            {"g", "h", "i"}
        };
        AbstractMatrix<Indexes, String>stringNavigableMatrix = NavigableMatrix.from(stringMatrix, "");
        assertEquals("a", stringNavigableMatrix.value(new Indexes(0, 0)));
        assertEquals("e", stringNavigableMatrix.value(new Indexes(1, 1)));
        assertEquals("i", stringNavigableMatrix.value(new Indexes(2, 2)));
    }

    @Test
    public void testConstantMethod() {
        // Test constant method
        AbstractMatrix<Indexes, Integer> constantMatrix = NavigableMatrix.constant(3, 3, 5, 0);
        assertEquals(5, (int) constantMatrix.value(new Indexes(0, 0)));
        assertEquals(5, (int) constantMatrix.value(new Indexes(2, 2)));
    }

    @Test
    public void testIdentityMethod() {
        AbstractMatrix<Indexes, Integer> identityMatrix = NavigableMatrix.identity(3, 0, 1);
        assertEquals(1, (int) identityMatrix.value(new Indexes(0, 0)));
        assertEquals(1, (int) identityMatrix.value(new Indexes(1, 1)));
        assertEquals(1, (int) identityMatrix.value(new Indexes(2, 2)));
        assertEquals(0, (int) identityMatrix.value(new Indexes(0, 1)));
    }

    @Test
    public void testFromArray() {
        String[][] array = {{"A", "B"}, {"C", "D"}};
        AbstractMatrix<Indexes, String> navMatrix = NavigableMatrix.from(array, "");

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
        AbstractMatrix<Indexes, String> navMatrix = NavigableMatrix.from(map, "");

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
        AbstractMatrix<Indexes, Integer>navigableMatrix1 = NavigableMatrix.from(matrix1, 0);

        NavigableMap<Indexes, Integer> matrix2 = new TreeMap<>();
        matrix2.put(new Indexes(0, 0), 4);
        matrix2.put(new Indexes(1, 1), 5);
        AbstractMatrix<Indexes, Integer>navigableMatrix2 = NavigableMatrix.from(matrix2, 0);

        Matrix<Indexes, Integer> mergedMatrix = navigableMatrix1.merge(navigableMatrix2, Integer::sum);

        NavigableMap<Indexes, Integer> expectedMatrix = new TreeMap<>();
        expectedMatrix.put(new Indexes(0, 0), 5);
        expectedMatrix.put(new Indexes(1, 1), 7);
        expectedMatrix.put(new Indexes(2, 2), 3);    
        expectedMatrix.put(new Indexes(1, 2), 3);

        assertEquals(expectedMatrix, mergedMatrix.representation());
    }

    @Test
    public void testRowAndColumn() {
        NavigableMap<Indexes, Integer> m = new TreeMap<>();
        m.put(new Indexes(0, 0), 1);
        m.put(new Indexes(0, 1), 2);
        m.put(new Indexes(0, 2), 3);
        m.put(new Indexes(1, 0), 4);
        m.put(new Indexes(1, 1), 5);
        m.put(new Indexes(1, 2), 6);

        AbstractMatrix<Indexes, Integer> matrix = NavigableMatrix.from(m, 0);

        NavigableVector<Integer> row0 = matrix.row(0);
        NavigableVector<Integer> row1 = matrix.row(1);
        NavigableVector<Integer> col0 = matrix.column(0);
        NavigableVector<Integer> col1 = matrix.column(1);
        NavigableVector<Integer> col2 = matrix.column(2);

        assertEquals(1, row0.value(0).intValue());
        assertEquals(2, row0.value(1).intValue());
        assertEquals(3, row0.value(2).intValue());
        assertEquals(4, row1.value(0).intValue());
        assertEquals(5, row1.value(1).intValue());
        assertEquals(6, row1.value(2).intValue());
        assertEquals(1, col0.value(0).intValue());
        assertEquals(4, col0.value(1).intValue());
        assertEquals(2, col1.value(0).intValue());
        assertEquals(5, col1.value(1).intValue());
        assertEquals(3, col2.value(0).intValue());
        assertEquals(6, col2.value(1).intValue());
    }

    @Test
    public void testTranspose() {
        AbstractMatrix<Indexes, Integer>  matrix = NavigableMatrix.instance(3, 2, index -> index.row() + index.column(), 0);
        AbstractMatrix<Indexes, Integer> transposedMatrix = matrix.transpose();


        assertEquals(Integer.valueOf(1), transposedMatrix.value(new Indexes(0, 1)));
        assertEquals(Integer.valueOf(1), transposedMatrix.value(new Indexes(1, 0)));
        assertEquals(Integer.valueOf(2), transposedMatrix.value(new Indexes(1, 1)));
    }


    @Test
    public void testMultiply() {
        // Create two matrices for multiplication
        AbstractMatrix<Indexes, Integer>  matrix1 = NavigableMatrix.instance(2, 3, index -> index.row() + index.column(), 0);
        AbstractMatrix<Indexes, Integer>  matrix2 = NavigableMatrix.instance(3, 2, index -> index.row() + index.column(), 0);

        AbstractMatrix<Indexes, Integer> result = matrix1.multiply(matrix2, BinaryOperations::add, BinaryOperations::multiply);

        // Test the values of the result matrix
        assertEquals(Integer.valueOf(5), result.value(new Indexes(0, 0)));
        assertEquals(Integer.valueOf(8), result.value(new Indexes(0, 1)));
        assertEquals(Integer.valueOf(8), result.value(new Indexes(1, 0)));
        assertEquals(Integer.valueOf(14), result.value(new Indexes(1, 1)));

    }
    @Test
    public void testEntrywise() {
        AbstractMatrix<Indexes, Integer> matrix1 = NavigableMatrix.instance(2, 3, index -> index.row() + index.column(), 0);
        AbstractMatrix<Indexes, Integer> matrix2 = NavigableMatrix.instance(2, 3, index -> index.row() + index.column(), 0);

        AbstractMatrix<Indexes, Integer> result = matrix1.entryWiseMultiplication(matrix2, BinaryOperations::multiply);

        assertEquals(Integer.valueOf(0), result.value(new Indexes(0, 0)));
        assertEquals(Integer.valueOf(1), result.value(new Indexes(0, 1)));
        assertEquals(Integer.valueOf(4), result.value(new Indexes(0, 2)));
        assertEquals(Integer.valueOf(1), result.value(new Indexes(1, 0)));
        assertEquals(Integer.valueOf(4), result.value(new Indexes(1, 1)));
        assertEquals(Integer.valueOf(9), result.value(new Indexes(1, 2)));
    }

}
