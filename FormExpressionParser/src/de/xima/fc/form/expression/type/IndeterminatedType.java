package de.xima.fc.form.expression.type;

import de.xima.fc.form.expression.object.ALangObject.Type;

/**
 * This type does not equal any type, not even itself.
 * @author madgaksha
 */
public enum IndeterminatedType implements IVariableType {
	INSTANCE
	;

	@Override
	public boolean compatible(final IVariableType type) {
		return false;
	}

	@Override
	public Type getLangObjectType() {
		return Type.NULL;
	}

}
