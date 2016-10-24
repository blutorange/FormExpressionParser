package de.xima.fc.form.expression.type;

import de.xima.fc.form.expression.object.ALangObject.Type;

public class ArrayType implements IVariableType {
	private final IVariableType elementType;
	public ArrayType(final IVariableType elementType) {
		this.elementType = elementType;
	}
	@Override
	public boolean compatible(final IVariableType type) {
		if (this == type) return true;
		if (!(type instanceof ArrayType)) return false;
		return elementType.compatible(((ArrayType)type).elementType);
	}
	@Override
	public Type getLangObjectType() {
		return Type.ARRAY;
	}
}
