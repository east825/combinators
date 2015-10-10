package combinators.parsers;

import combinators.TokenStream;
import org.jetbrains.annotations.NotNull;

/**
 * @author Mikhail Golubev
 */
class OptionalParser<T> extends Parser<T> {
  private final BaseParser<T> myParser;

  public OptionalParser(@NotNull BaseParser<T> parser) {
    myParser = parser;
  }

  @NotNull
  @Override
  public ParserResult<T> parse(@NotNull TokenStream tokens) throws ParserException {
    try {
      return myParser.parse(tokens);
    }
    catch (ParserException ignored) {
    }
    return new ParserResult<>(null, tokens);
  }
}
