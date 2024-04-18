package Matrix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import org.junit.Test;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class PeekingIteratorTest {
    @Test
    public void testNextPeekElement() {
        Iterator<Integer> iterator = Arrays.asList(2, 3).iterator();
        PeekingIterator<Integer> peekingIterator = PeekingIterator.from(iterator);
        assertTrue(peekingIterator.hasNext());
        assertEquals(Integer.valueOf(2), peekingIterator.next());
        assertEquals(Integer.valueOf(3), peekingIterator.element());
        assertEquals(Optional.of(3), peekingIterator.peek());
        peekingIterator.next();
        assertFalse(peekingIterator.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNextNoSuchElementException() {
        List<Integer> list = Arrays.asList(1);
        Iterator<Integer> iterator = list.iterator();
        PeekingIterator<Integer> peekingIterator = PeekingIterator.from(iterator);
        
        peekingIterator.next();
        peekingIterator.next();
    }

    @Test
    public void testHasNext() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Iterator<Integer> iterator = list.iterator();
        PeekingIterator<Integer> peekingIterator = PeekingIterator.from(iterator);
        
        assertTrue(peekingIterator.hasNext());
        peekingIterator.next();
        assertTrue(peekingIterator.hasNext());
        peekingIterator.next();
        assertTrue(peekingIterator.hasNext());
        peekingIterator.next();
        assertFalse(peekingIterator.hasNext());
    }

    @Test
    public void testPeek() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Iterator<Integer> iterator = list.iterator();
        PeekingIterator<Integer> peekingIterator = PeekingIterator.from(iterator);
        
        assertEquals(Optional.of(1), peekingIterator.peek());
        peekingIterator.next();
        assertEquals(Optional.of(2), peekingIterator.peek());
        peekingIterator.next();
        assertEquals(Optional.of(3), peekingIterator.peek());
        peekingIterator.next();
        assertEquals(Optional.empty(), peekingIterator.peek());
    }

    @Test
    public void testFromIterable() {
        Iterable<Integer> iterable = Arrays.asList(4, 5, 6);
        PeekingIterator<Integer> peekingIterator = PeekingIterator.from(iterable);
        assertTrue(peekingIterator.hasNext());
        assertEquals(Integer.valueOf(4), peekingIterator.next());
        assertEquals(Optional.of(5), peekingIterator.peek());
        assertEquals(Integer.valueOf(5), peekingIterator.element());
        assertTrue(peekingIterator.hasNext());
        assertEquals(Integer.valueOf(5), peekingIterator.next());
    }

    @Test
    public void testElementNoSuchElementException() {
        List<Integer> list = Arrays.asList(1);
        Iterator<Integer> iterator = list.iterator();
        PeekingIterator<Integer> peekingIterator = PeekingIterator.from(iterator);
        
        peekingIterator.next();
        assertThrows(NoSuchElementException.class, peekingIterator::element);
    }
}
