package test.exception;

/**
 * Thrown when creating a {@link test.SimpleJsonPath} from an empty string.
 */
public class EmptyJsonPathException extends SimpleJsonPathException {

    public EmptyJsonPathException() {
        super("empty JSON Path");
    }
}
