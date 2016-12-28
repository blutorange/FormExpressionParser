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
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public class ASTStringNode extends ANode {
	private static final long serialVersionUID = 1L;

	private char delimiter = '"';

	public ASTStringNode(final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}

	public ASTStringNode(final Node prototype) {
		super(prototype, FormExpressionParserTreeConstants.JJTSTRINGNODE);
	}

	/**
	 * @param delimiter
	 *            Character which delimits the string. " or '
	 */
	public void init(@Nullable final EMethod method, final char delimiter) throws ParseException {
		if (delimiter != '"' && delimiter != '\'' && delimiter != '`')
			throw new ParseException(
					NullUtil.messageFormat(CmnCnst.Error.INVALID_STRING_DELIMITER, Integer.valueOf(delimiter)));
		assertDelimiterChildrenEquals(delimiter);
		super.init(method);
		this.delimiter = delimiter;
	}

	private void assertDelimiterChildrenEquals(final char delimiter) throws ParseException {
		for (int i = children.length; i-- > 0;) {
			if (children[i].jjtGetNodeId() == FormExpressionParserTreeConstants.JJTSTRINGCHARACTERSNODE) {
				if (((ASTStringCharactersNode) children[i]).getDelimiter() != delimiter)
					throw new ParseException(NullUtil.messageFormat(CmnCnst.Error.UNMATCHING_STRING_DELIMITER,
							Character.valueOf(((ASTStringCharactersNode) children[i]).getDelimiter()),
							Integer.valueOf(delimiter)));
			}
		}
	}

	@Nullable
	@Override
	protected Node replacementOnChildRemoval(final int i) throws ArrayIndexOutOfBoundsException {
		return null;
	}

	public String parseString(final String literal) throws ParseException {
		if (literal.length() < 2)
			throw new ParseException(NullUtil.messageFormat(CmnCnst.Error.NODE_IMPROPER_STRING_TERMINATION, literal));
		try {
			return NullUtil.checkNotNull(StringEscapeUtils.unescapeJava(literal.substring(1, literal.length() - 1)));
		}
		catch (final IllegalArgumentException e) {
			throw new ParseException(NullUtil.messageFormat(CmnCnst.Error.NODE_INVALID_STRING,
					Integer.valueOf(getBeginLine()), Integer.valueOf(getBeginColumn()), e.getMessage()));
		}
	}

	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionReturnDataVisitor<R, T, E> visitor,
			final T data) throws E {
		return visitor.visit(this, data);
	}

	@Override
	public <R, E extends Throwable> R jjtAccept(final IFormExpressionReturnVoidVisitor<R, E> visitor) throws E {
		return visitor.visit(this);
	}

	@Override
	public <T, E extends Throwable> void jjtAccept(final IFormExpressionVoidDataVisitor<T, E> visitor, final T data)
			throws E {
		visitor.visit(this, data);
	}

	@Override
	public <E extends Throwable> void jjtAccept(final IFormExpressionVoidVoidVisitor<E> visitor) throws E {
		visitor.visit(this);
	}

	@Override
	protected void additionalToStringFields(final StringBuilder sb) {
		sb.append(delimiter).append(',');
	}

	public int getStringNodeCount() {
		return jjtGetNumChildren();
	}

	public boolean isInlineExpressionNode(final int i) {
		return jjtGetChild(i).jjtGetNodeId() != FormExpressionParserTreeConstants.JJTSTRINGCHARACTERSNODE;
	}

	public Node getStringNode(final int i) {
		return jjtGetChild(i);
	}

	public char getDelimiter() {
		return delimiter;
	}
}