package de.xima.fc.form.expression.exception.variabletype;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.type.IVariableType;

public class InhomogenousArrayTypesException extends InhomogenousCompoundTypesException {
	public InhomogenousArrayTypesException(final int index, final IVariableType previousType,
			final IVariableType currentType, final Node arrayNode) {
		super(index, previousType, currentType, "array", "element", arrayNode);
	}
}
