package de.xima.fc.form.expression.impl.logger;

import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.context.ILogger;
import de.xima.fc.form.expression.util.NullUtil;

public class JavaLogger implements ILogger {
	@Nonnull private final Logger logger;
	/** @see Logger */
	public JavaLogger(@Nonnull final String name) {
		logger = NullUtil.checkNotNull(Logger.getLogger(name));
	}
	/** @see Logger */
	public JavaLogger(@Nonnull final String name, @Nonnull final String resourceBundle) {
		logger = NullUtil.checkNotNull(Logger.getLogger(name, resourceBundle));
	}
	@Override
	public void error(@Nullable final String message) {
		logger.severe(message);
	}
	@Override
	public void warn(@Nullable final String message) {
		logger.warning(message);
	}
	@Override
	public void info(@Nullable final String message) {
		logger.info(message);
	}
	@Override
	public void debug(@Nullable final String message) {
		logger.fine(message);
	}
}
