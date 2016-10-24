package de.xima.fc.form.expression.type;

import de.xima.fc.form.expression.object.ALangObject.Type;

public class FunctionType implements IVariableType {
	private final IVariableType returnType;
	private final IVariableType[] argType;
	public FunctionType(final IVariableType returnType, final IVariableType... argType) {
		this.returnType = returnType;
		this.argType = argType;
	}
	@Override
	public boolean compatible(final IVariableType type) {
		if (this == type) return true;
		if (!(type instanceof FunctionType)) return false;
		final FunctionType functionType = (FunctionType)type;
		if (argType.length != functionType.argType.length)
			return false;
		for (int i = argType.length; i --> 0;)
			if (!argType[i].compatible(functionType.argType[i]))
				return false;
		return returnType.compatible(functionType.returnType);
	}
	@Override
	public Type getLangObjectType() {
		return Type.FUNCTION;
	}
}
