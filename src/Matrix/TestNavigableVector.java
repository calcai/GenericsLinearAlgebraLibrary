package Matrix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;


import java.util.Map;

import org.junit.Test;

public class TestNavigableVector {


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
    public void testMerge1(){ 
        NavigableVector<Integer> vector1 = NavigableVector.from(Map.of(1, 1, 2, 2, 3, 3), 0);
        NavigableVector<Integer> vector2 = NavigableVector.from(Map.of(1, 2, 2, 2, 3, 3, 4, 4), 0);

        NavigableVector<Integer> mergedVector = vector1.merge(vector2, Integer::sum);

        NavigableVector<Integer> expectedVector = NavigableVector.from(Map.of(1, 3, 2, 4, 3, 6, 4, 4), 0);

        assertEquals(expectedVector.representation(), mergedVector.representation());
    }

    @Test
    public void testEntryWiseMultiplication(){
        NavigableVector<Integer> vector1 = NavigableVector.from(Map.of(1, 1, 2, 2, 3, 3), 0);
        NavigableVector<Integer> vector2 = NavigableVector.from(Map.of(1, 2, 3, 3), 0);
        NavigableVector<Integer> productVector = vector1.entryWiseMultiplication(vector2, BinaryOperations::multiply);
        NavigableVector<Integer> expectedVector = NavigableVector.from(Map.of(1, 2, 3, 9), 0);
        assertEquals(expectedVector.representation(), productVector.representation());
    }

    @Test
    public void testMultiply(){
        NavigableVector<Integer> vector1 = NavigableVector.from(Map.of(1, 1, 2, 2, 3, 3), 0);
        NavigableVector<Integer> vector2 = NavigableVector.from(Map.of(1, 2, 3, 3), 0).transpose();
        Integer product = vector1.multiply(vector2, BinaryOperations::multiply, BinaryOperations::add).value(0);
        assertEquals(Integer.valueOf(11), product);
    }
}
