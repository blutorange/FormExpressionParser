package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.StringLangObject;

public abstract class StringNode extends MySimpleNode  {
	private String stringValue;
	public StringNode(final int id) {
		super(id);
	}

	public StringNode(final FormExpressionParser p, final int id) {
		super(p, id);
	}

	public void init(final String value) throws ParseException {
		if (value == null) throw new ParseException("value is null");
		final String s = parseString(value);
		stringValue = s;
	}

	public abstract String parseString(String literal) throws ParseException;

	@Override
	public ALangObject evaluate(final IEvaluationContext ec) {
		return StringLangObject.create(stringValue);
	}

	@Override
	public String toString() {
		return "StringNode(" + stringValue + ")";
	}

}
