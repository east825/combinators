package combinators.parsers;

import org.jetbrains.annotations.NotNull;

/**
 * @author Mikhail Golubev
 */
public class ParserException extends RuntimeException {

  private final int myOffset;

  public ParserException(@NotNull String message, int offset) {
    super(message + " at " + offset);
    myOffset = offset;
  }

  public int getOffset() {
    return myOffset;
  }
}
