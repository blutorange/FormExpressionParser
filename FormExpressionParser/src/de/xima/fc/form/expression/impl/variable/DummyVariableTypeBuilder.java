package de.xima.fc.form.expression.impl.variable;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.ELangObjectType;
import de.xima.fc.form.expression.exception.IllegalVariableTypeException;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.iface.parse.IVariableTypeBuilder;

public enum DummyVariableTypeBuilder implements IVariableTypeBuilder {
	INSTANCE;
	@Override
	public DummyVariableTypeBuilder append(@Nonnull final IVariableType type) {
		return this;
	}
	@Override
	public IVariableType build() throws IllegalVariableTypeException {
		throw new IllegalVariableTypeException();
	}
	@Override
	public IVariableTypeBuilder setBasicType(@Nonnull final ELangObjectType type) {
		return this;
	}
	@Override
	public IVariableTypeBuilder append(final ELangObjectType type) {
		return this;
	}
}