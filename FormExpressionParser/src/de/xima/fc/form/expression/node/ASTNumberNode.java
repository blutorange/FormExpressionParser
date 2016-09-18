package de.xima.fc.form.expression.node;

import java.math.BigDecimal;

import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTNumberNode extends MySimpleNode {
	private BigDecimal bigDecimalValue;


	public ASTNumberNode(final int id) {
		super(id);
	}

	public ASTNumberNode(final FormExpressionParser p, final int id) {
		super(p, id);
	}

	/**
	 * @param string String representing the number.
	 * @param isInt Whether it is an integer or float.
	 */
	public void init(final String string, final boolean isInt) throws ParseException {
		try {
			bigDecimalValue = new BigDecimal(string, NumberLangObject.MATH_CONTEXT);
		}
		catch (final NumberFormatException e) {
			throw new ParseException("not a number: " + string);
		}
	}

	@Override
	public <R, T> R jjtAccept(final IFormExpressionParserVisitor<R, T> visitor, final T data) throws EvaluationException {
		return visitor.visit(this, data);
	}

	@Override
	public String toString() {
		return "NumberNode(" + bigDecimalValue.toPlainString() + ")";
	}

	public BigDecimal getBigDecimalValue() {
		return bigDecimalValue;
	}

}
