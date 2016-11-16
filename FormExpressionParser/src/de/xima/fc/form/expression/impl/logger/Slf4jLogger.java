package de.xima.fc.form.expression.impl.logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.xima.fc.form.expression.iface.context.ILogger;
import de.xima.fc.form.expression.util.NullUtil;

public class Slf4jLogger implements ILogger {
	@Nonnull private final Logger logger;
	/** @see Logger */
	public Slf4jLogger(@Nonnull final Class<?> clazz) {
		logger = NullUtil.checkNotNull(LoggerFactory.getLogger(clazz));
	}
	@Override
	public void error(@Nullable final String message) {
		logger.error(message);

	}
	@Override
	public void warn(@Nullable final String message) {
		logger.warn(message);
	}
	@Override
	public void info(@Nullable final String message) {
		logger.info(message);
	}
	@Override
	public void debug(@Nullable final String message) {
		logger.debug(message);
	}
}
