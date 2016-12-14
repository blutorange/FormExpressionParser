package de.xima.fc.form.expression.impl.variable.provider;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.exception.IllegalVariableTypeException;
import de.xima.fc.form.expression.object.ALangObject;

@ParametersAreNonnullByDefault
public final class ImmutableSimpleVariableTypeVariableProvider<T extends ALangObject> extends AVariableProvider<T> {
	private static final long serialVersionUID = 1L;
	private final T value;
	public ImmutableSimpleVariableTypeVariableProvider(final T value) throws IllegalVariableTypeException {
		super(value.getType().getSimpleVariableType());
		if (!value.getType().isImmutable())
			throw new IllegalVariableTypeException(String.format("Variable type %s is not immutable.", value.getType()));
		this.value = value;
	}
	@Override
	public T make() {
		return value;
	}
}