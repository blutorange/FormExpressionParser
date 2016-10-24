package de.xima.fc.form.expression.type;

import de.xima.fc.form.expression.object.ALangObject.Type;

public interface IVariableType {

	/**
	 * @param type Type to compare this type to.
	 * @return Whether the two types are equivalent.
	 */
	public boolean compatible(IVariableType type);

	/**
	 * @return The type of the language object of this type.
	 */
	public Type getLangObjectType();
}
