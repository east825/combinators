package combinators.parsers;

import combinators.TokenStream;
import combinators.util.Pair;
import org.jetbrains.annotations.NotNull;

/**
 * @author Mikhail Golubev
 */
public abstract class SequenceParser<T1, T2, R> extends Parser<R> {
  protected final BaseParser<T1> myFirst;
  protected final BaseParser<T2> mySecond;

  @SuppressWarnings("unchecked")
  public SequenceParser(@NotNull BaseParser<? extends T1> first, @NotNull BaseParser<? extends T2> second) {
    myFirst = (BaseParser<T1>)first;
    mySecond = (BaseParser<T2>)second;
  }

  @NotNull
  public BaseParser<T1> getFirst() {
    return myFirst;
  }

  @NotNull
  public BaseParser<T2> getSecond() {
    return mySecond;
  }

  @Override
  public void accept(@NotNull ParserVisitor visitor) {
    visitor.visitSequenceParser(this);
  }


  public static class MatchBoth<T1, T2> extends SequenceParser<T1, T2, Pair<T1, T2>> {
    public MatchBoth(@NotNull BaseParser<? extends T1> first, BaseParser<? extends T2> second) {
      super(first, second);
    }

    @NotNull
    @Override
    public ParserResult<Pair<T1, T2>> parse(@NotNull TokenStream tokens) throws ParserException {
      final ParserResult<T1> parsed1 = myFirst.parse(tokens);
      final ParserResult<T2> parsed2 = mySecond.parse(parsed1.getRemainingTokens());
      final Pair<T1, T2> combinedResult = Pair.of(parsed1.getResult(), parsed2.getResult());
      return new ParserResult<>(combinedResult, parsed2.getRemainingTokens());
    }
  }

  public static class SkipThenMatch<T1, T2> extends SequenceParser<T1, T2, T2>{
    public SkipThenMatch(@NotNull BaseParser<? extends T1> skipped, BaseParser<? extends T2> other) {
      super(skipped, other);
    }
  
    @NotNull
    @Override
    public ParserResult<T2> parse(@NotNull TokenStream tokens) throws ParserException {
      final ParserResult<?> parsed1 = myFirst.parse(tokens);
      final ParserResult<T2> parsed2 = mySecond.parse(parsed1.getRemainingTokens());
      return new ParserResult<>(parsed2.getResult(), parsed2.getRemainingTokens());
    }
  }

  public static class MatchThenSkip<T1, T2> extends SequenceParser<T1, T2, T1>{
    public MatchThenSkip(@NotNull BaseParser<? extends T1> other, BaseParser<? extends T2> skipped) {
      super(other, skipped);
    }
  
    @NotNull
    @Override
    public ParserResult<T1> parse(@NotNull TokenStream tokens) throws ParserException {
      final ParserResult<T1> parsed1 = myFirst.parse(tokens);
      final ParserResult<?> parsed2 = mySecond.parse(parsed1.getRemainingTokens());
      return new ParserResult<>(parsed1.getResult(), parsed2.getRemainingTokens());
    }
  }
}
