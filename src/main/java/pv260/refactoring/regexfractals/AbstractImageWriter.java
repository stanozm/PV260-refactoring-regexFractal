/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pv260.refactoring.regexfractals;

import java.awt.Color;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Base implementation of the conversion algorithm which supplies
 * hooks in the main template method loop for subtypes to drive their custom behavior
 */
public abstract class AbstractImageWriter implements ImageWriter {

    @Override
    public void write(Matrix<Color> colorPattern, int pixelSize, Path file) throws IOException {
        validatePixelSize(pixelSize);
        writeStarted(colorPattern.width(),
                          colorPattern.height(),
                          pixelSize);
        for (int y = 0; y < colorPattern.width(); y++) {
            rowStarted();
            for (int x = 0; x < colorPattern.height(); x++) {
                Color color = colorPattern.get(x, y);
                writeCell(color, x, y);
            }
            rowEnded();
        }
        saveResultToFile(file);
    }

    private void validatePixelSize(int pixelSize) {
        if (pixelSize <= 0) {
            throw new IndexOutOfBoundsException("Pixel size must be at least 1");
        }
    }

    protected void writeStarted(int width, int height, int pixelSize) {
    }

    protected void rowStarted() {
    }

    protected void rowEnded() {
    }

    protected abstract void writeCell(Color color, int x, int y);

    protected abstract void saveResultToFile(Path file) throws IOException;

}
