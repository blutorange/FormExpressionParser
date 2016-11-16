package de.xima.fc.form.expression.node;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringEscapeUtils;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTStringNode extends ANode {
	private static final long serialVersionUID = 1L;

	public ASTStringNode(@Nonnull final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}

	@Nonnull private String stringValue = CmnCnst.EMPTY_STRING;

	/**
	 * @param delimiter
	 *            Character which delimits the string. " or '
	 */
	public void init(@Nullable final EMethod method, @Nonnull final String value, final char delimiter) throws ParseException {
		assertChildrenExactly(0);
		assertNonNull(value, CmnCnst.Error.NODE_NULL_STRING);
		super.init(method);
		final String s = parseString(value);
		stringValue = s;
	}

	@Nonnull
	public String parseString(@Nonnull final String literal) throws ParseException {
		if (literal.length() < 2)
			throw new ParseException(String.format(CmnCnst.Error.NODE_IMPROPER_STRING_TERMINATION, literal));
		try {
			return NullUtil.checkNotNull(StringEscapeUtils.unescapeJava(literal.substring(1, literal.length() - 1)));
		}
		catch (final IllegalArgumentException e) {
			throw new ParseException(String.format(CmnCnst.Error.NODE_INVALID_STRING,
					new Integer(getStartLine()), new Integer(getStartColumn()), e.getMessage()));
		}
	}

	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionParserVisitor<R, T, E> visitor, final T data) throws E {
		return visitor.visit(this, data);
	}

	@Nonnull
	public String getStringValue() {
		return stringValue;
	}

	@Override
	protected void additionalToStringFields(final StringBuilder sb) {
		sb.append('"').append(StringEscapeUtils.escapeJava(stringValue)).append('"').append(',');
	}
}
