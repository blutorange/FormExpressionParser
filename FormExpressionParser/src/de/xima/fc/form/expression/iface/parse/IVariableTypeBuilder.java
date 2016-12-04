package de.xima.fc.form.expression.iface.parse;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.ELangObjectType;

public interface IVariableTypeBuilder {
	@Nonnull
	IVariableTypeBuilder append(@Nonnull IVariableType type);
	@Nonnull
	IVariableType build() throws IllegalStateException;
	void setBasicType(@Nonnull ELangObjectType type);
}