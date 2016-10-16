package test.exception;

/**
 * Thrown when an invalid character if found on {@link test.SimpleJsonPath} creation.
 */
public class UnexpectedCharacter extends SimpleJsonPathException {

    private final Character character;
    private final Integer position;

    public UnexpectedCharacter(Character character, Integer position) {
        super("unexpected character '" + character + "' at position " + position);
        this.character = character;
        this.position = position;
    }

    public Character getCharacter() {
        return character;
    }

    public Integer getPosition() {
        return position;
    }
}
