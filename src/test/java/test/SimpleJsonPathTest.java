package test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.jayway.jsonpath.InvalidPathException;
import java.io.IOException;
import org.junit.Test;
import test.exception.EmptyJsonPathException;
import test.exception.UnexpectedCharacter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class SimpleJsonPathTest {

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

    @Test(expected = InvalidPathException.class)
    public void invalidTerminator() {
        new SimpleJsonPath("this.is.not.valid.");
    }

    @Test(expected = UnexpectedCharacter.class)
    public void pathShouldNotCountainDoubleDots() {
        new SimpleJsonPath("this.is.not..valid");
    }

    @Test
    public void deserialization() throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        SimpleJsonObject simpleJsonObject = objectMapper.readValue(SimpleJsonPathTest.class.getResource("/aSimpleJsonObject.json"), SimpleJsonObject.class);

        assertThat("the deserialized object should not be null", simpleJsonObject.getPath(), notNullValue());
        assertThat("the deserialized path should be like the input string", simpleJsonObject.getPath().toString(), equalTo("this.is.a.json.path"));
    }

    @Test
    public void serialization() throws JsonProcessingException {

        SimpleModule module = new SimpleModule();
        module.addSerializer(SimpleJsonPath.class, new ToStringSerializer());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(module);

        SimpleJsonObject simpleJsonObject = new SimpleJsonObject();
        simpleJsonObject.setPath(new SimpleJsonPath("this.is.a.json.path"));

        String serializedObject = objectMapper.writeValueAsString(simpleJsonObject);

        assertThat("the path should have been serialized as the input string", serializedObject, equalTo("{\"path\":\"this.is.a.json.path\"}"));
    }
}
