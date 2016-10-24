package de.xima.fc.form.expression.exception.variabletype;

import de.xima.fc.form.expression.grammar.Node;

public class IllegalVariableTypeException extends Exception {
	public final Node node;
	public IllegalVariableTypeException(final Node node, final String msg) {
		super(String.format("Ecountered incompatible variable type at line %d, column %d: %s",
				new Integer(node.getStartLine()), new Integer(node.getStartColumn()), msg));
		this.node = node;
	}
}
