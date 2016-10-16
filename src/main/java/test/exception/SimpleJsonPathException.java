package test.exception;

/**
 * Generic exception thrown on invalid {@link test.SimpleJsonPath}.
 */
public abstract class SimpleJsonPathException extends RuntimeException {

    public SimpleJsonPathException(String message) {
        super(message);
    }
}
