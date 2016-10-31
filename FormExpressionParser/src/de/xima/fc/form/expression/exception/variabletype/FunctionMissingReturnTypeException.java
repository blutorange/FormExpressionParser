package de.xima.fc.form.expression.exception.variabletype;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.util.CmnCnst;

public class FunctionMissingReturnTypeException extends IllegalVariableTypeException {
	private static final long serialVersionUID = 1L;
	public FunctionMissingReturnTypeException(final Node node) {
		super(node, String.format(CmnCnst.Error.FUNCTION_MISSING_RETURN_TYPE));
	}
}
