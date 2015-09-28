package combinators;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.LinkedList;

/**
 * @author Mikhail Golubev
 */
public class TokenStream {
  private final LinkedList<Token> myTokens;

  public TokenStream(@NotNull Iterable<Token> tokens) {
    myTokens = Lists.newLinkedList(tokens);
  }

  @Nullable
  public Token getToken() {
    return myTokens.peek();
  }

  public int getOffset() {
    return getToken().getStartOffset();
  }

  @NotNull
  public TokenStream advance() {
    if (myTokens.isEmpty()) {
      return new TokenStream(Collections.<Token>emptyList());
    }
    return new TokenStream(myTokens.subList(1, myTokens.size()));
  }
}
