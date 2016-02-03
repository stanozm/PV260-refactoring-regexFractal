package pv260.refactoring.regexfractals;

import java.awt.Color;
import java.io.BufferedWriter;
import static java.lang.String.format;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Pattern;

public class RegexFractals {

    private static final int DEFAULT_PIXEL_SIZE = 1;
    private static final String DEFAULT_OUTPUT_ASCII = "output/fractal.txt";
    private static final String DEFAULT_OUTPUT_PNG = "output/fractal.png";

    //some nice inputs
    //.*1.*
    //.*(12|23|34|41).*
    //.*(13|31|24|42).*
    public static void doMain(String[] args) throws Exception {
        ParsedArgs parsedArgs = new ParsedArgs();
        try {
            parsedArgs.parse(args);
        } catch (InvalidInputException e) {
            throw new IllegalArgumentException("Invalid input.\n"
                    + "Expected arguments:\n"
                    + "grid side\n"
                    + "    must be positive power of 2\n"
                    + "\n"
                    + "regex pattern\n"
                    + "    must contain only symbols 1234, capturing group,\n"
                    + "    altrenative, option, wildcards and basiciterations\n"
                    + "\n"
                    + "Errors:\n"
                    + e.causes());
        }

        AsciiImageConverter asciiConverter = new AsciiImageConverter();
        FractalGrid grid = new FractalGrid(parsedArgs.gridSide(), 1);
        Color[][] colorGrid = new Color[parsedArgs.gridSide()][parsedArgs.gridSide()];
        for (int i = 0; i < parsedArgs.gridSide(); i++) {
            for (int j = 0; j < parsedArgs.gridSide(); j++) {
                Color pixelColor = asciiConverter.colorFor(Pattern.compile(parsedArgs.pattern()), grid.signatureOf(i, j));
                colorGrid[j][i] = pixelColor;
            }
        }

        Files.createDirectories(Paths.get(DEFAULT_OUTPUT_ASCII).getParent());
        try (BufferedWriter w = Files.newBufferedWriter(Paths.get(DEFAULT_OUTPUT_ASCII), Charset.forName("UTF-8"))) {
            w.write(asciiConverter.convert(colorGrid, parsedArgs.pixelSize()));
        }

        PngImageConverter pngConverter = new PngImageConverter(new File(DEFAULT_OUTPUT_PNG));
        pngConverter.convert(colorGrid, parsedArgs.pixelSize());
    }

    public static void main(String[] args) {
        try {
            doMain(args);
        } catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }
    }
}
