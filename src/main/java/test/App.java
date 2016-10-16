package test;

import com.jayway.jsonpath.JsonPath;
import java.util.regex.Pattern;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        SimpleJsonPath p = SimpleJsonPath.compile("base.contacts.email");

        SimpleJsonPath p2 = SimpleJsonPath.compile("$..book[?(@.price <= $['expensive'])]");
        
        
        System.out.println(p);
        System.out.println(p2);
        
        
    }
}

class SimpleJsonPath {
  
  private final JsonPath jsonPath;
  
  private SimpleJsonPath(JsonPath jsonPath) {
    this.jsonPath = jsonPath;
  }
  
  public String toString() {
    return jsonPath
      .getPath()
      .replaceAll("^" + Pattern.quote("$['"), "")
      .replaceAll(Pattern.quote("']") + "$", "")
      .replace("']['", ".");
  }
  
  public static SimpleJsonPath compile(String s) {
    
    s = s.trim();
    
    if (s.isEmpty()) {
      throw new RuntimeException("argument is empty");
    }
    
    SimpleJsonPathValidator validator = new SimpleJsonPathValidator();
    
    validator.validate();
    
    return new SimpleJsonPath(JsonPath.compile(s));
  }
}

class SimpleJsonPathValidator {

  private State state = State.BEGIN;
  
  public void validate() {
    
    char[] chars = s.toCharArray();
    
    String c;
    
    for (int i = 0, n = chars.length; i < n; i++) {
    
      c = Character.toString(chars[i]));
      
      switch (state) {
        case BEGIN:
          Pattern p = Pattern.compile("[\\p{Alnum}_]");
          System.out.println(p.matcher(c.matches()));
          break;
        
        case ALNUM:
          break;
      }
    }
  }
  
  private enum State {
    BEGIN, ALNUM, DOT
  }
}
