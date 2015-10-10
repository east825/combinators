package combinators;

import combinators.parsers.BaseParser;
import combinators.parsers.ParserResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Mikhail Golubev
 */
public class CombinatorsTestCase {
  protected static void assertTokensEqual(@NotNull Object expectedType, @NotNull String expectedText, @Nullable Token actualToken) {
    assertNotNull(actualToken);
    assertEquals(String.format("Wrong token type: expected %s, got %s", expectedType, actualToken.getType()),
                 expectedType, actualToken.getType());
    assertEquals(String.format("Wrong token text: expected '%s', got '%s'", expectedText, actualToken.getText()),
                 expectedText, actualToken.getText());
  }

  @Nullable
  protected static <T> T parse(@NotNull BaseParser<T> parser, @NotNull FluentLexer lexer, @NotNull String text) {
    final ParserResult<T> result = parser.parse(lexer.start(text));
    return result.getResult();
  }
}
