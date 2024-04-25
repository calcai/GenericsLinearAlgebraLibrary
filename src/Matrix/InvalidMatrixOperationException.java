package Matrix;

import java.security.InvalidParameterException;


/**
 * Exception class that ensures matrix operations are valid
 */
public class InvalidMatrixOperationException extends Exception {

    private static final long serialVersionUID = 01234L;

    
    /** 
     * Checks that the specified matrix sizes are multipliable column of this matrix must be same size as row of specified matrix
     * 
     * @param thisMatrixSize
     * @param otherMatrixSize
     */
    public static void requireMultipliableMatrices(Indexes thisMatrixSize, Indexes otherMatrixSize){
        if(!(thisMatrixSize.column() == otherMatrixSize.row())){
            throw new InvalidParameterException("Matrix sizes are not multipliable");
        }
    }

    
    /** 
     * Checks that the specified matrices are the same dimensions
     * 
     * @param thisMatrixSize
     * @param otherMatrixSize
     */
    public static void requireSameMatrixSizes(Indexes thisMatrixSize, Indexes otherMatrixSize){
        if(!thisMatrixSize.equals(otherMatrixSize)){
            throw new InvalidParameterException("Matrix sizes must be equal");
        }
    }

    
    /** 
     * Checks that matrix is a vector and the input is within range
     * 
     * @param matrixInput
     * @param matrixSize
     */
    public static void requireValidVectorInput(Indexes matrixInput, Indexes matrixSize){
        if(matrixInput.row() != 1 && matrixInput.column() != 1){
            throw new InvalidParameterException("Vector size invalid");
        }
        else if(matrixInput.column() < 0 || matrixInput.row() < 0 || matrixInput.column() > matrixSize.column() || matrixInput.row() > matrixSize.row()){
            throw new InvalidParameterException("Vector index out of bounds");
        }
    }
}
