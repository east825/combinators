package combinators.parsers;

import combinators.TokenStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Mikhail Golubev
 */
public class ParserResult<T> {
  private final TokenStream myRemainingTokens;
  private final T myResult;

  public ParserResult(@Nullable T result, @NotNull TokenStream remainingTokens) {
    myRemainingTokens = remainingTokens;
    myResult = result;
  }

  @NotNull
  public TokenStream getRemainingTokens() {
    return myRemainingTokens;
  }

  @Nullable
  public T getResult() {
    return myResult;
  }
}
