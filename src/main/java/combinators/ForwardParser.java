package combinators;

import org.jetbrains.annotations.NotNull;

/**
 * @author Mikhail Golubev
 */
public class ForwardParser<T> extends Parser<T> {
  private Parser<T> myDelegate = null;

  public void define(@NotNull Parser<T> definition) {
    myDelegate = definition;
  }

  @NotNull
  @Override
  public ParserResult<T> parse(@NotNull TokenStream tokens) throws ParserException {
    return myDelegate.parse(tokens);
  }
}
