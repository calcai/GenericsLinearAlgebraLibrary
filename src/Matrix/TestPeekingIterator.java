package Matrix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import org.junit.Test;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class TestPeekingIterator {
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
}
