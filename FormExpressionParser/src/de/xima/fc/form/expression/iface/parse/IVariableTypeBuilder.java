package de.xima.fc.form.expression.iface.parse;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.exception.IllegalVariableTypeException;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;

@ParametersAreNonnullByDefault
public interface IVariableTypeBuilder {
	IVariableTypeBuilder append(IVariableType type);
	/**
	 * Adds the given type to the list of generics. Must be a simple type.
	 * @param type Type to add.
	 * @return this for chaining.
	 * @throws IllegalVariableTypeException When the argument is not a simple type.
	 */
	IVariableTypeBuilder append(ILangObjectClass type) throws IllegalVariableTypeException;
	IVariableType build() throws IllegalVariableTypeException;
	IVariableTypeBuilder setBasicType(ILangObjectClass type);
}