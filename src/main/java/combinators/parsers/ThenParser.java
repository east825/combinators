package combinators.parsers;

import combinators.TokenStream;
import combinators.util.Pair;
import org.jetbrains.annotations.NotNull;

/**
 * @author Mikhail Golubev
 */
public class ThenParser<T1, T2> extends Parser<Pair<T1, T2>>{
  private final BaseParser<? extends T1> myFirst;
  private final BaseParser<? extends T2> mySecond;

  public ThenParser(@NotNull BaseParser<? extends T1> first, BaseParser<? extends T2> second) {
    myFirst = first;
    mySecond = second;
  }

  @NotNull
  @Override
  public ParserResult<Pair<T1, T2>> parse(@NotNull TokenStream tokens) throws ParserException {
    final ParserResult<? extends T1> parsed1 = myFirst.parse(tokens);
    final ParserResult<? extends T2> parsed2 = mySecond.parse(parsed1.getRemainingTokens());
    final Pair<T1, T2> combinedResult = Pair.of(parsed1.getResult(), parsed2.getResult());
    return new ParserResult<>(combinedResult, parsed2.getRemainingTokens());
  }

  @Override
  public void accept(@NotNull ParserVisitor visitor) {
    visitor.visitThenParser(this);
  }
}
