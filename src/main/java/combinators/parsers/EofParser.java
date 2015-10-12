package combinators.parsers;

import combinators.Token;
import combinators.TokenStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Mikhail Golubev
 */
public class EofParser extends SkipParser<Object> {

  private static final String STANDARD_NAME = "<EOF>";

  @NotNull
  @Override
  public ParserResult<Object> parse(@NotNull TokenStream tokens) throws ParserException {
    final Token token = tokens.getToken();
    if (token == null) {
      return new ParserResult<>(null, tokens);
    }
    throw new ParserException("Expected end of file, got " + token.getText(), token.getStartOffset());
  }
  
  @Override
  public void accept(@NotNull ParserVisitor visitor) {
    visitor.visitEofParser(this);
  }

  @Nullable
  @Override
  public String getName() {
    return STANDARD_NAME;
  }
}
