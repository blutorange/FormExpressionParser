package de.xima.fc.form.expression.impl.contextcommand;

import de.xima.fc.form.expression.context.IExternalContextCommand;

public enum ESystemOutCommand implements IExternalContextCommand {
	DISABLE_OUTPUT,
	ENABLE_OUTPUT;

	@Override
	public <T extends IExternalContextCommand> T castOrNull(final Class<T> clazz) {
		return this.getClass().isAssignableFrom(clazz) ? clazz.cast(this) : null;
	}

	@Override
	public Void getData() {
		return null;
	}

	@Override
	public ESystemOutCommand getType() {
		return this;
	}
}