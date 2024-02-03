package Matrix;

import java.util.Comparator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public record Indexes(Integer row, Integer column) {

    private static final Comparator<Indexes> byrow = Comparator.comparingInt(Indexes::row).thenComparing(Indexes::column);

    private static final Comparator<Indexes> bycolumn = Comparator.comparingInt(Indexes::column).thenComparing(Indexes::row);

    public static final Indexes ORIGIN = new Indexes(0, 0);

    public Indexes withRow(int row){
        return new Indexes(row, this.column);
    }

    public Indexes withColumn(int column){
        return new Indexes(this.row, column);
    }

    public int compareByRow(Indexes index){
        return byrow.compare(this, index);
    }

    public <S> S value (S[][] matrix){
        if (matrix == null ){
            throw new IllegalArgumentException("Illegal matrix argument");
        }
        if (this.row - 1 > matrix.length){
            throw new ArrayIndexOutOfBoundsException("Row index is out of bounds of input matrix");
        }
        if (this.column - 1 > matrix[0].length){
            throw new ArrayIndexOutOfBoundsException("Column index is out of bounds of input matrix");
        }
        return matrix[this.row][this.column];
    }

    public boolean areDiagonal(){
        return this.row == this.column;
    }

    public static Stream<Indexes> stream(Indexes from, Indexes to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Illegal index input");
        }

        return IntStream.range(from.row(), to.row())
                .boxed()
                .flatMap(row -> IntStream.range(
                        (from.column()),
                        (to.column()))
                .mapToObj(column -> new Indexes(row, column))
                );
    }

    public static Stream<Indexes> stream(Indexes size){
        if (size == null) {
            throw new IllegalArgumentException("Illegal size input");
        }
        return stream(ORIGIN, size);
    }

    public static Stream<Indexes> stream(int rows, int columns){
        return stream(ORIGIN, new Indexes(rows, columns));
    }

    
}
