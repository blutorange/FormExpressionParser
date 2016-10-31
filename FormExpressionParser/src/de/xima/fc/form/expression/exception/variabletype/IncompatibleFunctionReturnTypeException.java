package de.xima.fc.form.expression.exception.variabletype;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.type.IVariableType;
import de.xima.fc.form.expression.util.CmnCnst;

public class IncompatibleFunctionReturnTypeException extends IllegalVariableTypeException {
	private static final long serialVersionUID = 1L;
	public final IVariableType expectedReturnType, actualReturnType;
	public IncompatibleFunctionReturnTypeException(final Node functionNode, final IVariableType expectedReturnType, final IVariableType actualReturnType) {
		super(functionNode, String.format(CmnCnst.Error.INCOMPATIBLE_FUNCTION_RETURN_TYPE, expectedReturnType, actualReturnType));
		this.expectedReturnType = expectedReturnType;
		this.actualReturnType = actualReturnType;
	}
}
