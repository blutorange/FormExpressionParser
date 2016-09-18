package de.xima.fc.form.expression.node;

import org.apache.commons.lang3.StringEscapeUtils;

import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;

public class ASTDoubleQuotedString extends AStringNode {
	public ASTDoubleQuotedString(final int id) {
		super(id);
	}

	public ASTDoubleQuotedString(final FormExpressionParser p, final int id) {
		super(p, id);
	}

	@Override
	public String parseString(final String literal) throws ParseException {
		if (literal == null) throw new ParseException("string is null");
		if (literal.length() < 2) throw new ParseException("string not terminated properly: " + literal);
		return StringEscapeUtils.unescapeJava(literal.substring(1,literal.length()-1));
	}

}
