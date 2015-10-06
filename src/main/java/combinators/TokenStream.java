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

  @Nullable
  public Object getTokenType(int offset) {
    final Token token = getToken(offset);
    return token != null ? token.getType() : null;
  }

  @Nullable
  public Object getTokenType() {
    return getTokenType(0);
  }

  @Nullable
  public String getTokenText() {
    return getTokenText(0);
  }

  @Nullable
  public String getTokenText(int offset) {
    final Token token = getToken(offset);
    return token != null ? token.getText() : null;
  }
  
  public int getOffset(int offset) {
    final Token token = getToken(offset);
    return token != null ? token.getStartOffset() : -1;
  }

  public int getOffset() {
    return getOffset(0);
  }

  @NotNull
  public TokenStream advance() {
    return new TokenStream(this, myBufferStart + 1);
  }
}
