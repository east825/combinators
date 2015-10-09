package combinators;

import com.google.common.collect.Iterables;
import combinators.util.Pair;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Mikhail Golubev
 */
public class FluentLexer {
  private final List<Pair<Pattern, Object>> myTokenSpecs;
  private final List<Pattern> myCommentSpecs;
  private final List<Pattern> myWhitespaceSpecs;

  public FluentLexer(@NotNull List<Pair<Pattern, Object>> specs,
                     @NotNull List<Pattern> commentSpecs,
                     @NotNull List<Pattern> whitespaceSpecs) {
    myTokenSpecs = specs;
    myCommentSpecs = commentSpecs;
    myWhitespaceSpecs = whitespaceSpecs;
  }

  @NotNull
  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    final List<Pair<Pattern, Object>> myTokenSpecs = new ArrayList<>();
    final List<Pattern> myCommentSpecs = new ArrayList<>();
    final List<Pattern> myWhitespaceSpecs = new ArrayList<>();


    @NotNull
    public Builder token(@Language("RegExp") @NotNull String regex, @NotNull Object type) {
      myTokenSpecs.add(Pair.of(Pattern.compile(regex), type));
      return this;
    }

    @NotNull
    public Builder whitespaceToken(@Language("RegExp") @NotNull String regex) {
      myWhitespaceSpecs.add(Pattern.compile(regex));
      return this;
    }

    @NotNull
    public Builder commentToken(@Language("RegExp") @NotNull String regex) {
      myCommentSpecs.add(Pattern.compile(regex));
      return this;
    }

    @NotNull
    public FluentLexer build() {
      return new FluentLexer(myTokenSpecs, myCommentSpecs, myWhitespaceSpecs);
    }
  }

  @NotNull
  public TokenStream start(@NotNull String text) {
    return new TokenStream(new TokenIterator(text));
  }

  private class TokenIterator implements Iterator<Token> {
    final String myText;
    Token myLastMatched = null;
    int myOffset;

    private TokenIterator(@NotNull String text) {
      myText = text;
      myOffset = skipWhitespacesAndComments(0);
    }

    private int skipWhitespacesAndComments(int startOffset) {
      for (Pattern pattern : Iterables.concat(myWhitespaceSpecs, myCommentSpecs)) {
        final Matcher matcher = pattern.matcher(myText);
        if (matcher.find(startOffset) && matcher.start() == startOffset) {
          return matcher.end();
        }
      }
      return startOffset;
    }

    @Nullable
    private Token findFirstMatch() {
      if (myOffset >= myText.length()) {
        return null;
      }
      for (Pair<Pattern, Object> spec : myTokenSpecs) {
        final Matcher matcher = spec.getFirst().matcher(myText);
        if (matcher.find(myOffset) && matcher.start() == myOffset) {
          return new Token(spec.getSecond(), matcher.group(), matcher.start());
        }
      }
      return null;
    }

    @Override
    public boolean hasNext() {
      if (myLastMatched == null) {
        myLastMatched = findFirstMatch();
      }
      return myLastMatched != null;
    }

    @Override
    public Token next() {
      Token result = myLastMatched;
      if (result == null) {
        result = findFirstMatch();
        if (result == null) {
          throw new NoSuchElementException();
        }
      }
      myLastMatched = null;
      myOffset = skipWhitespacesAndComments(result.getEndOffset());
      return result;
    }
  }
}
