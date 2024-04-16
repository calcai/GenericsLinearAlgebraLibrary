package Matrix;

import java.util.Collections;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.BinaryOperator;

public abstract class AbstractMatrix<I,T> implements Matrix<I,T> {

    protected final NavigableMap<I, T> matrix;

    private final T zero;

    private int rows;

    private int columns;

    protected AbstractMatrix(NavigableMap<I,T> matrix, T zero, int rows, int columns) {
        this.matrix = Collections.unmodifiableNavigableMap(matrix);
        this.zero = zero;
        this.rows = rows;
        this.columns = columns;
    }

    @Override
    public T value(I index){
        Objects.requireNonNull(index);
        return this.matrix.get(index);
    }

    @Override
    public T zero(){
        return this.zero;
    }

    @Override
    public NavigableMap<I, T> representation(){
        return new TreeMap<>(this.matrix);
    }

    public int getRows(){
        return this.rows;
    }

    public int getColumns(){
        return this.columns;
    }

    public void setRows(int rows){
        this.rows = rows;
    }

    public void setColumns(int columns){
        this.columns = columns;
    }

    public Indexes size(){
        return new Indexes(this.getRows(), this.getColumns());
    }

    public abstract PeekingIterator<Map.Entry<I,T>> peekingIterator();

    public abstract AbstractMatrix<I,T> transpose();

    public abstract NavigableVector<T> row(Integer i);

    public abstract NavigableVector<T> column(Integer i);

    public abstract AbstractMatrix<I, T> multiply(AbstractMatrix<I, T> multiplier, BinaryOperator<T> multiply, BinaryOperator<T> add);

    public abstract AbstractMatrix<I, T> entryWiseMultiplication(AbstractMatrix<I, T> multiplier, BinaryOperator<T> multiply);


}
