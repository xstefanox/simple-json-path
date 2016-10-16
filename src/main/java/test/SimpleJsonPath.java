package test;

import com.jayway.jsonpath.JsonPath;
import java.util.regex.Pattern;
import test.exception.EmptyJsonPathException;
import test.exception.UnexpectedCharacter;

/**
 * Implements a simple JSON Path accepting only plain paths in dot-notation. No operators or advanced JSON objet
 * properties navigation operators is accepted.
 */
class SimpleJsonPath {

    /**
     * Match alphanumeric characters plus the underscore.
     */
    private static final Pattern ALNUM = Pattern.compile("[\\p{Alnum}_]");

    /**
     * Matche a dot.
     */
    private static final Pattern DOT = Pattern.compile("\\.");

    /**
     * Match the beginning of a JSON Path in square notation.
     */
    private static final String SQUARE_NOTATION_BEGIN = "^" + Pattern.quote("$['");

    /**
     * Match the end of a JSON Path in square notation.
     */
    private static final String SQUARE_NOTATION_END = Pattern.quote("']") + "$";

    /**
     * Match the separator of a JSON Path in square notation.
     */
    private static final String SQUARE_NOTATION_SEPARATOR = "']['";

    /**
     * The JSON Path object to which each read/write operation is delegated.
     */
    private final JsonPath jsonPath;

    /**
     * Create a new {@link SimpleJsonPath} from a {@link String}.
     */
    public SimpleJsonPath(String jsonPath) {

        // normalize the string
        String s = jsonPath.trim();

        if (s.isEmpty()) {
            throw new EmptyJsonPathException();
        }

        // validate each character of the string

        State state = State.BEGIN;

        for (int i = 0; i < s.length(); i++) {

            Character c = s.charAt(i);
            String charString = Character.toString(c);

            switch (state) {

                case BEGIN:

                    // a path can only begin with an alphanumeric character

                    if (!ALNUM.matcher(charString).matches()) {
                        throw new UnexpectedCharacter(c, i);
                    }

                    state = State.ALNUM;

                    break;

                case ALNUM:

                    // an alphanumeric character can be followed by another alphanumeric character or a separator

                    if (ALNUM.matcher(charString).matches()) {
                        state = State.ALNUM;
                    } else if (DOT.matcher(charString).matches()) {
                        state = State.DOT;
                    } else {
                        throw new UnexpectedCharacter(c, i);
                    }

                    break;

                case DOT:

                    // a separator can only be followed by an alphanumeric character

                    if (!ALNUM.matcher(charString).matches()) {
                        throw new UnexpectedCharacter(c, i);
                    }

                    state = State.ALNUM;

                    break;
            }
        }

        // save the json path
        this.jsonPath = JsonPath.compile(jsonPath);
    }

    /**
     * Return a {@link String} representation of the JSON Path using the dot-notation.
     */
    public String toString() {
        return jsonPath
                .getPath()
                .replaceAll(SQUARE_NOTATION_BEGIN, "")
                .replaceAll(SQUARE_NOTATION_END, "")
                .replace(SQUARE_NOTATION_SEPARATOR, ".");
    }

    /**
     * Validation states.
     */
    private enum State {

        /**
         * No previous character, the validation has not begun.
         */
        BEGIN,

        /**
         * The previous character was alphanumeric.
         */
        ALNUM,

        /**
         * The previous character was a separator.
         */
        DOT
    }
}
