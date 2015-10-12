package combinators.parsers;

import combinators.util.Pair;
import org.jetbrains.annotations.NotNull;

/**
 * @author Mikhail Golubev
 */
public abstract class Parser<T> extends BaseParser<T> {

  @NotNull
  public <T2> Parser<Pair<T, T2>> then(@NotNull Parser<? extends T2> other) {
    return new ThenParser<>(this, other);
  }

  @NotNull
  public Parser<T> then(@NotNull SkipParser<?> skipped) {
    return new ThenSkipParser<>(this, skipped);
  }
}
