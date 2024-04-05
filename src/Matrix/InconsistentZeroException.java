package Matrix;

public final class InconsistentZeroException extends Exception{

    private static final long serialVersionUID = 0123L;

    private final String thisZero;
    private final String otherZero;

    public InconsistentZeroException(String thisZero, String otherZero){
        this.thisZero = thisZero;
        this.otherZero = otherZero;
    }

    public String getThisZero(){
        return this.thisZero;
    }
    
    public String getOtherZero(){
        return this.otherZero;
    }

    public static <I,T> T requireMatching(Matrix<I,T> thisMatrix, Matrix<I,T> otherMatrix){
        if(thisMatrix.zero().equals(otherMatrix.zero())){
            return thisMatrix.zero();
        }
        else{
            throw new IllegalArgumentException("Zeros are not equal");
        }
    }
}
