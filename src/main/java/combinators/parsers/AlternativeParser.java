package combinators.parsers;

import combinators.TokenStream;
import org.jetbrains.annotations.NotNull;

/**
 * @author Mikhail Golubev
 */
class AlternativeParser<T> extends Parser<T> {
  private final BaseParser<? extends T> myFirst, mySecond;

  public AlternativeParser(@NotNull BaseParser<? extends T> first, BaseParser<? extends T> second) {
    myFirst = first;
    mySecond = second;
  }

  @SuppressWarnings("unchecked")
  @NotNull
  @Override
  public ParserResult<T> parse(@NotNull TokenStream tokens) {
    try {
      return (ParserResult<T>)myFirst.parse(tokens);
    }
    catch (ParserException ignored) {

    }
    return (ParserResult<T>)mySecond.parse(tokens);
  }

  @Override
  public void accept(@NotNull ParserVisitor visitor) {
    visitor.visitAlternativeParser(this);
  }
}
