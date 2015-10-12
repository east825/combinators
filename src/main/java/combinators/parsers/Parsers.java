package combinators.parsers;

import combinators.Token;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

/**
 * @author Mikhail Golubev
 */
public class Parsers {

  private static final EofParser EOF_PARSER = new EofParser();

  private Parsers() {
  }

  @NotNull
  public static Parser<Token> token(@NotNull Object type) {
    return new TokenParser(type, null);
  }

  @NotNull
  public static Parser<Token> token(@NotNull String type, @NotNull String text) {
    return new TokenParser(type, text);
  }

  @NotNull
  public static <T> Parser<List<T>> many(@NotNull BaseParser<T> parser) {
    return parser.repeated(0, Integer.MAX_VALUE);
  }

  @NotNull
  public static <T> Parser<List<T>> onePlus(@NotNull BaseParser<T> parser) {
    return parser.repeated(1, Integer.MAX_VALUE);
  }

  @NotNull
  public static <T> Parser<T> maybe(@NotNull BaseParser<T> parser) {
    return parser.optional();
  }

  @NotNull
  public static SkipParser<?> eof() {
    return EOF_PARSER;
  }

  @NotNull
  public static <T> ForwardParser<T> forwarded() {
    return new ForwardParser<>();
  }

  @NotNull
  public static <T> SkipParser<T> skip(@NotNull BaseParser<T> parser) {
    return new SkipParser<>(parser);
  }

  public static final Function<Token, String> tokenText = Token::getText;
  public static final Function<Token, Object> tokenType = Token::getType;
}
