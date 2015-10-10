package combinators.parsers;

import combinators.TokenStream;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikhail Golubev
 */
class RepeatParser<T> extends Parser<List<T>> {
  private final int myMaxTimes;
  private final int myMinTimes;
  private final BaseParser<T> myParser;

  public RepeatParser(@NotNull BaseParser<T> parser, int maxTimes, int minTimes) {
    myParser = parser;
    myMaxTimes = maxTimes;
    myMinTimes = minTimes;
  }

  @NotNull
  @Override
  public ParserResult<List<T>> parse(@NotNull TokenStream tokens) throws ParserException {
    final List<T> results = new ArrayList<>();
    TokenStream remaining = tokens;
    int count = 0;
    try {
      for (/*empty*/; count < myMaxTimes; count++) {
        final ParserResult<T> parsed = myParser.parse(remaining);
        results.add(parsed.getResult());
        remaining = parsed.getRemainingTokens();
      }
    }
    catch (ParserException e) {
      if (count < myMinTimes) {
        final String msg = String.format("Parser %s was supposed to match at least %d times (maximum %d)",
                                         myParser, myMinTimes, myMaxTimes);
        throw new ParserException(msg, remaining.getOffset());
      }
    }
    return new ParserResult<>(results, remaining);
  }
}
