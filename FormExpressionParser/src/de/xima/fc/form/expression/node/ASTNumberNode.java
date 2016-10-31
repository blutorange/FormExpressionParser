package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTNumberNode extends SimpleNode {
	private static final long serialVersionUID = 1L;
	private double doubleValue;

	public ASTNumberNode(final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}

	/**
	 * @param string String representing the number.
	 * @param isInt Whether it is an integer or float.
	 */
	public void init(final EMethod method, final String string, final boolean isInt) throws ParseException {
		try {
			doubleValue = Double.parseDouble(string);
		}
		catch (final NumberFormatException e) {
			throw new ParseException(String.format(CmnCnst.Error.NODE_INVALID_NUMBER, string, new Integer(getStartLine()), new Integer(getStartColumn()), e.getMessage()));
		}
		super.init(method);
	}

	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionParserVisitor<R, T, E> visitor, final T data) throws E {
		return visitor.visit(this, data);
	}

	@Override
	protected void additionalToStringFields(final StringBuilder sb) {
		sb.append(doubleValue).append(',');
	}

	public double getDoubleValue() {
		return doubleValue;
	}

}
