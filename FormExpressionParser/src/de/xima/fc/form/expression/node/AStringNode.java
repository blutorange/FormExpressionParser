package de.xima.fc.form.expression.node;

import org.apache.commons.lang3.StringEscapeUtils;

import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public abstract class AStringNode extends MySimpleNode  {
	protected String stringValue;
	public AStringNode(final int id) {
		super(id);
	}

	public AStringNode(final FormExpressionParser p, final int id) {
		super(p, id);
	}

	public void init(final String value) throws ParseException {
		if (value == null) throw new ParseException("value is null");
		final String s = parseString(value);
		stringValue = s;
	}

	public abstract String parseString(String literal) throws ParseException;

	@Override
	public <R, T> R jjtAccept(final IFormExpressionParserVisitor<R, T> visitor, final T data) throws EvaluationException {
		return visitor.visit(this, data);
	}

	public String getStringValue() {
		return stringValue;
	}

	@Override
	public String toString() {
		return "StringNode(\"" + StringEscapeUtils.escapeJava(stringValue) + "\")";
	}

}
