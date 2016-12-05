package de.xima.fc.form.expression.impl;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.base.Optional;

import de.xima.fc.form.expression.iface.evaluate.IExternalContextCommand;

public abstract class AExternalContextCommand implements IExternalContextCommand {
	@SuppressWarnings("unchecked") // We check the class beforehand.
	@Override
	@Nonnull
	@ParametersAreNonnullByDefault // Optional factory method does not return null
	public <T extends IExternalContextCommand> Optional<T> castTo(final Class<T> clazz) {
		if (getClass().isAssignableFrom(clazz))
			return Optional.of((T)this);
		return Optional.absent();
	}
}