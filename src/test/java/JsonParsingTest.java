import combinators.Parser;
import combinators.Parsers;
import combinators.SimpleLexer;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Mikhail Golubev
 */
public class JsonParsingTest {
  enum TokenType {
    NULL_KEYWORD,
    TRUE_KEYWORD,
    FALSE_KEYWORD,
    L_BRACE,
    R_BRACE,
    L_BRACKET,
    R_BRACKET,
    NUMBER_LITERAL,
    STRING_LITERAL,
    COMMA,
    COLON
  }

  private static SimpleLexer myLexer;
  private static Parser<Object> myGrammar;

  public static void createLexer() {
    myLexer = SimpleLexer.builder()
      .token("null", TokenType.NULL_KEYWORD)
    .build()
  }

  @BeforeClass
  public void createGrammar() {

  }

}
