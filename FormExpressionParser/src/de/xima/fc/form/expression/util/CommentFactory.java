package de.xima.fc.form.expression.util;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.ECommentType;
import de.xima.fc.form.expression.exception.FormExpressionException;
import de.xima.fc.form.expression.grammar.FormExpressionParserConstants;
import de.xima.fc.form.expression.grammar.Token;
import de.xima.fc.form.expression.iface.parse.IComment;

public final class CommentFactory {
	private CommentFactory() {}

	@Nonnull
	public static IComment fromToken(@Nonnull final Token token) {
		switch (token.kind) {
		case FormExpressionParserConstants.MultiLineComment:
		case FormExpressionParserConstants.TypeMultiLineComment:
		case FormExpressionParserConstants.SingleLineComment:
		case FormExpressionParserConstants.TypeSingleLineComment:
			break;
		default:
			throw new FormExpressionException(NullUtil.messageFormat(CmnCnst.Error.NOT_A_COMMENT_TOKEN, token.kind));
		}
		final Token t = NullUtil.checkNotNull(token);
		final ECommentType type = t.image.charAt(1) == '/' ? ECommentType.SINGLE_LINE : ECommentType.MULTI_LINE;
		final int column = t.beginColumn;
		final int line = t.beginLine;
		final String text;
		switch (type) {
		case MULTI_LINE:
			text = NullUtil.checkNotNull(t.image.substring(2, t.image.length()-2));
			break;
		case SINGLE_LINE:
			text = NullUtil.checkNotNull(t.image.substring(2));
			break;
		default:
			text = CmnCnst.NonnullConstant.STRING_EMPTY;
			break;
		}
		return new CommentImpl(type, text, line, column);
	}
}