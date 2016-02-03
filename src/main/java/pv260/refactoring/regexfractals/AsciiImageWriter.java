package pv260.refactoring.regexfractals;

import java.awt.Color;
import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

/**
 * Takes image represented by colors and gives image represented by string
 */
public class AsciiImageWriter extends AbstractImageWriter {

    private StringBuilder gridBuffer;
    private StringBuilder lineBuffer;
    private int pixelSize;

    @Override
    protected void writeStarted(int width, int height, int pixelSize) {
        gridBuffer = new StringBuilder(symbolCount(width, height, pixelSize));
        this.pixelSize = pixelSize;
    }

    @Override
    protected void rowStarted() {
        lineBuffer = new StringBuilder();
    }

    @Override
    protected void rowEnded() {
        for (int i = 0; i < pixelSize; i++) {
            gridBuffer.append(lineBuffer);
            gridBuffer.append("\n");
        }
    }

    @Override
    protected void writeCell(Color color, int x, int y) {
        for (int i = 0; i < pixelSize; i++) {
            lineBuffer.append(color == BLACK ? "\u2588" : " ");
        }
    }

    @Override
    protected void saveResultToFile(Path file) throws IOException {
        Files.write(file, gridBuffer.toString().getBytes());
    }

    private int symbolCount(int rows, int columns, int pixelSize) {
        int columnsWithLinebreak = columns + 1;
        int symbolsPerPixel = pixelSize * pixelSize;
        return rows * columnsWithLinebreak * symbolsPerPixel;
    }
}
