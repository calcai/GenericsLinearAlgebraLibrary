package Matrix;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestMatrix {

    // NavigableVector Tests
    @Test(expected = NullPointerException.class)
    public void testNullFrom() {
        NavigableVector.from((Map<Integer, String>) null, "0");
    }

    @Test
    public void testFrom() {
        Map<Integer, String> test = new TreeMap<>();
        test.put(0, "A");
        test.put(1, "B");
        test.put(2, "0");

        NavigableVector<String> vector = NavigableVector.from(test, "0");

        assertEquals("A", vector.value(0));
        assertEquals("B", vector.value(1));
        assertNull(vector.value(2));
        assertEquals("0", vector.zero());
    }

    //Indexes tests
    @Test
    public void testWithRowAndWithColumn() {
        Indexes index = new Indexes(2, 3);

        Indexes index1 = index.withRow(7);

        assertEquals(Integer.valueOf(7), index1.row());
        assertEquals(index.column(), index1.column());

        Indexes index2 = index.withColumn(9);

        assertEquals(index.row(), index2.row());
        assertEquals(9, index2.column().intValue());
    }

    @Test
    public void testCompareByRow() {
        Indexes index1 = new Indexes(4, 0);
        Indexes index2 = new Indexes(5, 9);

        int result = index1.compareTo(index2);

        assertTrue(result < 0); 
    }

    @Test
    public void testIndexesValue() {
        String[][] matrix = {{"a", "b", "b"}, {"d", "e", "f"}, {"g", "h", "i"}};
        Indexes index = new Indexes(2, 2);

        String value = index.value(matrix);

        assertEquals("i", value);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndexesValueWithInvalidInput() {
        String[][] matrix = {{"A", "B", "C"}, {"D", "E", "F"}};
        Indexes index = new Indexes(3, 3);

        index.value(matrix);
    }

    @Test
    public void testDiagonal() {
        Indexes diagonalIndex = new Indexes(1, 1);
        Indexes nonDiagonalIndex = new Indexes(1, 7);

        assertTrue(diagonalIndex.areDiagonal());
        assertFalse(nonDiagonalIndex.areDiagonal());
    }

    @Test
    public void testIndexesStream() {
        Indexes from = new Indexes(0, 1);
        Indexes to = new Indexes(2, 3);

        Stream<Indexes> stream = Indexes.stream(from, to);
        List<Indexes> result = stream.collect(Collectors.toList());

        assertEquals(List.of(new Indexes(0, 1), new Indexes(0, 2), new Indexes(1, 1), new Indexes(1, 2)), result);
    }
    @Test
    public void testIndexesStreamWithSize() {
        Indexes size = new Indexes(2, 3);

        Stream<Indexes> stream = Indexes.stream(size);
        var result = stream.collect(Collectors.toList());
        assertEquals(Arrays.asList(new Indexes(0, 0), new Indexes(0, 1), new Indexes(0, 2), new Indexes(1, 0), new Indexes(1, 1), new Indexes(1, 2)), result);
    }

    @Test
    public void testIndexesStreamWithRowsAndColumns() {
        Stream<Indexes> stream = Indexes.stream(2, 3);
        var result = stream.collect(Collectors.toList());

        assertEquals(Arrays.asList(new Indexes(0, 0), new Indexes(0, 1), new Indexes(0, 2),
                new Indexes(1, 0), new Indexes(1, 1), new Indexes(1, 2)), result);
    }

    @Test
    public void testValue(){
        NavigableMap<Indexes, Integer> matrix = new TreeMap<>();
        matrix.put(new Indexes(0, 0), 1);
        matrix.put(new Indexes(1, 1), 2);
        NavigableMatrix<Integer> navMatrix = NavigableMatrix.from(matrix, 0);
        Indexes index = new Indexes(0, 0);

        int value = index.value(navMatrix);
        assertEquals(1, value);
    }
    @Test
    public void testMerge() {
        NavigableMap<Indexes, Integer> matrix1 = new TreeMap<>();
        matrix1.put(new Indexes(0, 0), 1);
        matrix1.put(new Indexes(1, 1), 2);
        matrix1.put(new Indexes(2, 2), 3);
        NavigableMatrix<Integer> navigableMatrix1 = NavigableMatrix.from(matrix1, 0);

        NavigableMap<Indexes, Integer> matrix2 = new TreeMap<>();
        matrix2.put(new Indexes(0, 0), 4);
        matrix2.put(new Indexes(1, 1), 5);
        matrix2.put(new Indexes(2, 2), 6);
        NavigableMatrix<Integer> navigableMatrix2 = NavigableMatrix.from(matrix2, 0);

        NavigableMatrix<Integer> mergedMatrix = navigableMatrix1.merge(navigableMatrix2, Integer::sum);

        NavigableMap<Indexes, Integer> expectedMatrix = new TreeMap<>();
        expectedMatrix.put(new Indexes(0, 0), 5);
        expectedMatrix.put(new Indexes(1, 1), 7);
        expectedMatrix.put(new Indexes(2, 2), 9);

        assertEquals(expectedMatrix, mergedMatrix.representation());
    }

}

