package pv260.refactoring.regexfractals;

import java.awt.Color;
import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;
import java.util.regex.Pattern;

/**
 * Takes image represented by colors and gives image represented by string
 */
public class AsciiImageConverter implements ImageConverter {

    public String convert(Matrix<Color> colorPattern, int pixelSize) {
        validatePixelSize(pixelSize);
        StringBuilder gridBuffer = new StringBuilder(
                symbolCount(colorPattern.width(), colorPattern.height(), pixelSize));
        //we have to iterate by row
        for (int row = 0; row < colorPattern.height(); row++) {
            StringBuilder rowBuffer = new StringBuilder(
                    symbolCount(1, colorPattern.width(), pixelSize));
            for (int col = 0; col < colorPattern.width(); col++) {
                for (int i = 0; i < pixelSize; i++) {
                    Color color = colorPattern.get(col, row);
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

    private int symbolCount(int rows, int columns, int pixelSize){
        int columnsWithLinebreak = columns+1;
        int symbolsPerPixel = pixelSize*pixelSize;
        return rows*columnsWithLinebreak*symbolsPerPixel;
    }

    protected void validatePixelSize(int pixelSize) {
        if (pixelSize <= 0) {
            throw new IndexOutOfBoundsException("Pixel size must be at least 1");
        }
    }
}
