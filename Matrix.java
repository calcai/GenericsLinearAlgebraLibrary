package Matrix;

import java.util.NavigableMap;

public interface Matrix<I, T> {

    T value(I index);

    T zero();
    
    NavigableMap<I, T> representation();

}
