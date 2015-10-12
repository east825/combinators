package combinators;

import org.jetbrains.annotations.NotNull;

/**
 * @author Mikhail Golubev
 */
public class Token {
  private final Object myType;
  private final int myStartOffset;
  private final String myText;

  public Token(@NotNull Object type, @NotNull String text, int startOffset) {
    myType = type;
    myText = text;
    myStartOffset = startOffset;
  }

  @NotNull
  public Object getType() {
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
    if (o == null || getClass() != o.getClass()) return false;

    Token token = (Token)o;

    if (myStartOffset != token.myStartOffset) return false;
    if (!myType.equals(token.myType)) return false;
    return !(myText != null ? !myText.equals(token.myText) : token.myText != null);
  }

  @Override
  public int hashCode() {
    int result = myType.hashCode();
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
