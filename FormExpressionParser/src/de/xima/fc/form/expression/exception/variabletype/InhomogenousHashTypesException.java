package de.xima.fc.form.expression.exception.variabletype;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.type.IVariableType;
import de.xima.fc.form.expression.util.CmnCnst;

public abstract class InhomogenousHashTypesException extends InhomogenousCompoundTypeException {
	private static final long serialVersionUID = 1L;
	public InhomogenousHashTypesException(final int index, final IVariableType previousType,
			final IVariableType currentType, final String elementName, final Node hashNode) {
		super(index, previousType, currentType, CmnCnst.NAME_HASH, elementName, hashNode);
	}
}
