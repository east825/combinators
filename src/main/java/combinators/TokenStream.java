package combinators;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @author Mikhail Golubev
 */
public class TokenStream {
  private final Iterator<Token> myTokens;
  private final List<Token> myTokenBuffer;
  private final int myBufferStart;

  public TokenStream(@NotNull Iterator<Token> tokens) {
    myTokens = tokens;
    myTokenBuffer = new ArrayList<>();
    myBufferStart = 0;
  }

  private TokenStream(@NotNull TokenStream other, int start) {
    myTokens = other.myTokens;
    myTokenBuffer = other.myTokenBuffer;
    myBufferStart = start;
  }

  @Nullable
  public Token getToken(int offset) {
    final int absOffset = myBufferStart + offset;
    if (absOffset >= myTokenBuffer.size()) {
      for (int i = myTokenBuffer.size(); i <= absOffset; i++) {
        if (!myTokens.hasNext()) {
          return null;
        }
        myTokenBuffer.add(myTokens.next());
      }
    }
    return myTokenBuffer.get(absOffset);
  }

  @Nullable
  public Token getToken() {
    return getToken(0);
  }

  public int getOffset() {
    final Token token = getToken();
    return token != null ? token.getStartOffset() : -1;
  }

  @NotNull
  public TokenStream advance() {
    return new TokenStream(this, myBufferStart + 1);
  }
}
