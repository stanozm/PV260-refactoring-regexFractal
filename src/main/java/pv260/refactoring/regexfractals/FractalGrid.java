package pv260.refactoring.regexfractals;

import static java.util.Arrays.asList;
import java.util.Collections;
import java.util.List;
import static pv260.refactoring.regexfractals.Quadrant.LOWER_LEFT;
import static pv260.refactoring.regexfractals.Quadrant.LOWER_RIGHT;
import static pv260.refactoring.regexfractals.Quadrant.UPPER_RIGHT;
import static pv260.refactoring.regexfractals.Quadrant.UPPER_LEFT;

/**
 * Grid of the Fractal stored in an array
 */
public class FractalGrid {

    private Matrix<String> signatures;

    /**
     * @param gridSide the size
     * @param limit    send in 1 for default behavior
     */
    public FractalGrid(int gridSide, double limit) {
        QuadrantSlice rootSlize = new QuadrantSlice(gridSide);
        signatures = new ArrayBackedMatrix<>(gridSide, gridSide);
        rootSlize.writeLeavesTo(signatures);
    }

    /**
     * Return signature of the pixel at given coordinates
     */
    public String signatureOf(int x, int y) {
        return signatures.get(x, y);
    }

    public int side() {
        return signatures.width();
    }

    /**
     * One quarter of the parents space
     * top left is 2, to right is 1,
     * bottom left is 3, bottom right is 4
     */
    private static class QuadrantSlice {

        private int accumulatedX;
        private int accumulatedY;
        private String accumulatedSignature;
        private boolean isLeaf = false;
        private List<QuadrantSlice> children = Collections.emptyList();

        /**
         * use for root
         * @param size the size left for this slice
         */
        public QuadrantSlice(int size) {
            this.accumulatedX = 0;
            this.accumulatedY = 0;
            this.accumulatedSignature = "";
            children = asList(
                    new QuadrantSlice(size / 2, accumulatedX, accumulatedY, accumulatedSignature, UPPER_RIGHT),
                    new QuadrantSlice(size / 2, accumulatedX, accumulatedY, accumulatedSignature, UPPER_LEFT),
                    new QuadrantSlice(size / 2, accumulatedX, accumulatedY, accumulatedSignature, LOWER_LEFT),
                    new QuadrantSlice(size / 2, accumulatedX, accumulatedY, accumulatedSignature, LOWER_RIGHT)
            );
        }

        /**
         * use for children
         * @param size the size left for this slice
         */
        public QuadrantSlice(int size, int parentX, int parentY, String parentSignature, Quadrant quadrant) {
            this.accumulatedX = parentX + quadrant.xAddend(size);
            this.accumulatedY = parentY + quadrant.yAddend(size);
            this.accumulatedSignature = parentSignature + quadrant.signatureAddend();
            if (size == 1) {
                isLeaf = true;
            } else { // this is not a child, so create more slices and make those children of this
                children = asList(
                        new QuadrantSlice(size / 2, accumulatedX, accumulatedY, accumulatedSignature, UPPER_RIGHT),
                        new QuadrantSlice(size / 2, accumulatedX, accumulatedY, accumulatedSignature, UPPER_LEFT),
                        new QuadrantSlice(size / 2, accumulatedX, accumulatedY, accumulatedSignature, LOWER_LEFT),
                        new QuadrantSlice(size / 2, accumulatedX, accumulatedY, accumulatedSignature, LOWER_RIGHT)
                );
            }
        }

        /**
         * if this is a leaf add its signature to the collecting parameter,
         * else pass it to all children recursively
         * this method starts at the root and eventually collects signatures of all the children
         * into the array
         * called from constructor to initialize the signaturesArray
         */
        private void writeLeavesTo(Matrix<String> signaturesArray) {
            if (isLeaf) {
                signaturesArray.set(accumulatedSignature, accumulatedX, accumulatedY);
            } else {
                for (QuadrantSlice child : children) {
                    child.writeLeavesTo(signaturesArray);
                }
            }
        }
    }
}
