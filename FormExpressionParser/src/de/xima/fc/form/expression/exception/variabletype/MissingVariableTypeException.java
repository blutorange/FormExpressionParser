package de.xima.fc.form.expression.exception.variabletype;

import de.xima.fc.form.expression.grammar.Node;

public class MissingVariableTypeException extends IllegalVariableTypeException {
	public MissingVariableTypeException(final Node node, final String details) {
		super(node, String.format("Missing variable type declaration: %s", details));
	}
}
