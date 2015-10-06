package combinators;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author Mikhail Golubev
 */
public class Parsers {
  private Parsers() {
  }
  
  public static class TokenParser extends UsefulParser<Token> {
    private final Object myExpectedType;
    private final String myExpectedText;

    public TokenParser(@NotNull Object expectedType, @Nullable String expectedText) {
      myExpectedType = expectedType;
      myExpectedText = expectedText;
    }

    @NotNull
    @Override
    public ParserResult<Token> parse(@NotNull TokenStream tokens) throws ParserException {
      final Token token = tokens.getToken();
      if (token == null) {
        throw new ParserException("Unexpected end of file", tokens.getOffset());
      }
      if (token.getType().equals(myExpectedType) && (myExpectedText == null || token.getText().equals(myExpectedText))) {
        return new ParserResult<>(token, tokens.advance());
      }
      final String msg = String.format("Expected %s, got %s (%s)", myExpectedType, token.getType(), token.getText());
      throw new ParserException(msg, token.getStartOffset());
    }
  }

  @NotNull
  public static UsefulParser<Token> token(@NotNull Object type) {
    return new TokenParser(type, null);
  }

  @NotNull
  public static UsefulParser<Token> token(@NotNull String type, @NotNull String text) {
    return new TokenParser(type, text);
  }

  @NotNull
  public static <T> UsefulParser<List<T>> many(@NotNull BaseParser<T> parser) {
    return parser.repeated(0, Integer.MAX_VALUE);
  }

  @NotNull
  public static <T> UsefulParser<List<T>> onePlus(@NotNull BaseParser<T> parser) {
    return parser.repeated(1, Integer.MAX_VALUE);
  }

  @NotNull
  public static <T> UsefulParser<T> maybe(@NotNull BaseParser<T> parser) {
    return parser.optional();
  }

  @NotNull
  public static SkipParser<?> eof() {
    return new SkipParser<>(new BaseParser<Object>() {
      @NotNull
      @Override
      public ParserResult<Object> parse(@NotNull TokenStream tokens) throws ParserException {
        final Token token = tokens.getToken();
        if (token == null) {
          return new ParserResult<>(null, tokens);
        }
        throw new ParserException("Expected end of file, got " + token.getText(), token.getStartOffset());
      }
    });
  }

  @NotNull
  public static <T> ForwardParser<T> forwarded() {
    return new ForwardParser<>();
  }

  @NotNull
  public static <T> SkipParser<T> skip(@NotNull BaseParser<T> parser) {
    return new SkipParser<>(parser);
  }
}
