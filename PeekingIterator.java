package JavaGenericsMatrix;

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

    private Optional<T> getNext() {
        return it.hasNext() ? Optional.of(it.next()) : Optional.empty();
    }

    public static<T> PeekingIterator<T> from(Iterator<T> iterator){
        Objects.requireNonNull(iterator);
        return new PeekingIterator<T>(iterator);
    }

    public static<T> PeekingIterator<T> from(Iterable<T> iterable){
        Objects.requireNonNull(iterable);
        return new PeekingIterator<T>(iterable.iterator());
    }

    @Override
    public boolean hasNext() {
        return next.isPresent();
    }

    @Override
    public T next() {
        if (next.isEmpty()) {
            throw new NoSuchElementException();
        }
        T current = next.get();
        next = getNext();
        return current;
    }

    public Optional<T> peek(){
        return next;
    }

    public T element(){
        if (next.isEmpty()) {
            throw new NoSuchElementException();
        }
        return next.get();
    }    
}
