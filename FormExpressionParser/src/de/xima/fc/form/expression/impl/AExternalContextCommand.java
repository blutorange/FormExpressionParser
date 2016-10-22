package de.xima.fc.form.expression.impl;

import de.xima.fc.form.expression.context.IExternalContextCommand;

public abstract class AExternalContextCommand implements IExternalContextCommand {
	@Override
	public <T extends IExternalContextCommand> T castOrNull(Class<T> clazz) {
		return this.getClass().isAssignableFrom(clazz) ? clazz.cast(this) : null;
	}
}