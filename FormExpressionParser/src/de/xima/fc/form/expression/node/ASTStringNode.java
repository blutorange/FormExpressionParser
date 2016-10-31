package de.xima.fc.form.expression.node;

import org.apache.commons.lang3.StringEscapeUtils;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTStringNode extends SimpleNode {
	private static final long serialVersionUID = 1L;

	public ASTStringNode(final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}

	private String stringValue;

	/**
	 * @param delimiter
	 *            Character which delimits the string. " or '
	 */
	public void init(final EMethod method, final String value, final char delimiter) throws ParseException {
		super.init(method);
		final String s = parseString(value);
		stringValue = s;
	}

	public String parseString(final String literal) throws ParseException {
		if (literal == null)
			throw new ParseException(CmnCnst.Error.NODE_NULL_STRING);
		if (literal.length() < 2)
			throw new ParseException(String.format(CmnCnst.Error.NODE_IMPROPER_STRING_TERMINATION, literal));
		try {
			return StringEscapeUtils.unescapeJava(literal.substring(1, literal.length() - 1));
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

	public String getStringValue() {
		return stringValue;
	}

	@Override
	protected void additionalToStringFields(final StringBuilder sb) {
		sb.append('"').append(StringEscapeUtils.escapeJava(stringValue)).append('"').append(',');
	}
}
