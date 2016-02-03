package pv260.refactoring.regexfractals;

import java.awt.Color;
import java.io.BufferedWriter;
import static java.lang.String.format;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;
import static java.util.regex.Pattern.compile;

public class RegexFractals {

    private static final Path DEFAULT_OUTPUT_DIRECTORY = Paths.get("output");
    private static final String DEFAULT_OUTPUT_FILE_ASCII = "fractal.txt";
    private static final String DEFAULT_OUTPUT_FILE_PNG = "fractal.png";

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

        RegexColorizer colorizer = new RegexColorizer(compile(parsedArgs.pattern()));
        FractalGrid grid = new FractalGrid(parsedArgs.gridSide(), 1);
        Color[][] colorizedGrid = colorize(grid, colorizer);
        prepareOutputFolder();
        outputAscii(colorizedGrid, parsedArgs.pixelSize());
        outputPng(colorizedGrid, parsedArgs.pixelSize());
    }

    static Color[][] colorize(FractalGrid grid, RegexColorizer colorizer) {
        Color[][] colorGrid = new Color[grid.side()][grid.side()];
        for (int i = 0; i < grid.side(); i++) {
            for (int j = 0; j < grid.side(); j++) {
                Color pixelColor = colorizer.colorForSignature(grid.signatureOf(i, j));
                colorGrid[j][i] = pixelColor;
            }
        }
        return colorGrid;
    }

    static void prepareOutputFolder() throws IOException {
        Files.createDirectories(DEFAULT_OUTPUT_DIRECTORY);
    }

    static void outputAscii(Color[][] colorizedGrid, int pixelSize) throws IOException {
        AsciiImageConverter asciiConverter = new AsciiImageConverter();
        try (BufferedWriter w = Files.newBufferedWriter(
                DEFAULT_OUTPUT_DIRECTORY.resolve(DEFAULT_OUTPUT_FILE_ASCII),
                Charset.forName("UTF-8"))) {
            w.write(asciiConverter.convert(colorizedGrid, pixelSize));
        }
    }

    static void outputPng(Color[][] colorizedGrid, int pixelSize) {
        PngImageConverter pngConverter = new PngImageConverter(
                DEFAULT_OUTPUT_DIRECTORY.resolve(DEFAULT_OUTPUT_FILE_PNG).toFile());
        pngConverter.convert(colorizedGrid, pixelSize);
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
