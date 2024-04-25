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
    
    
    /** 
     * Creates a NavigableVector from a Map
     * 
     * @param vector
     * @param zero
     * @return NavigableVector<S>
     */
    public static <S> NavigableVector<S> from(Map<Integer, S> vector, S zero) {
        Objects.requireNonNull(vector);
        Objects.requireNonNull(zero);

        int rows = Collections.max(vector.keySet());
        int columns = 1;

        NavigableMap<Integer, S> navigableMap = new TreeMap<>(vector);
        navigableMap.values().removeIf(value -> value.equals(zero));

        return new NavigableVector<S>(navigableMap, zero, rows, columns);
    }

    
    /** 
     * Returns the NavigableVector as a PeekingIterator
     * 
     * @return PeekingIterator<Entry<Integer, T>>
     */
    public PeekingIterator<Map.Entry<Integer,T>> peekingIterator(){
        return PeekingIterator.from(this.representation().entrySet().iterator());
    }

    
    /** 
     * Applies a binary operation on each corresponding index of two vectors 
     * 
     * @param other
     * @param op
     * @return NavigableVector<T>
     */
    @Override
    public NavigableVector<T> merge(Matrix<Integer, T> other, BinaryOperator<T> op) {
        InconsistentZeroException.requireMatching(this, other);
        return NavigableVector.from(MapMerger.merge(this.peekingIterator(), other.peekingIterator(),Comparator.naturalOrder(), op, 0, this.zero()), this.zero());
    }

    
    /** 
     * Multiplies each corresponding index based on user defined binary operator for multiply
     * 
     * @param multiplier
     * @param multiply
     * @return NavigableVector<T>
     */
    @Override
    public NavigableVector<T> entryWiseMultiplication(AbstractMatrix<Integer, T> multiplier, BinaryOperator<T> multiply){
        InvalidMatrixOperationException.requireSameMatrixSizes(this.size(), multiplier.size());
        InconsistentZeroException.requireMatching(this, multiplier);
        return this.merge(multiplier, multiply);
    }

    
    /** 
     * Uses standard matrix multiplication from this to specified matrix based on user defined multiply and add operators
     * 
     * @param multiplier
     * @param multiply
     * @param add
     * @return AbstractMatrix<Integer, T>
     */
    @Override
    public AbstractMatrix<Integer, T> multiply(AbstractMatrix<Integer ,T> multiplier, BinaryOperator<T> multiply, BinaryOperator<T> add){
        InvalidMatrixOperationException.requireMultipliableMatrices(this.size(), multiplier.size());
        InconsistentZeroException.requireMatching(this, multiplier);
        NavigableVector<T> entryWise = entryWiseMultiplication(multiplier.transpose(), multiply);
        Map<Integer, T> result = new TreeMap<>();
        result.put(0, entryWise.matrix.values().stream().reduce(zero(), add));
        return NavigableVector.from(result, zero());
    }

    
    /** 
     * Flips row and column indices to return a transposed vector
     * 
     * @return NavigableVector<T>
     */
    public NavigableVector<T> transpose(){
        int rows = this.getRows();
        this.setRows(this.getColumns());
        this.setColumns(rows);
        return this;
    }

    
    /** 
     * Returns row representation of row i as a NavigableVector
     * 
     * @param row
     * @return NavigableVector<T>
     */
    //returns row representation
    @Override
    public NavigableVector<T> row(Integer row) {
        InvalidMatrixOperationException.requireValidVectorInput(new Indexes(row, this.size().column()), this.size());
        NavigableMap<Integer, T> rowMap = new TreeMap<>();
        rowMap.putAll(this.matrix.subMap(row, true, row, true));
        return new NavigableVector<>(rowMap, this.zero(), 1, this.getColumns());
    }
    
    
    /** 
     * Returns column representation of column i as a NavigableVector
     * 
     * @param column
     * @return NavigableVector<T>
     */
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
