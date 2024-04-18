package Matrix;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.BinaryOperator;

public class NavigableVector<T> extends AbstractMatrix<Integer, T> {

    private NavigableVector(NavigableMap<Integer,T> matrix, T zero, int rows, int columns){
        super(matrix, zero, rows, columns);
    }
    
    public static <S> NavigableVector<S> from(Map<Integer, S> vector, S zero) {
        Objects.requireNonNull(vector);
        Objects.requireNonNull(zero);

        int rows = Collections.max(vector.keySet());
        int columns = 1;

        NavigableMap<Integer, S> navigableMap = new TreeMap<>(vector);
        navigableMap.values().removeIf(value -> value.equals(zero));

        return new NavigableVector<S>(navigableMap, zero, rows, columns);
    }

    public PeekingIterator<Map.Entry<Integer,T>> peekingIterator(){
        return PeekingIterator.from(this.representation().entrySet().iterator());
    }

    @Override
    public NavigableVector<T> merge(Matrix<Integer, T> other, BinaryOperator<T> op) {
        InconsistentZeroException.requireMatching(this, other);
        return NavigableVector.from(MapMerger.merge(this.peekingIterator(), other.peekingIterator(),Comparator.naturalOrder(), op, 0, this.zero()), this.zero());
    }

    @Override
    public NavigableVector<T> entryWiseMultiplication(AbstractMatrix<Integer, T> multiplier, BinaryOperator<T> multiply){
        InvalidMatrixOperationException.requireSameMatrixSizes(this.size(), multiplier.size());
        InconsistentZeroException.requireMatching(this, multiplier);
        return this.merge(multiplier, multiply);
    }

    @Override
    public AbstractMatrix<Integer, T> multiply(AbstractMatrix<Integer ,T> multiplier, BinaryOperator<T> multiply, BinaryOperator<T> add){
        InvalidMatrixOperationException.requireMultipliableMatrices(this.size(), multiplier.size());
        InconsistentZeroException.requireMatching(this, multiplier);
        NavigableVector<T> entryWise = entryWiseMultiplication(multiplier.transpose(), multiply);
        Map<Integer, T> result = new TreeMap<>();
        result.put(0, entryWise.matrix.values().stream().reduce(zero(), add));
        return NavigableVector.from(result, zero());
    }

    public NavigableVector<T> transpose(){
        int rows = this.getRows();
        this.setRows(this.getColumns());
        this.setColumns(rows);
        return this;
    }

    @Override
    public NavigableVector<T> row(Integer row) {
        InvalidMatrixOperationException.requireValidVectorInput(new Indexes(row, this.size().column()), this.size());
        NavigableMap<Integer, T> rowMap = new TreeMap<>();
        rowMap.putAll(this.matrix.subMap(row, true, row, true));
        return new NavigableVector<>(rowMap, this.zero(), 1, this.getColumns());
    }
    
    @Override
    public NavigableVector<T> column(Integer column) {
        InvalidMatrixOperationException.requireValidVectorInput(new Indexes(this.size().row(), column), this.size());
        NavigableMap<Integer, T> columnMap = new TreeMap<>();
        for (Integer rowIndex : this.matrix.keySet()) {
            columnMap.put(rowIndex, this.value(column));
        }
        return new NavigableVector<>(columnMap, this.zero(), this.getRows(), 1);
    }
    
}
