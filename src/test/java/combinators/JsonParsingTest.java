package combinators;

import combinators.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

import static combinators.JsonParsingTest.TokenType.*;
import static combinators.Parsers.*;
import static org.junit.Assert.assertEquals;

/**
 * @author Mikhail Golubev
 */
public class JsonParsingTest extends CombinatorsTestCase {
  enum TokenType {
    NULL_KEYWORD,
    TRUE_KEYWORD,
    FALSE_KEYWORD,
    L_BRACE,
    R_BRACE,
    L_BRACKET,
    R_BRACKET,
    NUMBER_LITERAL,
    STRING_LITERAL,
    COMMA,
    COLON
  }

  private static SimpleLexer myLexer;
  private static Parser<Object> myGrammar;

  @BeforeClass
  public static void createLexer() {
    myLexer = SimpleLexer.builder()
      .token("null", NULL_KEYWORD)
      .token("true", TRUE_KEYWORD)
      .token("false", FALSE_KEYWORD)
      .token("\\{", L_BRACE)
      .token("\\}", R_BRACE)
      .token("\\[", L_BRACKET)
      .token("\\]", R_BRACKET)
      .token(",", COMMA)
      .token(":", COLON)
      .token("\"(\\\\[\"\\\\/bfnrt]|\\\\u[A-Fa-f0-9]{4}|[^\\\\\"])*\"", STRING_LITERAL)
      .token("-?(0|[1-9][0-9]*)(\\.[0-9]*)?([eE][+-]?[0-9]*)?", NUMBER_LITERAL)
      .whitespaceToken("\\s+")
      .build();
  }

  @BeforeClass
  public static void createGrammar() {
    final UsefulParser<Boolean> trueKeyword = token(TRUE_KEYWORD).map(token -> Boolean.TRUE);
    final UsefulParser<Boolean> falseKeyword = token(FALSE_KEYWORD).map(token -> Boolean.FALSE);
    final UsefulParser<Object> nullKeyword = token(NULL_KEYWORD).map(token -> null);
    final UsefulParser<String> string = token(STRING_LITERAL).map(token -> {
      final String text = token.getText();
      return text.substring(1, text.length() - 1);
    });
    final UsefulParser<Integer> number = token(NUMBER_LITERAL).map(token -> Integer.valueOf(token.getText()));
    final UsefulParser<Object> atom = nullKeyword
      .or(trueKeyword)
      .or(falseKeyword)
      .or(string)
      .or(number);

    final ForwardParser<Object> value = forwarded();
    final UsefulParser<List<Object>> commaSeparated = value.then(many(skip(token(COMMA)).then(value))).map(JsonParsingTest::join);
    final UsefulParser<List<Object>> array = skip(token(L_BRACKET))
      .then(maybe(commaSeparated).map(JsonParsingTest::notEmpty))
      .then(skip(token(R_BRACKET)));
    final UsefulParser<Pair<String, Object>> keyValue = string.then(skip(token(COLON))).then(value);
    final UsefulParser<List<Pair<String, Object>>> colonSeparated = keyValue.then(many(skip(token(COLON)).then(keyValue))).map(JsonParsingTest::join);
    final UsefulParser<List<Pair<String, Object>>> object = skip(token(L_BRACE))
      .then(maybe(colonSeparated).map(JsonParsingTest::notEmpty))
      .then(skip(token(R_BRACE)));
    value.define(atom
                   .or(array)
                   .or(object));
    myGrammar = value;
  }

  @NotNull
  private static <T> List<T> join(@NotNull Pair<T, List<T>> pair) {
    final List<T> result = new ArrayList<>();
    result.add(pair.getFirst());
    result.addAll(pair.getSecond());
    return result;
  }

  @NotNull
  private static <T> List<T> notEmpty(@Nullable List<T> result) {
    return result == null ? Collections.<T>emptyList() : result;
  }

  @Test
  public void testParseAtoms() {
    assertEquals(null, parse("null"));
    assertEquals(42, parse("42"));
    assertEquals("str", parse("\"str\""));
    assertEquals(true, parse("true"));
    assertEquals(false, parse("false"));
  }

  @Test
  public void testParseArrays() {
    assertEquals(Collections.emptyList(), parse("[]"));
    assertEquals(Collections.singletonList(1), parse("[1]"));
    assertEquals(Arrays.asList(1, 2), parse("[1, 2]"));
    assertEquals(Collections.singletonList(Collections.singletonList(Collections.emptyList())), parse("[[[]]]"));
  }

  @Nullable
  private static Object parse(@NotNull String text) {
    return parse(myGrammar, myLexer, text);
  }
}
