package Matrix;


public class BinaryOperations<T> {

    
    /** 
     * Example possible BinaryOperator for multiply, using type Integer
     * 
     * @param t
     * @param u
     * @return T
     */
    @SuppressWarnings("unchecked")
    public static <T> T multiply(T t, T u){
        return (T)Integer.valueOf((Integer)t*(Integer)u);
    }

    /** 
     * Example possible BinaryOperator for add, using type Integer
     * 
     * @param t
     * @param u
     * @return T
     */
    @SuppressWarnings("unchecked")
    public static <T> T add(T t, T u){
        return (T)Integer.valueOf((Integer)t+(Integer)u);
    }
}
