package Matrix;


public class BinaryOperations<T> {

    @SuppressWarnings("unchecked")
    public static <T> T multiply(T t, T u){
        return (T)Integer.valueOf((Integer)t*(Integer)u);
    }

    @SuppressWarnings("unchecked")
    public static <T> T add(T t, T u){
        return (T)Integer.valueOf((Integer)t+(Integer)u);
    }
}
