package JavaGenericsMatrix;

import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.BinaryOperator;

public class NavigableVector<T> extends AbstractMatrix<Integer, T> {

    private NavigableVector(NavigableMap<Integer,T> matrix, T zero){
        super(matrix, zero);
    }
    
    public static <S> NavigableVector<S> from(Map<Integer, S> vector, S zero) {

        Objects.requireNonNull(vector);
        Objects.requireNonNull(zero);

        NavigableMap<Integer, S> navigableMap = new TreeMap<>(vector);
        
        navigableMap.values().removeIf(value -> value.equals(zero));

        return new NavigableVector<S>(navigableMap, zero);
    }

    public PeekingIterator<Map.Entry<Integer,T>> peekingIterator(){
        return PeekingIterator.from(this.representation().entrySet().iterator());
    }

    @Override
    public NavigableVector<T> merge(Matrix<Integer, T> other, BinaryOperator<T> op) {
        InconsistentZeroException.requireMatching(this, other);
        return NavigableVector.from(MapMerger.merge(this.peekingIterator(), other.peekingIterator(),Comparator.naturalOrder(), op, 0, this.zero()), this.zero());
    }
}
