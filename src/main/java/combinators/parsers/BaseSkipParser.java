package combinators.parsers;

import combinators.util.Pair;
import org.jetbrains.annotations.NotNull;

/**
 * @author Mikhail Golubev
 */
public abstract class BaseSkipParser<T> extends BaseParser<T> {
  @NotNull
  public <T2> Parser<T2> then(@NotNull Parser<? extends T2> other) {
    return new SkipThenParser<>(this, other);
  }

  @NotNull
  public <T2> BaseSkipParser<Pair<T,T2>> then(@NotNull BaseSkipParser<? extends T2> skipped) {
    return new SkipParser<>(new ThenParser<>(this, skipped));
  }
}
