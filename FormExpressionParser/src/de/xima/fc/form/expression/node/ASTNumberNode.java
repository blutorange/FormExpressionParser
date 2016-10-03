package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTNumberNode extends SimpleNode {
	private double doubleValue;

	public ASTNumberNode(final int id) {
		super(id);
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
			throw new ParseException("Number not representable by a float: " + string);
		}
		siblingMethod = method;
	}

	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionParserVisitor<R, T, E> visitor, final T data) throws E {
		return visitor.visit(this, data);
	}

	@Override
	protected void additionalToStringFields(StringBuilder sb) {
		sb.append(doubleValue).append(",");
	}

	public double getDoubleValue() {
		return doubleValue;
	}

}
