package combinators;

import combinators.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;

import java.util.Arrays;

import static combinators.Parsers.*;
import static org.junit.Assert.assertEquals;

/**
 * @author Mikhail Golubev
 */
public class ParserTest extends CombinatorsTestCase {
  private static final FluentLexer ourLexer = FluentLexer.builder()
    .token("[A-Za-z_]+", "word")
    .token("\\d+", "number")
    .token("[+-]]", "sign")
    .whitespaceToken("\\s+")
    .build();

  private static final Parser<String> WORD = token("word").map(tokenText);
  private static final Parser<String> NUMBER = token("number").map(tokenText);
  private static final Parser<String> SIGN = token("sign").map(tokenText);
  
  
  @Test
  public void testThenParser() {
    assertEquals(Pair.of("foo", "bar"), parse(WORD.then(WORD), "foo  bar"));
    assertEquals(Pair.of(Pair.of("foo", "bar"), "baz"), parse(WORD.then(WORD).then(WORD), "foo  bar baz"));
    assertEquals(Pair.of("foo", Pair.of("bar", "baz")), parse(WORD.then(WORD.then(WORD)), "foo  bar baz"));
    assertEquals("foo", parse(WORD.then(skip(WORD)), "foo bar"));
    assertEquals("bar", parse(skip(WORD).then(WORD), "foo bar"));
    assertEquals(parse(skip(WORD.then(WORD)).then(WORD), "foo bar baz"), 
                 parse(skip(WORD).then(skip(WORD)).then(WORD), "foo bar baz"));
  }

  @Test
  public void testOrParser() {
    assertEquals("123", parse(NUMBER.or(WORD), "123"));
    assertEquals("123", parse(WORD.or(NUMBER), "123"));
    assertEquals("123", parse(skip(WORD).or(NUMBER), "123"));
    assertEquals("123", parse(WORD.or(skip(NUMBER)), "123"));
    assertEquals("123", parse(skip(WORD).or(skip(NUMBER)), "123"));
  }

  @Test
  public void testManyParser() {
    assertEquals(Arrays.asList("foo", "bar", "baz"), parse(many(WORD), "foo bar baz"));
    assertEquals(Arrays.asList(1, 2, 3), parse(many(NUMBER.map(Integer::valueOf)), "1 2 3"));
  }

  @Test
  public void testMaybeParser() {
    assertEquals("foo", parse(maybe(WORD), "foo"));
    assertEquals(null, parse(maybe(WORD), "123"));
  }

  @Nullable
  private static <T> T parse(@NotNull BaseParser<T> parser, @NotNull String text) {
    return parse(parser, ourLexer, text);
  } 
}
