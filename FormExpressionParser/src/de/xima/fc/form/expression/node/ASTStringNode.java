package de.xima.fc.form.expression.node;

import org.apache.commons.lang3.StringEscapeUtils;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTStringNode extends SimpleNode {
	private String stringValue;

	public ASTStringNode(final int id) {
		super(id);
	}

	/**
	 * @param delimiter Character which delimits the string. " or '
	 */
	public void init(final EMethod method, final String value, final char delimiter) throws ParseException {
		if (value == null) throw new ParseException("value is null");
		siblingMethod = method;
		final String s = parseString(value);
		stringValue = s;
	}

	public String parseString(final String literal) throws ParseException {
		if (literal == null) throw new ParseException("string is null");
		if (literal.length() < 2) throw new ParseException("string not terminated properly: " + literal);
		return StringEscapeUtils.unescapeJava(literal.substring(1,literal.length()-1));
	}

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
