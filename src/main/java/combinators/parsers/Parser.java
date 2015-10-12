package combinators.parsers;

import combinators.util.Pair;
import org.jetbrains.annotations.NotNull;

/**
 * @author Mikhail Golubev
 */
public abstract class Parser<T> extends BaseParser<T> {

  @NotNull
  public <T2> Parser<Pair<T, T2>> then(@NotNull Parser<? extends T2> other) {
    return new SequenceParser.MatchBoth<>(this, other);
  }

  @NotNull
  public Parser<T> then(@NotNull BaseSkipParser<?> skipped) {
    return new SequenceParser.MatchThenSkip<>(this, skipped);
  }
}
