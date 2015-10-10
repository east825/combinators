package combinators.parsers;

import combinators.TokenStream;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * @author Mikhail Golubev
 */
class MapParser<T1, T2> extends Parser<T2> {
  private final Function<? super T1, ? extends T2> myFunction;
  private final BaseParser<T1> myParser;

  public MapParser(@NotNull BaseParser<T1> parser, @NotNull Function<? super T1, ? extends T2> function) {
    myParser = parser;
    myFunction = function;
  }

  @NotNull
  @Override
  public ParserResult<T2> parse(@NotNull TokenStream tokens) throws ParserException {
    final ParserResult<T1> parsed = myParser.parse(tokens);
    return new ParserResult<>(myFunction.apply(parsed.getResult()), parsed.getRemainingTokens());
  }
}
