package combinators;

import combinators.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author Mikhail Golubev
 */
public abstract class BaseParser<T> implements Parser<T> {

  @NotNull
  public UsefulParser<T> or(@NotNull final BaseParser<? extends T> other) {
    return new UsefulParser<T>() {
      @NotNull
      @Override
      public ParserResult<T> parse(@NotNull TokenStream tokens) {
        try {
          return BaseParser.this.parse(tokens);
        }
        catch (ParserException ignored) {
          
        }
        //noinspection unchecked
        return (ParserResult<T>)other.parse(tokens);
      }
    };
  }

  @NotNull
  public UsefulParser<List<T>> repeated(final int minTimes, final int maxTimes) {
    return new UsefulParser<List<T>>() {
      @NotNull
      @Override
      public ParserResult<List<T>> parse(@NotNull TokenStream tokens) throws ParserException {
        int count = 0;
        final List<T> results = new ArrayList<>();
        TokenStream remaining = tokens;
        try {
          for (/*empty*/; count < maxTimes; count++) {
            final ParserResult<T> parsed = BaseParser.this.parse(tokens);
            results.add(parsed.getResult());
            remaining = parsed.getRemainingTokens();
          }
        }
        catch (ParserException e) {
          if (count < minTimes) {
            final String msg = String.format("Parser %s was supposed to match at least %d times (maximum %d)",
                                             BaseParser.this, minTimes, maxTimes);
            throw new ParserException(msg, remaining.getOffset());
          }
        }
        return new ParserResult<>(results, remaining);
      }
    };
  }

  @NotNull
  public UsefulParser<T> optional() {
    return new UsefulParser<T>() {
      @NotNull
      @Override
      public ParserResult<T> parse(@NotNull TokenStream tokens) throws ParserException {
        try {
          return BaseParser.this.parse(tokens);
        }
        catch (ParserException ignored) {
        }
        return new ParserResult<>(null, tokens);
      }
    }; 
  }
  
  @NotNull
  public <T2> UsefulParser<T2> map(@NotNull Function<? super T, ? extends T2> function) {
    return new UsefulParser<T2>() {
      @NotNull
      @Override
      public ParserResult<T2> parse(@NotNull TokenStream tokens) throws ParserException {
        final ParserResult<T> parsed = BaseParser.this.parse(tokens);
        return new ParserResult<>(function.apply(parsed.getResult()), parsed.getRemainingTokens());
      }
    };
  }
}
