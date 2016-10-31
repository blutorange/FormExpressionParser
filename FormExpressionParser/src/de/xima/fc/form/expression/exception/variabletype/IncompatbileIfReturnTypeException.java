package de.xima.fc.form.expression.exception.variabletype;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.type.IVariableType;
import de.xima.fc.form.expression.util.CmnCnst;

public class IncompatbileIfReturnTypeException extends IllegalVariableTypeException {
	private static final long serialVersionUID = 1L;
	public final IVariableType ifType, elseType;
	public IncompatbileIfReturnTypeException(final Node functionNode, final IVariableType ifType, final IVariableType elseType) {
		super(functionNode, String.format(CmnCnst.Error.INCOMPATIBLE_IF_RETURN_TYPE, ifType, elseType));
		this.ifType = ifType;
		this.elseType = elseType;
	}
}
