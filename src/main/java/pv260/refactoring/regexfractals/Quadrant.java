package pv260.refactoring.regexfractals;

public enum Quadrant {

    UPPER_LEFT(0, 0, '2'),
    UPPER_RIGHT(1, 0, '1'),
    LOWER_LEFT(0, 1, '3'),
    LOWER_RIGHT(1, 1, '4');

    private final int addX;
    private final int addY;
    private final char signatureAddend;

    private Quadrant(int addX, int addY, char signatureAddend) {
        this.addX = addX;
        this.addY = addY;
        this.signatureAddend = signatureAddend;
    }

    /**
     * @return how much will moving into this quadrant
     * moves the left edge along the x axis
     */
    public int xAddend(int quadrantSize) {
        return this.addX * quadrantSize;
    }

    /**
     * @return how much will moving into this quadrant
     * moves the top edge along the y axis
     */
    public int yAddend(int quadrantSize) {
        return this.addY * quadrantSize;
    }

    /**
     * @return symbol to append to signature of cell
     * after moving into this quadrant
     */
    public char signatureAddend() {
        return this.signatureAddend;
    }
}
