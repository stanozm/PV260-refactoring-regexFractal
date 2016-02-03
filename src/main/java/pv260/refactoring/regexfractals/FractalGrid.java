package pv260.refactoring.regexfractals;

import static java.util.Arrays.asList;
import java.util.Collections;
import java.util.List;

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
        private char signatureAddend;

        /**
         * use for root
         * @param size the size left for this slice
         */
        public QuadrantSlice(int size) {
            this.accumulatedX = 0;
            this.accumulatedY = 0;
            this.accumulatedSignature = "";
            children = asList(
                    new QuadrantSlice(size / 2, accumulatedX, accumulatedY, accumulatedSignature, true, true),
                    new QuadrantSlice(size / 2, accumulatedX, accumulatedY, accumulatedSignature, true, false),
                    new QuadrantSlice(size / 2, accumulatedX, accumulatedY, accumulatedSignature, false, true),
                    new QuadrantSlice(size / 2, accumulatedX, accumulatedY, accumulatedSignature, false, false)
            );
        }

        /**
         * use for children
         * @param size the size left for this slice
         */
        public QuadrantSlice(int size, int parentX, int parentY, String parentSignature, boolean isRightQuadrant, boolean isTopQuadrant) {
            this.accumulatedX = parentX + (isRightQuadrant ? size : 0);
            this.accumulatedY = parentY + (!isTopQuadrant ? size : 0);
            calculateSignatureAddend(isRightQuadrant, isTopQuadrant);
            this.accumulatedSignature = parentSignature + signatureAddend;
            if (size == 1) {
                isLeaf = true;
            } else { // this is not a child, so create more slices and make those children of this
                children = asList(
                        new QuadrantSlice(size / 2, accumulatedX, accumulatedY, accumulatedSignature, true, true),
                        new QuadrantSlice(size / 2, accumulatedX, accumulatedY, accumulatedSignature, true, false),
                        new QuadrantSlice(size / 2, accumulatedX, accumulatedY, accumulatedSignature, false, true),
                        new QuadrantSlice(size / 2, accumulatedX, accumulatedY, accumulatedSignature, false, false)
                );
            }
        }

        /**
         * calculate what quadrant this is and thus what it adds to the signature
         */
        private void calculateSignatureAddend(boolean isRightQuadrant, boolean isTopQuadrant) {
            if (isRightQuadrant && isTopQuadrant) {
                signatureAddend = '1';
            } else if (!isRightQuadrant && isTopQuadrant) {
                signatureAddend = '2';
            } else if (!isRightQuadrant && !isTopQuadrant) {
                signatureAddend = '3';
            } else if (isRightQuadrant && !isTopQuadrant) {
                signatureAddend = '4';
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
