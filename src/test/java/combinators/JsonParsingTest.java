package combinators;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import combinators.parsers.BaseParser;
import combinators.parsers.ForwardParser;
import combinators.parsers.Parser;
import combinators.parsers.SkipParser;
import combinators.util.Pair;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

import static combinators.JsonParsingTest.TokenType.*;
import static combinators.parsers.Parsers.*;
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

  private static FluentLexer myLexer;
  private static BaseParser<Object> myGrammar;

  @BeforeClass
  public static void createLexer() {
    myLexer = FluentLexer.builder()
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

  @SuppressWarnings("LocalCanBeFinal")
  @BeforeClass
  public static void createGrammar() {
    Parser<Boolean> trueKeyword = token(TRUE_KEYWORD).map(token -> Boolean.TRUE);
    Parser<Boolean> falseKeyword = token(FALSE_KEYWORD).map(token -> Boolean.FALSE);
    Parser<Object> nullKeyword = token(NULL_KEYWORD).map(token -> null);
    Parser<String> string = token(STRING_LITERAL).map(token -> {
      final String text = token.getText();
      return text.substring(1, text.length() - 1);
    });
    Parser<Integer> number = token(NUMBER_LITERAL).map(token -> Integer.valueOf(token.getText()));
    Parser<Object> atom = nullKeyword
      .or(trueKeyword)
      .or(falseKeyword)
      .or(string)
      .or(number);

    ForwardParser<Object> value = forwarded();
    Parser<List<Object>> array = commaSeparatedCollection(L_BRACKET, value, R_BRACKET);
    Parser<Pair<String, Object>> entry = string.then(op(COLON)).then(value);
    Parser<Map<String, Object>> object = commaSeparatedCollection(L_BRACE, entry, R_BRACE)
      .map(pairs -> {
        Map<String, Object> result = new LinkedHashMap<>();
        for (Pair<String, Object> pair : pairs) {
          result.put(pair.getFirst(), pair.getSecond());
        }
        return result;
      });
    value.define(atom
                   .or(array)
                   .or(object));
    myGrammar = value;
  }

  @NotNull
  private static <T> Parser<List<T>> commaSeparatedItems(@NotNull Parser<T> item) {
    return item.then(many(op(COMMA).then(item))).map(pair -> {
      final List<T> result = new ArrayList<>();
      result.add(pair.getFirst());
      //noinspection ConstantConditions
      result.addAll(pair.getSecond());
      return result;
    });
  }

  @NotNull
  private static <T> Parser<List<T>> commaSeparatedCollection(@NotNull TokenType openBrace,
                                                              @NotNull Parser<T> item,
                                                              @NotNull TokenType closeBrace) {
    return op(openBrace)
      .then(maybe(commaSeparatedItems(item)).map(xs -> xs == null ? Collections.<T>emptyList() : xs))
      .then(op(closeBrace));
  }

  @NotNull
  private static SkipParser<Token> op(@NotNull TokenType openBrace) {
    return skip(token(openBrace));
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

  @Test
  public void testParseObject() throws Exception {
    final Object parsed = parse("{\n" +
                                "  \"name\": \"Bob\",\n" +
                                "  \"age\": 27,\n" +
                                "  \"address\": {\n" +
                                "    \"street\": \"Sesame Street\",\n" +
                                "    \"building\": 17,\n" +
                                "    \"city\": \"New York\"\n" +
                                "  },\n" +
                                "  \"hobbies\": [\n" +
                                "    \"hiking\",\n" +
                                "    \"jogging\",\n" +
                                "    \"cycling\"\n" +
                                "  ]\n" +
                                "}");
    final Map<String, Object> expected = ImmutableMap.<String, Object>builder()
      .put("name", "Bob")
      .put("age", 27)
      .put("address", ImmutableMap.builder()
        .put("street", "Sesame Street")
        .put("building", 17)
        .put("city", "New York")
        .build())
      .put("hobbies", ImmutableList.of("hiking", "jogging", "cycling"))
      .build();
    assertEquals(expected, parsed);
  }

  @Nullable
  private static Object parse(@Language("JSON") @NotNull String text) {
    return parse(myGrammar, myLexer, text);
  }
}
