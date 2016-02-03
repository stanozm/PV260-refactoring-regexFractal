package pv260.refactoring.regexfractals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InvalidInputException extends Exception {

    private final List<String> causes;

    public InvalidInputException(List<String> causes) {
        super();
        this.causes = Collections.unmodifiableList(new ArrayList<>(causes));
    }

    public List<String> causes() {
        return this.causes;
    }
}
