package test.exception;


public class EmptyJsonPathException extends RuntimeException {

    public EmptyJsonPathException() {
        super("empty JSON Path");
    }
}
