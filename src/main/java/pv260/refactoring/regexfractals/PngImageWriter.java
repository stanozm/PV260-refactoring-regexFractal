package pv260.refactoring.regexfractals;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import javax.imageio.ImageIO;

public class PngImageWriter extends AsciiImageWriter {

    private static final String PNG_FORMAT = "png";

    private BufferedImage image;
    private Graphics2D g2d;

    private int pixelSize;

    @Override
    protected void writeStarted(int width, int height, int pixelSize) {
        image = new BufferedImage(
                width * pixelSize, height * pixelSize,
                BufferedImage.TYPE_INT_RGB);
        g2d = image.createGraphics();
        this.pixelSize = pixelSize;
    }

    @Override
    protected void writeCell(Color color, int x, int y) {
        g2d.setColor(color);
        g2d.fillRect(x * pixelSize, y * pixelSize, pixelSize, pixelSize);
    }

    @Override
    protected void saveResultToFile(Path file) throws IOException {
        ImageIO.write(image, PNG_FORMAT, file.toFile());
    }
}
