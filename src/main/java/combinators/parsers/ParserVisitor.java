package combinators.parsers;

import org.jetbrains.annotations.NotNull;

/**
 * @author Mikhail Golubev
 */
public interface ParserVisitor {
  void visitTokenParser(@NotNull TokenParser parser);

  <T> void visitSkipParser(@NotNull SkipParser<T> parser);

  void visitEofParser(@NotNull EofParser parser);

  <T1, T2> void visitThenParser(@NotNull ThenParser<T1, T2> parser);

  <T1, T2> void visitSkipThenParser(@NotNull SkipThenParser<T1, T2> parser);

  <T1, T2> void visitThenSkipParser(@NotNull ThenSkipParser<T1, T2> parser);

  <T> void visitAlternativeParser(@NotNull AlternativeParser<T> parser);

  <T> void visitRepeatParser(@NotNull RepeatParser<T> parser);

  <T1, T2> void visitMapParser(@NotNull MapParser<T1, T2> parser);

  <T> void visitOptionalParser(@NotNull OptionalParser<T> parser);

  <T> void visitForwardParser(@NotNull ForwardParser<T> parser);
}
