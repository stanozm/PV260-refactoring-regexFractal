package pv260.refactoring.regexfractals;

import java.awt.Color;
import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;
import java.util.regex.Pattern;

/**
 * Paint WHITE if the pattern matches, else BLACK
 */
public class RegexColorizer {

    private final Pattern regex;

    public RegexColorizer(Pattern regex) {
        this.regex = regex;
    }

	public Color colorForSignature(String signature) {
		return this.regex.matcher(signature).matches() ? WHITE : BLACK;
	}
}
