package combinators.parsers;

import combinators.TokenStream;
import combinators.util.Pair;
import org.jetbrains.annotations.NotNull;

/**
 * @author Mikhail Golubev
 */
public class SkipParser<T> extends BaseParser<T> {
  private BaseParser<T> myParser = null;

  protected SkipParser() {
    // for EofParser
  }

  public SkipParser(@NotNull BaseParser<T> other) {
    myParser = other;
  }

  @NotNull
  @Override
  public ParserResult<T> parse(@NotNull TokenStream tokens) throws ParserException {
    return myParser.parse(tokens);
  }

  @NotNull
  public <T2> Parser<T2> then(@NotNull Parser<? extends T2> other) {
    return new SkipThenParser<>(this, other);
  }

  @NotNull
  public <T2> SkipParser<Pair<T, T2>> then(@NotNull SkipParser<? extends T2> skipped) {
    return new SkipParser<>(new ThenParser<>(this, skipped));
  }

  @Override
  public void accept(@NotNull ParserVisitor visitor) {
    visitor.visitSkipParser(this);
  }
}
