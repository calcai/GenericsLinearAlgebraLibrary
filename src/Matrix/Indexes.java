package Matrix;

import java.util.Comparator;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * Index representation
 */
public record Indexes(Integer row, Integer column) implements Comparable<Indexes> {

    public static final Comparator<Indexes> byrow = Comparator.comparingInt(Indexes::row).thenComparing(Indexes::column);

    public static final Comparator<Indexes> bycolumn = Comparator.comparingInt(Indexes::column).thenComparing(Indexes::row);

    public static final Indexes ORIGIN = new Indexes(0, 0);

    
    /** 
     * get an Indexes object by maintaining the column and replacing the row with parameter value
     * 
     * @param row
     * @return Indexes
     */
    public Indexes withRow(int row){
        return new Indexes(row, this.column);
    }

    
    /** 
     * Get an Indexes object by maintaining the column and replacing the row with parameter value
     * 
     * @param column
     * @return Indexes
     */
    public Indexes withColumn(int column){
        return new Indexes(this.row, column);
    }

    
    /** 
     * Compares this object with the specified object for order. Returns a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object. Checks row first then column as tiebreaker.
     * 
     * @param other
     * @return int
     */
    public int compareTo(Indexes other) {
        int rowComparison = Integer.compare(this.row(), other.row());
        if (rowComparison != 0) {
            return rowComparison;
        }
        return Integer.compare(this.column(), other.column());
    }

    
    /** 
     * Returns value 
     * 
     * @param matrix
     * @return S
     */
    public <S> S value (S[][] matrix){
        Objects.requireNonNull(matrix);
        if (this.row - 1 > matrix.length){
            throw new ArrayIndexOutOfBoundsException("Row index is out of bounds of input matrix");
        }
        if (this.column - 1 > matrix[0].length){
            throw new ArrayIndexOutOfBoundsException("Column index is out of bounds of input matrix");
        }
        return matrix[this.row][this.column];
    }

    
    /** 
     * Gets value of the input matrix and this index
     * 
     * @param matrix
     * @return S
     */
    public <S> S value(Matrix<Indexes, S> matrix){
        Objects.requireNonNull(matrix);
        return matrix.value(new Indexes(this.row, this.column));
    }

    
    /** 
     * Returns whether the index is at a diagonal
     * 
     * @return boolean
     */
    public boolean areDiagonal(){
        return this.row == this.column;
    }

    
    /** 
     * Indicates whether the corresponding object has the same corresponding row and column values
     * 
     * @param index
     * @return boolean
     */
    public boolean equals(Indexes index){
        if (this.row().equals(index.row()) && this.column().equals(index.column())){
            return true;
        }
        return false;
    }

    
    /** 
     * Returns a stream of Indexes all values between two indexes where all row and column values are within range
     * 
     * @param from
     * @param to
     * @return Stream<Indexes>
     */
    public static Stream<Indexes> stream(Indexes from, Indexes to) {
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);
        //Generates a stream of Indexes objects representing a range of rows and columns from 'from' row and column (inclusive) to 'to' row and column (exclusive).
        return IntStream.range(from.row(), to.row())
                .boxed()
                .flatMap(row -> IntStream.range(
                        (from.column()),
                        (to.column()))
                .mapToObj(column -> new Indexes(row, column))
                );
    }

    
    /** 
     * Returns a stream of Indexes from point (0,0) to specified point
     * 
     * @param size
     * @return Stream<Indexes>
     */
    public static Stream<Indexes> stream(Indexes size){
        Objects.requireNonNull(size);
        return stream(ORIGIN, size);
    }

    
    /**
     * Returns stream from point (0,0) to specified row in column value as (row, column)
     *  
     * @param rows
     * @param columns
     * @return Stream<Indexes>
     */
    public static Stream<Indexes> stream(int rows, int columns){
        return stream(ORIGIN, new Indexes(rows, columns));
    }

}
