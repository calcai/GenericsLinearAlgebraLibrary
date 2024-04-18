package Matrix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.Map;

import org.junit.Test;

public class NavigableVectorTest {

    @Test
    public void testPeekingIterator() {
        NavigableVector<Integer> vector = NavigableVector.from(Map.of(1, 1, 2, 0, 3, 3), 0);

        PeekingIterator<Map.Entry<Integer, Integer>> peekingIterator = vector.peekingIterator();

        assertTrue("Peeking iterator should have next element", peekingIterator.hasNext());
        peekingIterator.next();
        assertTrue(peekingIterator.hasNext());
        peekingIterator.next();
        assertFalse(peekingIterator.hasNext());
    }

    @Test
    public void testMerge() {
        NavigableVector<Integer> vector1 = NavigableVector.from(Map.of(1, 1, 2, 2, 3, 3), 0);
        NavigableVector<Integer> vector2 = NavigableVector.from(Map.of(1, 2, 2, 2, 3, 3, 4, 4), 0);

        NavigableVector<Integer> mergedVector = vector1.merge(vector2, Integer::sum);

        NavigableVector<Integer> expectedVector = NavigableVector.from(Map.of(1, 3, 2, 4, 3, 6, 4, 4), 0);

        assertEquals(expectedVector.representation(), mergedVector.representation());
    }

    @Test
    public void testEntryWiseMultiplication() {
        NavigableVector<Integer> vector1 = NavigableVector.from(Map.of(1, 1, 2, 2, 3, 3), 0);
        NavigableVector<Integer> vector2 = NavigableVector.from(Map.of(1, 2, 3, 3), 0);
        NavigableVector<Integer> productVector = vector1.entryWiseMultiplication(vector2, BinaryOperations::multiply);
        NavigableVector<Integer> expectedVector = NavigableVector.from(Map.of(1, 2, 3, 9), 0);
        assertEquals(expectedVector.representation(), productVector.representation());
    }

    @Test
    public void testMultiply() {
        NavigableVector<Integer> vector1 = NavigableVector.from(Map.of(1, 1, 2, 2, 3, 3), 0);
        NavigableVector<Integer> vector2 = NavigableVector.from(Map.of(1, 2, 2, 3, 3, 4), 0).transpose();
        Integer product = vector1.multiply(vector2, BinaryOperations::multiply, BinaryOperations::add).value(0);
        assertEquals(Integer.valueOf(20), product);
    }

    @Test
    public void testTranspose() {
        NavigableVector<Integer> vector = NavigableVector.from(Map.of(1, 1, 2, 2, 3, 3), 0);

        NavigableVector<Integer> transposedVector = vector.transpose();

        NavigableVector<Integer> expectedVector = NavigableVector.from(Map.of(1, 1, 2, 2, 3, 3), 0);

        assertEquals(expectedVector.representation(), transposedVector.representation());
    }

    @Test
    public void testRow() {
        NavigableVector<Integer> vector = NavigableVector.from(Map.of(1, 1, 2, 2, 3, 3), 0);

        NavigableVector<Integer> rowVector = vector.row(1);

        NavigableVector<Integer> expectedVector = NavigableVector.from(Map.of(1, 1), 0);

        assertEquals(expectedVector.representation(), rowVector.representation());
    }

    @Test
    public void testRow2() {
        NavigableVector<Integer> vector = NavigableVector.from(Map.of(1, 1, 2, 2, 3, 3), 0);

        NavigableVector<Integer> rowVector = vector.row(1);

        NavigableVector<Integer> expectedVector = NavigableVector.from(Map.of(1, 1), 0);

        assertEquals(expectedVector.representation(), rowVector.representation());
    }

    @Test
    public void testColumn() {
        NavigableVector<Integer> vector = NavigableVector.from(Map.of(1, 1, 2, 2, 3, 3), 0);

        NavigableVector<Integer> columnVector = vector.column(1);

        NavigableVector<Integer> expectedVector = NavigableVector.from(Map.of(1, 1, 2, 1, 3, 1), 0);

        assertEquals(expectedVector.representation(), columnVector.representation());
    }

    @Test(expected = Exception.class)
    public void testInvalidSize(){
        NavigableVector<Integer> vector = NavigableVector.from(Map.of(1, 1, 2, 2, 3, 3), 0);
        vector.row(4);
    }

    @Test(expected = Exception.class)
    public void testInvalidSize2(){
        NavigableVector<Integer> vector = NavigableVector.from(Map.of(1, 1, 2, 2, 3, 3), 0);
        vector.column(4);
    }

    @Test(expected = Exception.class)
    public void testInvalidSize4(){
        NavigableVector<Integer> vector = NavigableVector.from(Map.of(1, 1, 2, 2, 3, 3), 0);
        vector.column(-1);
    }

    @Test(expected = Exception.class)
    public void testInvalidSize5(){
        NavigableVector<Integer> vector = NavigableVector.from(Map.of(1, 1, 2, 2, 3, 3), 0);
        vector.row(-1);
    }

    @Test(expected = Exception.class)
    public void testInvalidSize6(){
        NavigableVector<Integer> vector = NavigableVector.from(Map.of(1, 1, 2, 2, 3, 3), 0);
        vector.column(-1);
    }

}
