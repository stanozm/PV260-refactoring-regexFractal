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
        Fragment root = new FragmentRoot(gridSide);
        signatures = new ArrayBackedMatrix<>(gridSide, gridSide);
        root.writeLeavesTo(signatures);
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

    private static class FragmentLeaf extends Fragment {

        public FragmentLeaf(Fragment parent, Quadrant quadrant) {
            this.size = parent.size / 2;
            this.accumulatedX = parent.accumulatedX + quadrant.xAddend(size);
            this.accumulatedY = parent.accumulatedY + quadrant.yAddend(size);
            this.accumulatedSignature = parent.accumulatedSignature + quadrant.signatureAddend();
        }

        @Override
        public void writeLeavesTo(Matrix<String> signaturesArray) {
            signaturesArray.set(accumulatedSignature, accumulatedX, accumulatedY);
        }
    }

    private static class FragmentIntermediate extends FragmentSplittable {

        public FragmentIntermediate(Fragment parent, Quadrant quadrant) {
            this.size = parent.size / 2;
            this.accumulatedX = parent.accumulatedX + quadrant.xAddend(size);
            this.accumulatedY = parent.accumulatedY + quadrant.yAddend(size);
            this.accumulatedSignature = parent.accumulatedSignature + quadrant.signatureAddend();
            this.children = createChildren();
        }
    }

    private static class FragmentRoot extends FragmentSplittable {

        public FragmentRoot(int size) {
            this.accumulatedX = 0;
            this.accumulatedY = 0;
            this.accumulatedSignature = "";
            this.size = size;
            this.children = createChildren();
        }
    }

    private static class FragmentSplittable extends Fragment {

        protected List<Fragment> children = Collections.emptyList();

        protected List<Fragment> createChildren() {
            return asList(
                    createChild(UPPER_RIGHT),
                    createChild(UPPER_LEFT),
                    createChild(LOWER_LEFT),
                    createChild(LOWER_RIGHT)
            );
        }

        private Fragment createChild(Quadrant quadrant) {
            //if parents size is only 2, his children will be 1, thus leaves
            if (size == 2) {
                return new FragmentLeaf(this, quadrant);
            } else {
                return new FragmentIntermediate(this, quadrant);
            }
        }

        @Override
        public void writeLeavesTo(Matrix<String> signaturesArray) {
            for (Fragment child : children) {
                child.writeLeavesTo(signaturesArray);
            }
        }
    }

    /**
     * One quarter of the parents space
     * top left is 2, to right is 1,
     * bottom left is 3, bottom right is 4
     */
    private abstract static class Fragment {

        protected int accumulatedX;
        protected int accumulatedY;
        protected String accumulatedSignature;
        protected int size;

        /**
         * if this is a leaf add its signature to the collecting parameter,
         * else pass it to all children recursively
         * this method starts at the root and eventually collects signatures of all the children
         * into the array
         * called from constructor to initialize the signaturesArray
         */
        public abstract void writeLeavesTo(Matrix<String> signaturesArray);
    }
}
