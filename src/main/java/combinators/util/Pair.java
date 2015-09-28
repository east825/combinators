package combinators.util;

import org.jetbrains.annotations.Nullable;

/**
 * @author Mikhail Golubev
 */
public final class Pair<T1, T2> {
  public static <T1, T2> Pair<T1, T2> of(@Nullable T1 first, @Nullable T2 second) {
    return new Pair<>(first, second);
  }

  private final T1 myFirst;
  private final T2 mySecond;

  private Pair(@Nullable T1 first, @Nullable T2 second) {
    myFirst = first;
    mySecond = second;
  }

  @Nullable
  public T1 getFirst() {
    return myFirst;
  }

  @Nullable
  public T2 getSecond() {
    return mySecond;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Pair)) return false;

    Pair<?, ?> pair = (Pair<?, ?>)o;

    if (myFirst != null ? !myFirst.equals(pair.myFirst) : pair.myFirst != null) return false;
    if (mySecond != null ? !mySecond.equals(pair.mySecond) : pair.mySecond != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = myFirst != null ? myFirst.hashCode() : 0;
    result = 31 * result + (mySecond != null ? mySecond.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "(" + myFirst + ", " + mySecond + ")";
  }
}
