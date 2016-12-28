package de.xima.fc.form.expression.impl.contextcommand;

import javax.annotation.Nonnull;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.common.base.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

import de.xima.fc.form.expression.iface.evaluate.IExternalContextCommand;

public enum ESystemOutCommand implements IExternalContextCommand {
	DISABLE_OUTPUT,
	ENABLE_OUTPUT;

	@SuppressWarnings("unchecked")
	@Override
	@Nonnull
	@NonNullByDefault
	public <T extends IExternalContextCommand> Optional<T> castTo(@Nonnull final Class<T> clazz) {
		checkNotNull(clazz);
		if (getClass().isAssignableFrom(clazz))
			return Optional.of((T)this);
		return Optional.absent();
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