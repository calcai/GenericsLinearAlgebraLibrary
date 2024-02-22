package JavaGenericsMatrix;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

public class NavigableMatrix <T> implements Matrix<Indexes, T> {

    public final NavigableMap<Indexes, T> matrixByRows;

    public final NavigableMap<Indexes, T> matrixByColumns;

    private final T zero;

    private NavigableMatrix(NavigableMap<Indexes, T> matrixByRows, NavigableMap<Indexes, T> matrixByColumns, T zero){
        this.matrixByRows = Collections.unmodifiableNavigableMap(matrixByRows);
        this.matrixByColumns = Collections.unmodifiableNavigableMap(matrixByColumns);
        this.zero = zero;
    }

    @Override
    public T value(Indexes index) {
        return matrixByRows.getOrDefault(index, zero);
    }

    @Override
    public T zero() {
        return this.zero;
    }

    @Override
    public NavigableMap<Indexes, T> representation() {
        return new TreeMap<>(this.matrixByRows);
    }

    public static<S> NavigableMatrix<S> instance(int rows, int columns, Function<Indexes, S> valueMapper, S zero){
        InvalidLengthException.requireNonEmpty(InvalidLengthException.Cause.ROW, rows);
        InvalidLengthException.requireNonEmpty(InvalidLengthException.Cause.COLUMN, columns);
        Objects.requireNonNull(valueMapper);
        
        NavigableMap<Indexes, S> matrixByRows = new TreeMap<>();
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                Indexes index = new Indexes(i, j);
                S value = valueMapper.apply(index);
                if(!value.equals(zero)){
                    matrixByRows.put(index, value);
                }
            }
        }
        NavigableMap<Indexes, S> matrixByColumns = reorderByColumn(matrixByRows);
        return new NavigableMatrix<S>(matrixByRows, matrixByColumns, zero);
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
        Objects.requireNonNull(identity);
        InvalidLengthException.requireNonEmpty(InvalidLengthException.Cause.ROW, size);
        Function<Indexes, S> valueMapper = indexes -> {
            if (indexes.areDiagonal()) {
                return identity;
            } else {
                return zero;
            }
        };
        return instance(size, size, valueMapper, zero);
    }


    public static <S> NavigableMatrix<S> from(NavigableMap<Indexes, S> matrix, S zero){
        matrix.values().removeIf(value -> value.equals(zero));
        NavigableMap<Indexes, S> rowMatrix = matrix;
        NavigableMap<Indexes, S> columnMatrix = reorderByColumn(matrix);
        return new NavigableMatrix<S>(rowMatrix, columnMatrix, zero);
    }

    //Helper method to convert matrixByRows to matrixByColumns, using priority queue
    public static <S> NavigableMap<Indexes, S> reorderByColumn(NavigableMap<Indexes, S> matrixByRows) {
        NavigableMap<Indexes, S> matrixByColumns = new TreeMap<>();
        PriorityQueue<Map.Entry<Indexes, S>> pq = new PriorityQueue<>(Comparator.comparing(entry -> entry.getKey().column()));
        pq.addAll(matrixByRows.entrySet());
        while (!pq.isEmpty()) {
            Map.Entry<Indexes, S> entry = pq.poll();
            matrixByColumns.put(entry.getKey(), entry.getValue()); 
        }
        return matrixByColumns;
    }
    
    
    public static <S> NavigableMatrix<S> from(S[][] matrix, S zero){
        Objects.requireNonNull(matrix);
        Function<Indexes, S> valueMapper = indexes -> {
            return matrix[indexes.row()][indexes.column()];
        };
    
        return instance(matrix.length, matrix[0].length, valueMapper, zero);
    }

    public NavigableVector<T> row(int i) {
        Map<Integer, T> filteredMap = matrixByRows.entrySet()
                .stream()
                .filter(entry -> entry.getKey().row() == i) 
                .collect(Collectors.toMap(entry -> entry.getKey().column(), Map.Entry::getValue, (a, b) -> a, TreeMap::new)); 
        return NavigableVector.from(filteredMap, zero); 
    }
    
    public NavigableVector<T> column(int i){
        Map<Integer, T> filteredMap = matrixByColumns.entrySet()
                .stream()
                .filter(entry -> entry.getKey().column() == i) 
                .collect(Collectors.toMap(entry -> entry.getKey().row(), Map.Entry::getValue, (a, b) -> a, TreeMap::new)); 
        System.out.println(filteredMap);
        return NavigableVector.from(filteredMap, zero); 
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

        private final Cause cause;
        private final int length;

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
