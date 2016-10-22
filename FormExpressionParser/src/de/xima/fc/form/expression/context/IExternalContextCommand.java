package de.xima.fc.form.expression.context;

import javax.annotation.Nullable;

public interface IExternalContextCommand {
	@Nullable
	public <T extends IExternalContextCommand> T castOrNull(Class<T> clazz);
	@Nullable
	public Object getData();
}