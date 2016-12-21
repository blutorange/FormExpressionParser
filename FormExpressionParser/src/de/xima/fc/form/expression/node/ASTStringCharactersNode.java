package de.xima.fc.form.expression.node;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

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
import de.xima.fc.form.expression.object.StringLangObject;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public class ASTStringCharactersNode extends ANode {
	private static final long serialVersionUID = 1L;

	private char delimiter = '"';

	private String stringValue = CmnCnst.NonnullConstant.STRING_EMPTY;

	public ASTStringCharactersNode(final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}

	public ASTStringCharactersNode(final Node prototype) {
		super(prototype, FormExpressionParserTreeConstants.JJTSTRINGNODE);
	}

	public ASTStringCharactersNode(final Node prototype, final String string) {
		super(prototype, FormExpressionParserTreeConstants.JJTSTRINGNODE);
		this.stringValue = string;
	}

	/** @param value Characters of the string. */
	public void init(@Nullable final EMethod method, final String value, final char delimiter) throws ParseException {
		assertChildrenExactly(0);
		assertNonNull(value, CmnCnst.Error.NODE_NULL_STRING);
		if (delimiter != '"' && delimiter != '\'' && delimiter != '`')
			throw new ParseException(NullUtil.messageFormat(CmnCnst.Error.INVALID_STRING_DELIMITER, delimiter));
		final String s;
		try {
			s = StringLangObject.unescape(value, delimiter);
		}
		catch (final IllegalArgumentException e) {
			throw new ParseException(NullUtil.messageFormat(CmnCnst.Error.NODE_INVALID_STRING,
					new Integer(getBeginLine()), new Integer(getBeginColumn()), e.getMessage()));			
		}
		super.init(method);
		this.delimiter = delimiter;
		this.stringValue = s;
	}

	@Nullable
	@Override
	protected Node replacementOnChildRemoval(final int i) throws ArrayIndexOutOfBoundsException {
		return null;
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

	public String getStringValue() {
		return stringValue;
	}

	@Override
	protected void additionalToStringFields(final StringBuilder sb) {
		sb.append('"').append(StringEscapeUtils.escapeJava(stringValue)).append('"').append(',').append(delimiter)
				.append(',');
	}
	
	public char getDelimiter() {
		return delimiter;
	}
}