package pv260.refactoring.regexfractals;

import java.awt.Color;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        Matrix<Color> colorizedGrid = colorize(grid, colorizer);
        prepareOutputFolder();
        new AsciiImageWriter().write(colorizedGrid,
                                     parsedArgs.pixelSize(),
                                     DEFAULT_OUTPUT_DIRECTORY.resolve(DEFAULT_OUTPUT_FILE_ASCII));
        new PngImageWriter().write(colorizedGrid,
                                   parsedArgs.pixelSize(),
                                   DEFAULT_OUTPUT_DIRECTORY.resolve(DEFAULT_OUTPUT_FILE_PNG));
    }

    static Matrix<Color> colorize(FractalGrid grid, RegexColorizer colorizer) {
        Matrix<Color> colorGrid = new ArrayBackedMatrix<>(grid.side(), grid.side());
        for (int row = 0; row < grid.side(); row++) {
            for (int col = 0; col < grid.side(); col++) {
                Color pixelColor = colorizer.colorForSignature(grid.signatureOf(col, row));
                colorGrid.set(pixelColor, col, row);
            }
        }
        return colorGrid;
    }

    static void prepareOutputFolder() throws IOException {
        Files.createDirectories(DEFAULT_OUTPUT_DIRECTORY);
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
