package combinators;

import org.junit.Test;

/**
 * @author Mikhail Golubev
 */
public class LexerTest extends CombinatorsTestCase {
  private static final FluentLexer ourWordLexer = FluentLexer.builder()
    .token("\\w+", "word")
    .token("\\s+", "space")
    .build();


  @Test
  public void testWords() {
    final TokenStream tokens = ourWordLexer.start("foo bar baz");
    assertTokensEqual("word", "foo", tokens.getToken());
    assertTokensEqual("word", "foo", tokens.getToken(0));
    assertTokensEqual("space", " ", tokens.getToken(1));
    final TokenStream tokens1 = tokens.advance();
    assertTokensEqual("space", " ", tokens1.getToken());
    assertTokensEqual("word", "bar", tokens1.getToken(1));
    assertTokensEqual("space", " ", tokens1.getToken(2));
    assertTokensEqual("word", "baz", tokens1.getToken(3));
  }
}
