package combinators.parsers;

import combinators.TokenStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Mikhail Golubev
 */
public class ForwardParser<T> extends Parser<T> {
  private Parser<? extends T> myDelegate = null;

  public void define(@NotNull Parser<? extends T> definition) {
    myDelegate = definition;
  }

  @NotNull
  @Override
  public ParserResult<T> parse(@NotNull TokenStream tokens) throws ParserException {
    //noinspection unchecked
    return (ParserResult<T>)myDelegate.parse(tokens);
  }

  @Override
  public void accept(@NotNull ParserVisitor visitor) {
    visitor.visitForwardParser(this);
  }

  @Nullable
  public Parser<? extends T> getDelegate() {
    return myDelegate;
  }
}
