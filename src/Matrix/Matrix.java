package Matrix;

import java.util.Map;
import java.util.NavigableMap;
import java.util.function.BinaryOperator;

public interface Matrix<I, T> {

    T value(I index);

    T zero();
    
    NavigableMap<I, T> representation();

    PeekingIterator<Map.Entry<I,T>> peekingIterator();

    Matrix<I,T> merge(Matrix<I, T> other, BinaryOperator<T> op);

}
