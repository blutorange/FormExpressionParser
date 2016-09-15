/* Generated By:JJTree: Do not edit this line. ASTDoubleQuotedString.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.xima.fc.form.expression.node;

import org.apache.commons.lang3.StringEscapeUtils;

import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;

public
@SuppressWarnings("all")
class ASTDoubleQuotedString extends StringNode {
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
/* JavaCC - OriginalChecksum=733b2ce2ee54ab7463e431a0b8342da0 (do not edit this line) */
