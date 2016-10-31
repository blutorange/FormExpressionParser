package de.xima.fc.form.expression.exception.variabletype;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.type.IVariableType;
import de.xima.fc.form.expression.util.CmnCnst;

public class InhomogenousHashValueTypesException extends InhomogenousHashTypesException {
	private static final long serialVersionUID = 1L;

	public InhomogenousHashValueTypesException(final int index, final IVariableType previousType,
			final IVariableType currentType, final Node hashNode) {
		super(index, previousType, currentType, CmnCnst.NAME_VALUE, hashNode);
	}
}
