package Matrix;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

public final class PeekingIterator<T> implements Iterator<T> {

    private final Iterator<T> it;
    private Optional<T> next;

    private PeekingIterator(Iterator<T> iterator){
        this.it = iterator;
        this.next = getNext();
    }

    
    /** 
     * Returns and advances the iterator if there is a next element
     * 
     * @return Optional<T>
     */
    private Optional<T> getNext() {
        return it.hasNext() ? Optional.of(it.next()) : Optional.empty();
    }

    
    /** 
     * Creates a PeekingIterator from any iterator
     * 
     * @param iterator
     * @return PeekingIterator<T>
     */
    public static<T> PeekingIterator<T> from(Iterator<T> iterator){
        Objects.requireNonNull(iterator);
        return new PeekingIterator<T>(iterator);
    }

    
    /** 
     * Creates a PeekingIterator from any iterable
     * 
     * @param iterable
     * @return PeekingIterator<T>
     */
    public static<T> PeekingIterator<T> from(Iterable<T> iterable){
        Objects.requireNonNull(iterable);
        return new PeekingIterator<T>(iterable.iterator());
    }

    
    /** 
     * Boolean that indicates whether the end of the PeekingIterator has been reached
     * 
     * @return boolean
     */
    @Override
    public boolean hasNext() {
        return next.isPresent();
    }

    
    /** 
     * Returns the next element and advances the iterator 
     * 
     * @return T
     */
    @Override
    public T next() {
        if (next.isEmpty()) {
            throw new NoSuchElementException();
        }
        T current = next.get();
        next = getNext();
        return current;
    }

    
    /** 
     * Returns the next element if it exists
     * 
     * @return Optional<T>
     */
    public Optional<T> peek(){
        return next;
    }

    
    /** 
     * Returns the next element or throws an excpetion
     * 
     * @return T
     */
    public T element(){
        if (next.isEmpty()) {
            throw new NoSuchElementException();
        }
        return next.get();
    }    
}
