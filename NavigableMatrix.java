package JavaGenericsMatrix;

import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.BinaryOperator;
import java.util.function.Function;

//return unmodifiable maps

public class NavigableMatrix <T> implements Matrix<Indexes, T> {

    private final NavigableMap<Indexes, T> matrix;

    private final T zero;

    private NavigableMatrix(NavigableMap<Indexes, T> matrix, T zero){
        this.matrix = matrix;
        this.zero = zero;
    }

    @Override
    public T value(Indexes index) {
        return matrix.getOrDefault(index, zero);
    }

    @Override
    public T zero() {
        return this.zero;
    }

    @Override
    public NavigableMap<Indexes, T> representation() {
        return new TreeMap<>(this.matrix);
    }

    public static<S> NavigableMatrix<S> instance(int rows, int columns, Function<Indexes, S> valueMapper, S zero){
        InvalidLengthException.requireNonEmpty(InvalidLengthException.Cause.ROW, rows);
        InvalidLengthException.requireNonEmpty(InvalidLengthException.Cause.COLUMN, columns);
        Objects.requireNonNull(valueMapper);
        
        NavigableMap<Indexes, S> matrix = new TreeMap<>();
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                Indexes index = new Indexes(i, j);
                S value = valueMapper.apply(index);
                if(!value.equals(zero)){
                    matrix.put(index, value);
                }
            }
        }
        return new NavigableMatrix<S>(matrix, zero);
    }

    public static <S> NavigableMatrix<S> constant(int rows, int columns, S value, S zero){
        InvalidLengthException.requireNonEmpty(InvalidLengthException.Cause.ROW, rows);
        InvalidLengthException.requireNonEmpty(InvalidLengthException.Cause.COLUMN, columns);

        Objects.requireNonNull(value);
        Function<Indexes, S> valueMapper = indexes ->{
            return value;
        };

        return instance(rows, columns, valueMapper, zero);
    }

    public static <S> NavigableMatrix<S> identity(int size, S zero, S identity){
        InvalidLengthException.requireNonEmpty(InvalidLengthException.Cause.ROW, size);
        Function<Indexes, S> valueMapper = indexes -> {
            if (indexes.row() == indexes.column()) {
                return identity;
            } else {
                return zero;
            }
        };
        return instance(size, size, valueMapper, zero);
    }

    public static <S> NavigableMatrix<S> from(NavigableMap<Indexes, S> matrix, S zero){
        //remove zeroes here
        return new NavigableMatrix<S>(matrix, zero);
    }

    public static <S> NavigableMatrix<S> from(S[][] matrix, S zero){
        Objects.requireNonNull(matrix);
        Function<Indexes, S> valueMapper = indexes -> {
            return matrix[indexes.row()][indexes.column()];
        };
    
        return instance(matrix.length, matrix[0].length, valueMapper, zero);
    }

    @Override
    public PeekingIterator<Map.Entry<Indexes, T>> peekingIterator(){
        return PeekingIterator.from(this.representation().entrySet().iterator());
    }
    
    @Override
    public NavigableMatrix<T> merge(Matrix<Indexes, T> other, BinaryOperator<T> op) {
        InconsistentZeroException.requireMatching(this, other);
        return NavigableMatrix.from(MapMerger.merge(this.peekingIterator(), other.peekingIterator(), Indexes.byrow, op, Indexes.ORIGIN, zero), zero);
    }

    public static class InvalidLengthException extends Exception{

        private static final long serialVersionUID = 012L;

        private Cause cause;
        private int length;

        enum Cause {
            ROW, COLUMN;
        }

        public InvalidLengthException (Cause cause, int length){
            this.cause = cause;
            this.length = length;
        }

        public Cause getCauseCategory(){
            return this.cause;
        }
        
        public int getLength(){
            return this.length;
        }

        public static int requireNonEmpty(Cause cause, int length){
            if (length <= 0){
                throw new IllegalArgumentException(new InvalidLengthException(cause, length));
            }
            return length;
        }

         
    }

    
}
