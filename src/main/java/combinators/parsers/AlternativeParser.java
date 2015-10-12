package combinators.parsers;

import combinators.TokenStream;
import org.jetbrains.annotations.NotNull;

/**
 * @author Mikhail Golubev
 */
class AlternativeParser<T> extends Parser<T> {
  private final BaseParser<T> myFirst, mySecond;

  @SuppressWarnings("unchecked")
  public AlternativeParser(@NotNull BaseParser<? extends T> first, BaseParser<? extends T> second) {
    myFirst = (BaseParser<T>)first;
    mySecond = (BaseParser<T>)second;
  }

  @NotNull
  @Override
  public ParserResult<T> parse(@NotNull TokenStream tokens) {
    try {
      return myFirst.parse(tokens);
    }
    catch (ParserException ignored) {
    }
    return mySecond.parse(tokens);
  }

  @Override
  public void accept(@NotNull ParserVisitor visitor) {
    visitor.visitAlternativeParser(this);
  }

  @NotNull
  public BaseParser<T> getFirst() {
    return myFirst;
  }

  @NotNull
  public BaseParser<T> getSecond() {
    return mySecond;
  }
}
