package Matrix;

import java.util.Collections;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.BinaryOperator;

/**
 * Abstract class that represents Matrix or vector 
 */

public abstract class AbstractMatrix<I,T> implements Matrix<I,T> {

    protected final NavigableMap<I, T> matrix;

    private final T zero;

    //number of rows in abstract matrix
    private int rows;

    //number of columns in abstract matrix
    private int columns;

    protected AbstractMatrix(NavigableMap<I,T> matrix, T zero, int rows, int columns) {
        this.matrix = Collections.unmodifiableNavigableMap(matrix);
        this.zero = zero;
        this.rows = rows;
        this.columns = columns;
    }

    
    /** 
     * Returns value at a certain index
     * 
     * @param index key identifier of type I
     * @return T value identifier
     */
    @Override
    public T value(I index){
        Objects.requireNonNull(index);
        return this.matrix.getOrDefault(index, zero());
    }

    
    /** 
     * Zero value of used by this AbstractMatrix
     * 
     * @return T zero value
     */
    @Override
    public T zero(){
        return this.zero;
    }

    
    /** 
     * Matrix representation in NavigableMap format
     * 
     * @return NavigableMap<I, T> 
     */
    @Override
    public NavigableMap<I, T> representation(){
        return new TreeMap<>(this.matrix);
    }

    
    /** 
     * Number of rows
     * 
     * @return int 
     */
    public int getRows(){
        return this.rows;
    }
    
    /** 
     * Number of columns
     * 
     * @return int 
     */
    public int getColumns(){
        return this.columns;
    }

    
    /** 
     * Indicate number of rows in this Matrix
     * 
     * @param rows
     */
    public void setRows(int rows){
        this.rows = rows;
    }

    
    /** 
     * Indicate number of columns in this Matrix
     * 
     * @param columns set number of columns
     */
    public void setColumns(int columns){
        this.columns = columns;
    }

    
    /** 
     * Size of AbstractMatrix in format (rows, columns)
     * 
     * @return Indexes
     */
    public Indexes size(){
        return new Indexes(this.getRows(), this.getColumns());
    }

    public abstract AbstractMatrix<I,T> transpose();

    public abstract NavigableVector<T> row(Integer i);

    public abstract NavigableVector<T> column(Integer i);

    public abstract AbstractMatrix<I, T> multiply(AbstractMatrix<I, T> multiplier, BinaryOperator<T> multiply, BinaryOperator<T> add);

    public abstract AbstractMatrix<I, T> entryWiseMultiplication(AbstractMatrix<I, T> multiplier, BinaryOperator<T> multiply);
}
