package pv260.refactoring.regexfractals;

import java.awt.Color;
import java.io.BufferedWriter;
import static java.lang.String.format;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RegexFractals {

    /**
     * regex must conform to this pattern to be valid
    */
    private static final Pattern VALID_REGEX = Pattern.compile("[\\[\\]().1234^\\\\*+|]+");

    private static final String DEFAULT_OUTPUT_ASCII = "output/fractal.txt";
    private static final String DEFAULT_OUTPUT_PNG = "output/fractal.png";

    //some nice inputs
    //.*1.*
    //.*(12|23|34|41).*
    //.*(13|31|24|42).*
    public static void doMain(String[] args) throws Exception {
        int gridSide;
        int pixelSize;
        String pattern;
        if (isPowerOfTwo(args[0])) {
            gridSide = Integer.valueOf(args[0]);
        } else {
            throw new IllegalArgumentException("Side must be a power of two number");
        }
        try{
            pixelSize = Integer.parseInt(args[1]);
            if(pixelSize <= 0){
                throw new IllegalArgumentException("Bad input");
            }
        } catch(NumberFormatException e){
            throw new IllegalArgumentException("Bad input");
        }
        if (isValidRegexPattern(args[2])) {
            pattern = args[2];
        } else {
            throw new IllegalArgumentException("Invalid pattern");
        }
        if (args.length != 3) {
            throw new IllegalArgumentException(format("Expected two arguments(grid side and pattern), got %s",
                                                      args.length));
        }
        AsciiImageConverter asciiConverter = new AsciiImageConverter();
        FractalGrid grid = new FractalGrid(gridSide, 1);
        Color[][] colorGrid = new Color[gridSide][gridSide];
        for (int i = 0; i < gridSide; i++) {
            for (int j = 0; j < gridSide; j++) {
                Color pixelColor = asciiConverter.colorFor(Pattern.compile(pattern), grid.signatureOf(i, j));
                colorGrid[j][i] = pixelColor;
            }
        }

        Files.createDirectories(Paths.get(DEFAULT_OUTPUT_ASCII).getParent());
        try (BufferedWriter w = Files.newBufferedWriter(Paths.get(DEFAULT_OUTPUT_ASCII), Charset.forName("UTF-8"))) {
            w.write(asciiConverter.convert(colorGrid, pixelSize));
        }

         PngImageConverter pngConverter = new PngImageConverter(new File(DEFAULT_OUTPUT_PNG));
        pngConverter.convert(colorGrid, pixelSize);
    }

    public static void main(String[] args) {
		try{
            doMain(args);
        } catch(Exception e){
            System.err.println(e);
            System.exit(1);
        }
	}

    /**
     * @return true if number is power of two, false otherwise
     */
    public static boolean isPowerOfTwo(String number) {
        int parsedNumber;
        try {
            parsedNumber = Integer.valueOf(number);
        } catch (NumberFormatException e) {
            System.out.println("Input " + number + " is not a number.");
            return false;
        }
        if (parsedNumber <= 1) {
            System.out.println("Number " + number + " is not power of two greater than 1");
            return false;
        }
        while (parsedNumber > 1) {
            if ((parsedNumber & 1) == 0) {
                parsedNumber = parsedNumber / 2;
            } else {
                System.out.println("Number " + number + " is not power of two");
                return false;
            }
        }
        return true;
    }

    /**
     * @return true if this application can work with the given regex pattern
     */
    public static boolean isValidRegexPattern(String regex) {
        Matcher validRegex = VALID_REGEX.matcher(regex);
        if (!validRegex.matches()) {
            System.out.println("Regex " + regex + " contains invalid characters.");
            return false;
        }
        try {
            Pattern.compile(regex);
        } catch (PatternSyntaxException e) {
            System.out.println("Input " + regex + " is not a valid regular expression.");
            return false;
        }
        return true;
    }
}
