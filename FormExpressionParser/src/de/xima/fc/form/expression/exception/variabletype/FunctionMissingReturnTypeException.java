package de.xima.fc.form.expression.exception.variabletype;

import de.xima.fc.form.expression.grammar.Node;

public class FunctionMissingReturnTypeException extends IllegalVariableTypeException {
	public FunctionMissingReturnTypeException(final Node node) {
		super(node, String.format("Functions must declare their return type."));
	}
}
