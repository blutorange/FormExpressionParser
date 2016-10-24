package de.xima.fc.form.expression.exception.variabletype;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.type.IVariableType;

public class IncompatibleFunctionReturnTypeException extends IllegalVariableTypeException {
	public final IVariableType expectedReturnType, actualReturnType;
	public IncompatibleFunctionReturnTypeException(final Node functionNode, final IVariableType expectedReturnType, final IVariableType actualReturnType) {
		super(functionNode, String.format("Expected function to return type %s, but it can return type %s.", expectedReturnType, actualReturnType));
		this.expectedReturnType = expectedReturnType;
		this.actualReturnType = actualReturnType;
	}
}
