package de.xima.fc.form.expression.iface.parse;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.ELangObjectType;
import de.xima.fc.form.expression.exception.IllegalVariableTypeException;

public interface IVariableTypeBuilder {
	@Nonnull
	IVariableTypeBuilder append(@Nonnull IVariableType type);
	/**
	 * Adds the given type to the list of generics. Must be a simple type.
	 * @param type Type to add.
	 * @return this for chaining.
	 * @throws IllegalVariableTypeException When the argument is not a simple type. 
	 */
	@Nonnull
	IVariableTypeBuilder append(@Nonnull ELangObjectType type) throws IllegalStateException, IllegalVariableTypeException;
	@Nonnull
	IVariableType build() throws IllegalVariableTypeException;
	@Nonnull
	IVariableTypeBuilder setBasicType(@Nonnull ELangObjectType type);
}