package Matrix;

import java.security.InvalidParameterException;

public class InvalidMatrixOperationException extends Exception {

    private static final long serialVersionUID = 01234L;

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

    public static void requireValidVectorInput(Indexes matrixInput, Indexes matrixSize){
        if(matrixInput.row() != 1 && matrixInput.column() != 1){
            throw new InvalidParameterException("Vector size invalid");
        }
        else if(matrixInput.column() < 0 || matrixInput.row() < 0 || matrixInput.column() > matrixSize.column() || matrixInput.row() > matrixSize.row()){
            throw new InvalidParameterException("Vector index out of bounds");
        }
    }
}
