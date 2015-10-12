package combinators.parsers;

import combinators.Token;
import combinators.TokenStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Mikhail Golubev
 */
public class TokenParser extends Parser<Token> {
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

  @Override
  public void accept(@NotNull ParserVisitor visitor) {
    visitor.visitTokenParser(this);
  }

  @NotNull
  public Object getExpectedType() {
    return myExpectedType;
  }

  @Nullable
  public String getExpectedText() {
    return myExpectedText;
  }
}
