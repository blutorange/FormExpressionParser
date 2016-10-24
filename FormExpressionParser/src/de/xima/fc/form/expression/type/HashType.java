package de.xima.fc.form.expression.type;

import de.xima.fc.form.expression.object.ALangObject.Type;

public class HashType implements IVariableType {
	private final IVariableType keyType;
	private final IVariableType valueType;
	public HashType(final IVariableType keyType, final IVariableType valueType) {
		this.keyType = keyType;
		this.valueType = valueType;
	}
	@Override
	public boolean compatible(final IVariableType type) {
		if (this == type) return true;
		if (!(type instanceof HashType)) return false;
		final HashType hashType = (HashType)type;
		return keyType.compatible(hashType.keyType) && valueType.compatible(hashType.valueType);
	}
	@Override
	public Type getLangObjectType() {
		return Type.HASH;
	}
}
