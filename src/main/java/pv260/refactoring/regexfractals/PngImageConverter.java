package pv260.refactoring.regexfractals;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class PngImageConverter extends AsciiImageConverter {

    private File outputFile;

    public PngImageConverter(File outputFile) {
        this.outputFile = outputFile;
    }

    /**
     * {@inheritDoc }
     * <p>
     * as a side effect creates a file (name passed in constructor)
     * containing the resulting image
     * @return always null
     */
    @Override
    public String convert(Matrix<Color> colorPattern, int pixelSize) {
        validatePixelSize(pixelSize);
        BufferedImage image = new BufferedImage(colorPattern.width() * pixelSize,
                                                colorPattern.height() * pixelSize,
                                                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        for (int row = 0; row < colorPattern.height(); row++) {
            for (int col = 0; col < colorPattern.width(); col++) {
                g2d.setColor(colorPattern.get(col, row));
                g2d.fillRect(col * pixelSize, row * pixelSize, pixelSize, pixelSize);
            }
        }
        output(image);
        return null;
    }

    private void output(BufferedImage image) {
        try {
            ImageIO.write(image, "png", outputFile);
        } catch (IOException ex) {
            throw new IllegalStateException("Cant write image to " + outputFile, ex);
        }

    }

}
