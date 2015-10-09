package combinators;

import combinators.util.Pair;
import org.jetbrains.annotations.NotNull;

/**
 * @author Mikhail Golubev
 */
public final class SkipParser<T> extends BaseParser<T> {
  private final BaseParser<T> myOtherParser;

  public SkipParser(@NotNull BaseParser<T> other) {
    myOtherParser = other;
  }

  @NotNull
  @Override
  public ParserResult<T> parse(@NotNull TokenStream tokens) throws ParserException {
    return myOtherParser.parse(tokens);
  }

  @NotNull
  public <T2> Parser<T2> then(@NotNull final Parser<? extends T2> other) {
    return new Parser<T2>() {
      @NotNull
      @Override
      public ParserResult<T2> parse(@NotNull TokenStream tokens) throws ParserException {
        final ParserResult<?> parsed1 = SkipParser.this.parse(tokens);
        final ParserResult<? extends T2> parsed2 = other.parse(parsed1.getRemainingTokens());
        return new ParserResult<>(parsed2.getResult(), parsed2.getRemainingTokens());
      }
    };

  }

  @NotNull
  public <T2> SkipParser<Pair<T, T2>> then(@NotNull final SkipParser<T2> skipped) {
    final BaseParser<Pair<T, T2>> parser = new BaseParser<Pair<T, T2>>() {
      @NotNull
      @Override
      public ParserResult<Pair<T, T2>> parse(@NotNull TokenStream tokens) throws ParserException {
        final ParserResult<T> parsed1 = SkipParser.this.parse(tokens);
        final ParserResult<? extends T2> parsed2 = skipped.parse(parsed1.getRemainingTokens());
        final Pair<T, T2> combinedResult = Pair.of(parsed1.getResult(), parsed2.getResult());
        return new ParserResult<>(combinedResult, parsed2.getRemainingTokens());
      }
    };
    return new SkipParser<>(parser);
  }
}
