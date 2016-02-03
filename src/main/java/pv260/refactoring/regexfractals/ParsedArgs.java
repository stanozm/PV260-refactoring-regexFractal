package pv260.refactoring.regexfractals;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class ParsedArgs {

    /**
     * regex must conform to this pattern to be valid
     */
    private static final Pattern VALID_REGEX = Pattern.compile("[\\[\\]().1234^\\\\*+|]+");

    private int gridSide;
    private int pixelSize;
    private String pattern;

    public int gridSide() {
        return gridSide;
    }

    public int pixelSize() {
        return pixelSize;
    }

    public String pattern() {
        return pattern;
    }

    public void parse(String[] args) throws InvalidInputException {
        List<String> errors = new ArrayList<>();
        readGridSide(args[0], errors);
        readPixelSize(args[1], errors);
        readRegexPattern(args[2], errors);
        validateArgCount(args.length, errors);
        if (!errors.isEmpty()) {
            throw new InvalidInputException(errors);
        }
    }

    void validateArgCount(int count, List<String> errors) {
        if (count != 3) {
            errors.add("Expected three arguments(grid side, pixel size and pattern), got " + count);
        }
    }

    /**
     * validate the given argument is positive power of two number
     */
    void readGridSide(String sideArg, List<String> errors) {
        int sideNum;
        try {
            sideNum = Integer.parseInt(sideArg);
        } catch (NumberFormatException e) {
            errors.add("Input for grid " + sideArg + " is not a number.");
            return;
        }
        if (sideNum <= 1) {
            errors.add("Grid side must be >= 2, got " + sideArg);
            return;
        }
        if (!isPowerOfTwo(sideNum)) {
            errors.add("Grid side must be power of 2, got " + sideArg);
        }
        this.gridSide = sideNum;
    }

    /**
     * @return true if number is power of two, false otherwise
     */
    boolean isPowerOfTwo(int number) {
        if (number <= 0) {
            return false;
        }
        int remainder = number;
        while (remainder > 1) {
            if ((remainder & 1) == 0) {
                remainder /= 2;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * validate the given argument is positive power of two number
     */
    void readPixelSize(String pixelArg, List<String> errors) {
        int pixelNum;
        try {
            pixelNum = Integer.parseInt(pixelArg);
        } catch (NumberFormatException e) {
            errors.add("Input for pixel " + pixelArg + " is not a number.");
            return;
        }
        if (pixelNum < 1) {
            errors.add("Pixel size must be at least 1, got " + pixelArg);
            return;
        }
        this.pixelSize = pixelNum;
    }

    /**
     * validate if this application can work with the given regex pattern
     */
    void readRegexPattern(String regex, List<String> errors) {
        Matcher validRegex = VALID_REGEX.matcher(regex);
        if (!validRegex.matches()) {
            errors.add("Regex " + regex + " contains invalid characters.");
            return;
        }
        try {
            Pattern.compile(regex);
        } catch (PatternSyntaxException e) {
            errors.add("Input " + regex + " is not a valid regular expression.");
            return;
        }
        this.pattern = regex;
    }
}
