package de.xima.fc.form.expression.type;

import de.xima.fc.form.expression.object.ALangObject.Type;

public enum ExceptionType implements IVariableType {
	INSTANCE;

	@Override
	public boolean compatible(final IVariableType type) {
		return this == type;
	}

	@Override
	public Type getLangObjectType() {
		return Type.EXCEPTION;
	}

}
