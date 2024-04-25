package Matrix;

public final class InconsistentZeroException extends Exception{

    private static final long serialVersionUID = 0123L;
    
    /** 
     * Ensure that two matrices have matching zero values, else throw exception
     * 
     * @param thisMatrix
     * @param otherMatrix
     * @return T
     */
    public static <I,T> T requireMatching(Matrix<I,T> thisMatrix, Matrix<I,T> otherMatrix){
        if(thisMatrix.zero().equals(otherMatrix.zero())){
            return thisMatrix.zero();
        }
        else{
            throw new IllegalArgumentException("Zeros are not equal");
        }
    }
}
