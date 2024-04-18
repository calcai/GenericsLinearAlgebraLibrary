package Matrix;

import static org.junit.Assert.assertFalse;

import org.junit.Test;


public class IndexesTest {

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testValueWithRowOutOfBounds() {
        Integer[][] matrix = {{1, 2}, {3, 4}};
        Indexes indexes = new Indexes(5, 0);

        indexes.value(matrix);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testValueWithColumnOutOfBounds() {
        Integer[][] matrix = {{1, 2}, {3, 4}};
        Indexes indexes = new Indexes(0, 5);

        indexes.value(matrix);
    }

    @Test
    public void testEqualsWithDifferentRows() {
        Indexes indexes1 = new Indexes(1, 2);
        Indexes indexes2 = new Indexes(3, 2);

        assertFalse(indexes1.equals(indexes2));
    }

    @Test
    public void testEqualsWithDifferentColumns() {
        Indexes indexes1 = new Indexes(1, 2);
        Indexes indexes2 = new Indexes(1, 3);

        assertFalse(indexes1.equals(indexes2));
    }
}
