package de.xima.fc.form.expression.exception.variabletype;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.util.CmnCnst;

public class MissingVariableTypeException extends IllegalVariableTypeException {
	private static final long serialVersionUID = 1L;
	public MissingVariableTypeException(final Node node, final String details) {
		super(node, String.format(CmnCnst.Error.MISSING_VARIABLE_TYPE, details));
	}
}
