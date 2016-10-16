package test;

import org.junit.Test;
import test.exception.EmptyJsonPathException;
import test.exception.UnexpectedCharacter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class SimpleJsonPathTest
{
    @Test
    public void serializeInDotNotation()
    {
        String jsonPathString = "this.is.a.valid.json_path";
        SimpleJsonPath jsonPath = new SimpleJsonPath(jsonPathString);
        assertThat("the serialized string should be equal to the given one", jsonPath.toString(), equalTo(jsonPathString));
    }

    @Test
    public void serializeTrimmed()
    {
        String jsonPathStringWithSpacesAfter = "spaces.after.path   ";
        SimpleJsonPath jsonPath1 = new SimpleJsonPath(jsonPathStringWithSpacesAfter);
        assertThat("the serialized string should be trimmed", jsonPath1.toString(), equalTo("spaces.after.path"));

        String jsonPathStringWithSpacesBefore = "   spaces.before.path";
        SimpleJsonPath jsonPath2 = new SimpleJsonPath(jsonPathStringWithSpacesBefore);
        assertThat("the serialized string should be trimmed", jsonPath2.toString(), equalTo("spaces.before.path"));
    }

    @Test(expected = EmptyJsonPathException.class)
    public void emptyJsonPath() {
        new SimpleJsonPath("");
    }

    @Test(expected = UnexpectedCharacter.class)
    public void acceptOnlySimplePaths() {
        new SimpleJsonPath("store.book[?(@.price < 10)]");
    }

    @Test(expected = UnexpectedCharacter.class)
    public void pathShouldNotStartWithDollar() {
        new SimpleJsonPath("$.store.book");
    }

    @Test(expected = UnexpectedCharacter.class)
    public void pathShouldNotContacinSpaces() {
        new SimpleJsonPath("store.book with.a.space");
    }
}
