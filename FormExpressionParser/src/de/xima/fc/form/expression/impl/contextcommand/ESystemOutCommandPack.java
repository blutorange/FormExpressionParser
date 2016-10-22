package de.xima.fc.form.expression.impl.contextcommand;

import de.xima.fc.form.expression.context.IExternalContextCommand;

public enum ESystemOutCommandPack implements IExternalContextCommand {
	DISABLE_OUTPUT,
	ENABLE_OUTPUT;

	@Override
	public <T extends IExternalContextCommand> T castOrNull(Class<T> clazz) {
		return this.getClass().isAssignableFrom(clazz) ? clazz.cast(this) : null;
	}

	@Override
	public Object getData() {
		return null;
	}
}