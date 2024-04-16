package Matrix;

import java.security.InvalidParameterException;

public class InvalidMatrixOperationException extends Exception {

    private static final long serialVersionUID = 01234L;

    private final Indexes thisMatrixSize;

    private final Indexes otherMatrixSize;

    public InvalidMatrixOperationException(Indexes thisMatrixSize, Indexes otherMatrixSize){
        this.thisMatrixSize = thisMatrixSize;
        this.otherMatrixSize = otherMatrixSize;
    }

    public Indexes getThisMatrixSize(){
        return this.thisMatrixSize;
    }

    public Indexes getOtherMatrixSize(){
        return this.otherMatrixSize;
    }

    public static void requireMultipliableMatrices(Indexes thisMatrixSize, Indexes otherMatrixSize){
        if(!thisMatrixSize.equals(new Indexes(otherMatrixSize.column(), otherMatrixSize.row()))){
            throw new InvalidParameterException("Matrix sizes are not multipliable");
        }
    }

    public static void requireSameMatrixSizes(Indexes thisMatrixSize, Indexes otherMatrixSize){
        if(!thisMatrixSize.equals(otherMatrixSize)){
            throw new InvalidParameterException("Matrix sizes must be equal");
        }
    }

    public static void requireValidVectorSize(Indexes matrixInput, Indexes matrixSize){
        if(matrixInput.row() != 1){
            throw new InvalidParameterException("Matrix sizes must be equal");
        }
        else if(matrixInput.column() < 0 || matrixInput.column() > matrixSize.column()){
            throw new InvalidParameterException("Matrix column out of bounds");
        }
    }

}
