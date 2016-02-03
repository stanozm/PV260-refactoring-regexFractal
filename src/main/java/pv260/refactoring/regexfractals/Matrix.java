package pv260.refactoring.regexfractals;

/**
 * Data structure which holds elements in a 2D matrix.
 * Indexing is done by pair [x,y], where [0,0] is top left,
 * x increases along the horizontal axe, to the right,
 * y increases toward to vertical axis downwards:
 * <pre>
 * +---+---+
 * |0,0|1,0|
 * +-------+
 * |0,1|1,1|
 * +---+---+
 * </pre>
 * The object is mutable.
 * Both width and height of the matrix must always be at least 1.
 */
public interface Matrix<T> {

	int width();

	int height();

	T get(int x, int y);

	void set(T item, int x, int y);

}
