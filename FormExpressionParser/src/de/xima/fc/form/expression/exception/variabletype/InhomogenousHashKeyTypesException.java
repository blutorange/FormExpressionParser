package de.xima.fc.form.expression.exception.variabletype;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.type.IVariableType;

public class InhomogenousHashKeyTypesException extends InhomogenousHashTypesException {
	public InhomogenousHashKeyTypesException(final int index, final IVariableType previousType,
			final IVariableType currentType, final Node hashNode) {
		super(index, previousType, currentType, "key", hashNode);
	}
}
