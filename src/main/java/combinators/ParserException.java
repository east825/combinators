package combinators;

import org.jetbrains.annotations.NotNull;

/**
 * @author Mikhail Golubev
 */
public class ParserException extends RuntimeException {

  private final int myOffset;

  public ParserException(@NotNull String message, int offset) {
    super(message);
    myOffset = offset;
  }

  public int getOffset() {
    return myOffset;
  }
}
