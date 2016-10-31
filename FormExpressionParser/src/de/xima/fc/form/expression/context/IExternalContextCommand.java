package de.xima.fc.form.expression.context;

import javax.annotation.Nullable;

public interface IExternalContextCommand {
	@Nullable
	public <T extends IExternalContextCommand> T castOrNull(Class<T> clazz);

	/**
	 *
	 * @return Data of this command. Implementing classes should replace
	 * the return type by a more specific type. When processing commands,
	 * use {@link #castOrNull(Class)} to convert the command to the correct
	 * type. Then you can access the data and type without additional casts.
	 */
	@Nullable
	public Object getData();

	/**
	 *
	 * @return Type of this command. Implementing classes should replace
	 * the return type by a more specific type. When processing commands,
	 * use {@link #castOrNull(Class)} to convert the command to the correct
	 * type. Then you can access the data and type without additional casts.
	 */
	@Nullable
	public Object getType();
}