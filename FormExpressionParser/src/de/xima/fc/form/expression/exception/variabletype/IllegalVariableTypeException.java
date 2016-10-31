package de.xima.fc.form.expression.exception.variabletype;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.util.CmnCnst;

public class IllegalVariableTypeException extends Exception {
	private static final long serialVersionUID = 1L;
	public final Node node;
	public IllegalVariableTypeException(final Node node, final String msg) {
		super(String.format(CmnCnst.Error.ILLEGAL_VARIABLE_TYPE,
				new Integer(node.getStartLine()), new Integer(node.getStartColumn()), msg));
		this.node = node;
	}
}
