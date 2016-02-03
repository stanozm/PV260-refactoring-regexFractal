package pv260.refactoring.regexfractals;

import java.awt.Color;

public interface ImageConverter {



    /**
     * Convert image to its textual representation, writable to a file
     * @param colorPattern colors of individual pixels, indexed by x and y
     * @param pixelSize  size of each pixel
     * @return converted fractal
     */
	String convert(Color[][] colorPattern, int pixelSize);

}
