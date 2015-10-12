package combinators.parsers;

import combinators.TokenStream;
import org.jetbrains.annotations.NotNull;

/**
 * @author Mikhail Golubev
 */
public class ThenSkipParser<T1, T2> extends Parser<T1>{
  private final BaseParser<? extends T1> myOther;
  private final BaseParser<? extends T2> mySkipped;

  public ThenSkipParser(@NotNull BaseParser<? extends T1> other, BaseParser<? extends T2> skipped) {
    myOther = other;
    mySkipped = skipped;
  }

  @NotNull
  @Override
  public ParserResult<T1> parse(@NotNull TokenStream tokens) throws ParserException {
    final ParserResult<? extends T1> parsed1 = myOther.parse(tokens);
    final ParserResult<?> parsed2 = mySkipped.parse(parsed1.getRemainingTokens());
    return new ParserResult<>(parsed1.getResult(), parsed2.getRemainingTokens());
  }

  @Override
  public void accept(@NotNull ParserVisitor visitor) {
    visitor.visitThenSkipParser(this);
  }
}
