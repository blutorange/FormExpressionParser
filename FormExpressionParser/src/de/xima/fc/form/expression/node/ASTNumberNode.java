package de.xima.fc.form.expression.node;

import java.math.BigDecimal;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.NumberLangObject;

public class ASTNumberNode extends MySimpleNode {
	private BigDecimal value;


	public ASTNumberNode(final int id) {
		super(id);
	}

	public ASTNumberNode(final FormExpressionParser p, final int id) {
		super(p, id);
	}

	@Override
	public ALangObject evaluate(final IEvaluationContext fc) {
		return NumberLangObject.create(value);
	}

	/**
	 * @param string String representing the number.
	 * @param isInt Whether it is an integer or float.
	 */
	public void init(final String string, final boolean isInt) throws ParseException {
		try {
			value = new BigDecimal(string, NumberLangObject.MATH_CONTEXT);
		}
		catch (final NumberFormatException e) {
			throw new ParseException("not a number: " + string);
		}
	}

	@Override
	public String toString() {
		return "NumberNode(" + value.toPlainString() + ")";
	}

}
