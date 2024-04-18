package Matrix;

import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

public class NavigableMatrix<T> extends AbstractMatrix<Indexes, T> {

    public final NavigableMap<Indexes, T> matrixByRows;

    private final T zero;


    private NavigableMatrix(NavigableMap<Indexes, T> matrixByRows, T zero, int rows, int columns){
        super(matrixByRows, zero, rows, columns);
        this.matrixByRows = matrixByRows;
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

    public static<S> AbstractMatrix<Indexes, S> instance(int rows, int columns, Function<Indexes, S> valueMapper, S zero){
        InvalidLengthException.requireNonEmpty(InvalidLengthException.Cause.ROW, rows);
        InvalidLengthException.requireNonEmpty(InvalidLengthException.Cause.COLUMN, columns);
        Objects.requireNonNull(valueMapper);
        
        NavigableMap<Indexes, S> matrixByRows = new TreeMap<>();
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                Indexes index = new Indexes(i, j);
                S value = valueMapper.apply(index);
                if(Objects.isNull(value) || !value.equals(zero)){
                    matrixByRows.put(index, value);
                }
            }
        }
        AbstractMatrix<Indexes, S> result = new NavigableMatrix<S>(matrixByRows, zero, rows, columns);
        return result;
    }

    public static <S> AbstractMatrix<Indexes, S> constant(int rows, int columns, S value, S zero){
        InvalidLengthException.requireNonEmpty(InvalidLengthException.Cause.ROW, rows);
        InvalidLengthException.requireNonEmpty(InvalidLengthException.Cause.COLUMN, columns);

        Objects.requireNonNull(value);
        Function<Indexes, S> valueMapper = indexes ->{
            return value;
        };

        return instance(rows, columns, valueMapper, zero);
    }

    public static <S> AbstractMatrix<Indexes, S> identity(int size, S zero, S identity){
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


    public static <S> AbstractMatrix<Indexes, S> from(NavigableMap<Indexes, S> matrix, S zero){
        int rows = matrix.keySet().stream()
                                .mapToInt(i -> i.row())
                                .max()
                                .getAsInt();
        int columns = matrix.keySet().stream()
                                .mapToInt(i -> i.column())
                                .max()
                                .getAsInt();
        matrix.values().removeIf(value -> value.equals(zero));
        NavigableMap<Indexes, S> rowMatrix = matrix;
        AbstractMatrix<Indexes, S> result = new NavigableMatrix<S>(rowMatrix, zero, rows, columns);
        return result;
    }

    //Helper method to convert matrixByRows to matrixByColumns, using priority queue
    private static <S> NavigableMap<Indexes, S> reorderByColumn(NavigableMap<Indexes, S> matrixByRows) {
        NavigableMap<Indexes, S> matrixByColumns = new TreeMap<>();
        PriorityQueue<Map.Entry<Indexes, S>> pq = new PriorityQueue<>(Comparator.comparing(entry -> entry.getKey().column()));
        pq.addAll(matrixByRows.entrySet());
        while (!pq.isEmpty()) {
            Map.Entry<Indexes, S> entry = pq.poll();
            matrixByColumns.put(entry.getKey(), entry.getValue()); 
        }
        return matrixByColumns;
    }
    
    
    public static <S> AbstractMatrix<Indexes, S> from(S[][] matrix, S zero){
        Objects.requireNonNull(matrix);
        Function<Indexes, S> valueMapper = indexes -> {
            return matrix[indexes.row()][indexes.column()];
        };
    
        return instance(matrix.length, matrix[0].length, valueMapper, zero);
    }
    
    public NavigableVector<T> row(Integer i) {
        Map<Integer, T> filteredMap = matrixByRows.entrySet()
                .stream()
                .filter(entry -> entry.getKey().row() == i) 
                .collect(Collectors.toMap(entry -> entry.getKey().column(), Map.Entry::getValue, (a, b) -> a, TreeMap::new)); 
        return NavigableVector.from(filteredMap, zero); 
    }
    
    public NavigableVector<T> column(Integer i){
        Map<Indexes, T> matrixByColumns = reorderByColumn(this.matrixByRows);
        Map<Integer, T> filteredMap = matrixByColumns.entrySet()
                .stream()
                .filter(entry -> entry.getKey().column() == i) 
                .collect(Collectors.toMap(entry -> entry.getKey().row(), Map.Entry::getValue, (a, b) -> a, TreeMap::new)); 
        return NavigableVector.from(filteredMap, zero).transpose(); 
    }
    

    @Override
    public PeekingIterator<Map.Entry<Indexes, T>> peekingIterator(){
        return PeekingIterator.from(this.representation().entrySet().iterator());
    }
    
    @Override
    public AbstractMatrix<Indexes, T> merge(Matrix<Indexes, T> other, BinaryOperator<T> op) {
        InconsistentZeroException.requireMatching(this, other);
        return NavigableMatrix.from(MapMerger.merge(this.peekingIterator(), other.peekingIterator(), Indexes.byrow, op, Indexes.ORIGIN, zero), zero);
    }

    public AbstractMatrix<Indexes, T> transpose(){
        Function<Indexes, T> valueMapper = indexes ->{
            return matrixByRows.get(new Indexes(indexes.column(), indexes.row()));
        };
        return instance(this.getColumns(), this.getRows(), valueMapper, zero());
    }

    @Override
    public AbstractMatrix<Indexes, T> entryWiseMultiplication(AbstractMatrix<Indexes, T> multiplier, BinaryOperator<T> multiply){
        InvalidMatrixOperationException.requireSameMatrixSizes(this.size(), multiplier.size());
        InconsistentZeroException.requireMatching(this, multiplier);
        return this.merge(multiplier, multiply);
    }

    @Override
    public AbstractMatrix<Indexes, T> multiply(AbstractMatrix<Indexes, T> multiplier, BinaryOperator<T> multiply, BinaryOperator<T> add){
        InvalidMatrixOperationException.requireMultipliableMatrices(this.size(), multiplier.size());
        InconsistentZeroException.requireMatching(this, multiplier);
        Function<Indexes, T> valueMapper = index -> {
            return this.row(index.row()).multiply(multiplier.column(index.column()), add, multiply).value(0);
        };
        return instance(this.getRows(), multiplier.getColumns(), valueMapper, zero());
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

        public static int requireNonEmpty(Cause cause, int length){
            if (length <= 0){
                throw new IllegalArgumentException(new InvalidLengthException(cause, length));
            }
            return length;
        }

    }
}
