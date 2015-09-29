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
public class SimpleLexer implements Iterable<Token> {
  private final List<Pair<Pattern, Object>> myTokenSpecs;
  private final List<Pattern> myCommentSpecs;
  private final List<Pattern> myWhitespaceSpecs;
  private final String myText;

  public SimpleLexer(@NotNull List<Pair<Pattern, Object>> specs,
                     @NotNull List<Pattern> commentSpecs,
                     @NotNull List<Pattern> whitespaceSpecs,
                     @NotNull String text) {
    myTokenSpecs = specs;
    myCommentSpecs = commentSpecs;
    myWhitespaceSpecs = whitespaceSpecs;
    myText = text;
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
    public SimpleLexer build(@NotNull String text) {
      return new SimpleLexer(myTokenSpecs, myCommentSpecs, myWhitespaceSpecs, text);
    }
  }

  @NotNull
  @Override
  public Iterator<Token> iterator() {
    return new Iterator<Token>() {
      int myOffset = skipWhitespacesAndComments(0);
      Token myLastMatched = null;

      private int skipWhitespacesAndComments(int startOffset) {
        for (Pattern pattern : Iterables.concat(myWhitespaceSpecs, myCommentSpecs)) {
          final Matcher matcher = pattern.matcher(myText);
          if (matcher.find(startOffset)) {
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
          if (matcher.find(myOffset)) {
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
        myOffset = skipWhitespacesAndComments(myOffset);
        return result;
      }
    };
  }
}
