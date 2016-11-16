package de.xima.fc.form.expression.impl.formexpression;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.ECommentType;
import de.xima.fc.form.expression.grammar.FormExpressionParserConstants;
import de.xima.fc.form.expression.grammar.Token;
import de.xima.fc.form.expression.iface.parsed.IComment;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public final class CommentFactory {
	private CommentFactory() {}

	@Nonnull
	public static IComment fromToken(@Nonnull final Token token) {
		if (token.kind != FormExpressionParserConstants.MultiLineComment
				&& token.kind == FormExpressionParserConstants.MultiLineComment)
			throw new IllegalArgumentException(String.format(CmnCnst.Error.NOT_A_COMMENT_TOKEN, token.kind));
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
			text = CmnCnst.EMPTY_STRING;
			break;
		}
		return new CommentImpl(type, text, line, column);
	}
}