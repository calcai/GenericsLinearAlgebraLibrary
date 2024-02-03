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

    // AbstractMatrix Tests
    @Test
    public void testAbstractMatrix() {
        NavigableMap<Integer, String> test = new TreeMap<>();
        test.put(0, "A");
        test.put(1, "B");

        AbstractMatrix<Integer, String> matrix = new AbstractMatrix<>(test, "0") {};

        assertEquals(test, matrix.representation());
        assertEquals("A", matrix.value(0));
        assertEquals("B", matrix.value(1));
        assertEquals("0", matrix.zero());
    }


    @Test(expected = IllegalArgumentException.class)
    public void testNullValue() {
        AbstractMatrix<Integer, String> matrix = new AbstractMatrix<>(new TreeMap<>(), "0") {};
        matrix.value(null);
    }

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
        // Arrange
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

        int result = index1.compareByRow(index2);

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


}

