package de.xima.fc.form.expression.exception.variabletype;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.type.IVariableType;

public abstract class InhomogenousHashTypesException extends InhomogenousCompoundTypesException {
	public InhomogenousHashTypesException(final int index, final IVariableType previousType,
			final IVariableType currentType, final String elementName, final Node hashNode) {
		super(index, previousType, currentType, "hash", elementName, hashNode);
	}
}
