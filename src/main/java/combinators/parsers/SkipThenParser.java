package combinators.parsers;

import combinators.TokenStream;
import org.jetbrains.annotations.NotNull;

/**
 * @author Mikhail Golubev
 */
public class SkipThenParser<T1, T2> extends Parser<T2>{
  private final BaseParser<? extends T1> mySkipped;
  private final BaseParser<? extends T2> myOther;

  public SkipThenParser(@NotNull BaseParser<? extends T1> skipped, BaseParser<? extends T2> other) {
    mySkipped = skipped;
    myOther = other;
  }

  @NotNull
  @Override
  public ParserResult<T2> parse(@NotNull TokenStream tokens) throws ParserException {
    final ParserResult<?> parsed1 = mySkipped.parse(tokens);
    final ParserResult<? extends T2> parsed2 = myOther.parse(parsed1.getRemainingTokens());
    return new ParserResult<>(parsed2.getResult(), parsed2.getRemainingTokens());
  }

  @Override
  public void accept(@NotNull ParserVisitor visitor) {
    visitor.visitSkipThenParser(this);
  }
}