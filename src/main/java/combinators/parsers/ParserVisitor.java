package combinators.parsers;

import org.jetbrains.annotations.NotNull;

/**
 * @author Mikhail Golubev
 */
public interface ParserVisitor<R> {
  void visitTokenParser(@NotNull TokenParser parser);

  <T> R visitSkipParser(@NotNull SkipParser<T> parser);

  R visitEofParser(@NotNull EofParser parser);

  <T> R visitAlternativeParser(@NotNull AlternativeParser<T> parser);

  <T1, T2, R1> R visitSequenceParser(SequenceParser<T1, T2, R1> parser);

  <T> R visitRepeatParser(@NotNull RepeatParser<T> parser);

  <T1, T2> R visitMapParser(@NotNull MapParser<T1, T2> parser);

  <T> R visitOptionalParser(@NotNull OptionalParser<T> parser);

  <T> R visitForwardParser(@NotNull ForwardParser<T> parser);
}
