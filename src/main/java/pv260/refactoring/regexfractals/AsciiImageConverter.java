package pv260.refactoring.regexfractals;

import java.awt.Color;
import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;
import java.util.regex.Pattern;

/**
 * Takes image represented by colors and gives image represented by string
 */
public class AsciiImageConverter implements ImageConverter {

    public String convert(Color[][] collorPattern, int pixelSize) {
        validatePixelSize(pixelSize);
        int gridSide = collorPattern.length;
        StringBuilder gridBuffer = new StringBuilder(gridSide * gridSide * pixelSize * pixelSize + gridSide * pixelSize);
        //we have to iterate by row
        for (int x = 0; x < gridSide; x++) {
            StringBuilder rowBuffer = new StringBuilder(gridSide * pixelSize);
            for (int y = 0; y < gridSide; y++) {
                for (int i = 0; i < pixelSize; i++) {
                    Color color = collorPattern[x][y];
                    rowBuffer.append(color == BLACK ? "\u2588" : " ");
                }
            }
            for (int i = 0; i < pixelSize; i++) {
                gridBuffer.append(rowBuffer);
                gridBuffer.append("\n");
            }

        }
        return gridBuffer.toString();
    }

    protected void validatePixelSize(int pixelSize) {
        if (pixelSize <= 0) {
            throw new IndexOutOfBoundsException("Pixel size must be at least 1");
        }
    }

    /**
     * Paint WHITE if the pattern matches, else BLACK
     */
    protected Color colorFor(Pattern pattern, String pixelSignature) {
        return pattern.matcher(pixelSignature).matches() ? WHITE : BLACK;
    }

}
