package de.xima.fc.form.expression.iface.context;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Optional;

public interface IExternalContextCommand {
	@Nonnull
	public <T extends IExternalContextCommand> 	Optional<T> castTo(@Nonnull Class<T> clazz);

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