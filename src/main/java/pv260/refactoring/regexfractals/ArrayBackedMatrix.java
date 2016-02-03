package pv260.refactoring.regexfractals;

/**
 * Implementation of the {@link Matrix} using 2D array
 */
public class ArrayBackedMatrix<T> implements Matrix<T> {

    private Object[][] backingArray;

    @Override
    public int width() {
        return backingArray.length;
    }

    @Override
    public int height() {
        return backingArray[0].length;
    }

    /**
     * @throws IllegalArgumentException if either of the width and height are <= 0
     */
    public ArrayBackedMatrix(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Got size " + width + "x" + height + ", size is required to be at least 1x1");
        }
        backingArray = new Object[width][height];
    }

    /**
     * {@inheritDoc }
     * @throws IndexOutOfBoundsException if either the x or y are <0
     * or >= than the respective dimension of the matrix
     * */
    @Override
    public T get(int x, int y) {
        validateIndex(x, y);
        // The cast is always safe as the set (only way to add items )
        // quarantees that the item is of correct type
        return (T) backingArray[x][y];
    }

    /**
     * {@inheritDoc }
     * @throws IndexOutOfBoundsException if either the x or y are <0
     * or >= than the respective dimension of the matrix
     * */
    @Override
    public void set(T item, int x, int y) {
        validateIndex(x, y);
        backingArray[x][y] = item;
    }

    private void validateIndex(int x, int y) {
        if (x < 0 || x >= width()
                || y < 0 || y >= height()) {
            throw new IndexOutOfBoundsException("Got index [" + x + "," + y + "] but the matrix is only " + width() + "x" + height());
        }
    }
}
