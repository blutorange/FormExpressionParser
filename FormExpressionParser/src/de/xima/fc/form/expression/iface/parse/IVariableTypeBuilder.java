package de.xima.fc.form.expression.iface.parse;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.enums.EVariableTypeFlag;
import de.xima.fc.form.expression.exception.IllegalVariableTypeException;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;

@NonNullByDefault
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
	IVariableTypeBuilder setFlag(EVariableTypeFlag flag);
}