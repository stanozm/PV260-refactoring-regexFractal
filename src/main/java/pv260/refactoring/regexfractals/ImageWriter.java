package pv260.refactoring.regexfractals;

import java.awt.Color;
import java.io.IOException;
import java.nio.file.Path;

public interface ImageWriter {

    /**
     * Save representation of image to file.
     * What the representation is is fully up to implementation.
     * It can either be machine-readable (e.g. binary, for compact storage),
     * or human-readable (e.g. image in png format)
     * @param colorPattern colors of individual pixels, indexed by x and y
     * @param pixelSize    size of each pixel
     * @param file         will contain the saved image
     */
    void write(Matrix<Color> colorPattern, int pixelSize, Path file) throws IOException;

}
