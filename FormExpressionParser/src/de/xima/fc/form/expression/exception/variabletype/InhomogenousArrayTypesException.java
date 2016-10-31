package de.xima.fc.form.expression.exception.variabletype;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.type.IVariableType;
import de.xima.fc.form.expression.util.CmnCnst;

public class InhomogenousArrayTypesException extends InhomogenousCompoundTypeException {
	private static final long serialVersionUID = 1L;
	public InhomogenousArrayTypesException(final int index, final IVariableType previousType,
			final IVariableType currentType, final Node arrayNode) {
		super(index, previousType, currentType, CmnCnst.NAME_ARRAY, CmnCnst.NAME_ELEMENT, arrayNode);
	}
}
