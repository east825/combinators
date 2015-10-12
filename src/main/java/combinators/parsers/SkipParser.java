package combinators.parsers;

import combinators.TokenStream;
import org.jetbrains.annotations.NotNull;

/**
 * @author Mikhail Golubev
 */
public class SkipParser<T> extends BaseSkipParser<T> {
  private BaseParser<T> myParser = null;

  public SkipParser(@NotNull BaseParser<T> other) {
    myParser = other;
  }

  @NotNull
  @Override
  public ParserResult<T> parse(@NotNull TokenStream tokens) throws ParserException {
    return myParser.parse(tokens);
  }

  @Override
  public void accept(@NotNull ParserVisitor visitor) {
    visitor.visitSkipParser(this);
  }
}
