package de.xima.fc.form.expression.exception.variabletype;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.type.IVariableType;

public class IncompatbileIfReturnType extends IllegalVariableTypeException {
	public final IVariableType ifType, elseType;
	public IncompatbileIfReturnType(final Node functionNode, final IVariableType ifType, final IVariableType elseType) {
		super(functionNode, String.format("If clause can return type %s, but else clause can return type %s.", ifType, elseType));
		this.ifType = ifType;
		this.elseType = elseType;
	}
}
