package combinators;

import org.jetbrains.annotations.NotNull;

/**
 * @author Mikhail Golubev
 */
public class Token {
  private final String myType;
  private final int myStartOffset;
  private String myText;

  public Token(@NotNull String type, @NotNull String text, int startOffset) {
    myType = type;
    myText = text;
    myStartOffset = startOffset;
  }

  @NotNull
  public String getType() {
    return myType;
  }

  public int getStartOffset() {
    return myStartOffset;
  }

  public int getEndOffset() {
    return myStartOffset + myText.length();
  }

  @NotNull
  public String getText() {
    return myText;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Token)) return false;

    Token token = (Token)o;

    if (myStartOffset != token.myStartOffset) return false;
    if (myType != null ? !myType.equals(token.myType) : token.myType != null) return false;
    if (myText != null ? !myText.equals(token.myText) : token.myText != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = myType != null ? myType.hashCode() : 0;
    result = 31 * result + myStartOffset;
    result = 31 * result + (myText != null ? myText.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Token{" +
           "myType='" + myType + '\'' +
           ", myStartOffset=" + myStartOffset +
           ", myText='" + myText + '\'' +
           '}';
  }
}
