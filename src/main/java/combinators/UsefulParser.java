package combinators;

import combinators.util.Pair;
import org.jetbrains.annotations.NotNull;

/**
 * @author Mikhail Golubev
 */
public abstract class UsefulParser<T> extends BaseParser<T> {

  @NotNull
  public <T2> UsefulParser<Pair<T, T2>> then(@NotNull final UsefulParser<? extends T2> other) {
    return new UsefulParser<Pair<T,T2>>() {
      @NotNull
      @Override
      public ParserResult<Pair<T, T2>> parse(@NotNull TokenStream tokens) throws ParserException {
        final ParserResult<T> parsed1 = UsefulParser.this.parse(tokens);
        final ParserResult<? extends T2> parsed2 = other.parse(parsed1.getRemainingTokens());
        final Pair<T, T2> combinedResult = Pair.of(parsed1.getResult(), parsed2.getResult());
        return new ParserResult<>(combinedResult, parsed2.getRemainingTokens());
      }
    };
  }

  @NotNull
  public UsefulParser<T> then(@NotNull final SkipParser<?> skipped) {
    return new UsefulParser<T>() {
      @NotNull
      @Override
      public ParserResult<T> parse(@NotNull TokenStream tokens) throws ParserException {
        final ParserResult<T> parsed1 = UsefulParser.this.parse(tokens);
        final ParserResult<?> parsed2 = skipped.parse(parsed1.getRemainingTokens());
        return new ParserResult<>(parsed1.getResult(), parsed2.getRemainingTokens());
      }
    };
  }

}
