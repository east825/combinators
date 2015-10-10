package combinators.parsers;

import combinators.TokenStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

/**
 * @author Mikhail Golubev
 */
public abstract class BaseParser<T> {
  private String myName = null;

  @NotNull
  public abstract ParserResult<T> parse(@NotNull TokenStream tokens) throws ParserException;

  @NotNull
  public final Parser<T> or(@NotNull final BaseParser<? extends T> other) {
    return new AlternativeParser<>(this, other);
  }

  @NotNull
  public final Parser<List<T>> repeated(int minTimes, int maxTimes) {
    return new RepeatParser<>(this, maxTimes, minTimes);
  }

  @NotNull
  public final Parser<T> optional() {
    return new OptionalParser<>(this); 
  }

  @NotNull
  public final <T2>  Parser<T2> map(@NotNull Function<? super T, ? extends T2> function) {
    return new MapParser<>(this, function);
  }

  @NotNull
  public final BaseParser<T> named(@NotNull String name) {
    myName = name;
    return this;
  }

  @Nullable
  public String getName() {
    return myName;
  }
}
