package combinators;

import org.jetbrains.annotations.NotNull;

/**
 * @author Mikhail Golubev
 */
public class ForwardParser<T> extends UsefulParser<T> {
  private UsefulParser<T> myDelegate = null;

  public void define(@NotNull UsefulParser<T> definition) {
    myDelegate = definition;
  }

  @NotNull
  @Override
  public ParserResult<T> parse(@NotNull TokenStream tokens) throws ParserException {
    return myDelegate.parse(tokens);
  }
}
