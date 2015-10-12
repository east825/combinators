package combinators.parsers;

import combinators.TokenStream;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * @author Mikhail Golubev
 */
class MapParser<T1, T2> extends Parser<T2> {
  private final Function<T1, T2> myFunction;
  private final BaseParser<T1> myParser;

  @SuppressWarnings("unchecked")
  public MapParser(@NotNull BaseParser<T1> parser, @NotNull Function<? super T1, ? extends T2> function) {
    myParser = parser;
    myFunction = (Function<T1, T2>)function;
  }

  @NotNull
  @Override
  public ParserResult<T2> parse(@NotNull TokenStream tokens) throws ParserException {
    final ParserResult<T1> parsed = myParser.parse(tokens);
    return new ParserResult<>(myFunction.apply(parsed.getResult()), parsed.getRemainingTokens());
  }

  @Override
  public void accept(@NotNull ParserVisitor visitor) {
    visitor.visitMapParser(this);
  }

  @NotNull
  public BaseParser<T1> getParser() {
    return myParser;
  }

  @NotNull
  public Function<? super T1, ? extends T2> getFunction() {
    return myFunction;
  }
}
