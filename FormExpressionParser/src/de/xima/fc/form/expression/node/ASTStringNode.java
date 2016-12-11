package de.xima.fc.form.expression.node;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringEscapeUtils;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionReturnDataVisitor;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionReturnVoidVisitor;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionVoidDataVisitor;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionVoidVoidVisitor;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class ASTStringNode extends ANode {
	private static final long serialVersionUID = 1L;

	@Nonnull private String stringValue = CmnCnst.NonnullConstant.STRING_EMPTY;

	public ASTStringNode(@Nonnull final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}

	public ASTStringNode(@Nonnull final Node prototype) {
		super(prototype, FormExpressionParserTreeConstants.JJTSTRINGNODE);
	}

	public ASTStringNode(@Nonnull final Node prototype, @Nonnull final String string) {
		super(prototype, FormExpressionParserTreeConstants.JJTSTRINGNODE);
		this.stringValue = string;
	}

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

	@Override
	protected Node replacementOnChildRemoval(final int i) throws ArrayIndexOutOfBoundsException {
		return null;
	}

	@Nonnull
	public String parseString(@Nonnull final String literal) throws ParseException {
		if (literal.length() < 2)
			throw new ParseException(NullUtil.messageFormat(CmnCnst.Error.NODE_IMPROPER_STRING_TERMINATION, literal));
		try {
			return NullUtil.checkNotNull(StringEscapeUtils.unescapeJava(literal.substring(1, literal.length() - 1)));
		}
		catch (final IllegalArgumentException e) {
			throw new ParseException(NullUtil.messageFormat(CmnCnst.Error.NODE_INVALID_STRING,
					new Integer(getStartLine()), new Integer(getStartColumn()), e.getMessage()));
		}
	}

	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionReturnDataVisitor<R, T, E> visitor, final T data) throws E {
		return visitor.visit(this, data);
	}

	@Override
	public <R, E extends Throwable> R jjtAccept(final IFormExpressionReturnVoidVisitor<R, E> visitor) throws E {
		return visitor.visit(this);
	}

	@Override
	public <T, E extends Throwable> void jjtAccept(final IFormExpressionVoidDataVisitor<T, E> visitor, final T data) throws E {
		visitor.visit(this, data);
	}

	@Override
	public <E extends Throwable> void jjtAccept(final IFormExpressionVoidVoidVisitor<E> visitor) throws E {
		visitor.visit(this);
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
