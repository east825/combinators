package combinators;

import org.jetbrains.annotations.NotNull;

/**
 * @author Mikhail Golubev
 */
public interface Parser<T> {
  @NotNull
  ParserResult<T> parse(@NotNull TokenStream tokens) throws ParserException;
}
