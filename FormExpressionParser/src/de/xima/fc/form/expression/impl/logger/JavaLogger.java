package de.xima.fc.form.expression.impl.logger;

import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.enums.ELogLevel;
import de.xima.fc.form.expression.iface.evaluate.ILogger;
import de.xima.fc.form.expression.impl.logger.SystemLogger.Level;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public class JavaLogger implements ILogger {
	private final Logger logger;
	private final ELogLevel level;
	/** @see Logger */
	public JavaLogger(final String name) {
		this(name, ELogLevel.WARN);
	}
	public JavaLogger(final String name, final ELogLevel logLevel) {
		logger = NullUtil.checkNotNull(Logger.getLogger(name));
		level = logLevel;
	}
	/** @see Logger */
	public JavaLogger(final String name, final String resourceBundle) {
		this(name, resourceBundle, ELogLevel.WARN);
	}
	public JavaLogger(final String name, final String resourceBundle, final ELogLevel logLevel) {
		logger = NullUtil.checkNotNull(Logger.getLogger(name, resourceBundle));
		level = logLevel;
	}
	@Override
	public void error(@Nullable final String message) {
		if (level.numeric >= Level.ERROR.numeric)
			logger.severe(message);
	}
	@Override
	public void warn(@Nullable final String message) {
		if (level.numeric >= Level.WARN.numeric)
			logger.warning(message);
	}
	@Override
	public void info(@Nullable final String message) {
		if (level.numeric >= Level.INFO.numeric)
			logger.info(message);
	}
	@Override
	public void debug(@Nullable final String message) {
		if (level.numeric >= Level.DEBUG.numeric)
			logger.fine(message);
	}
	@Override
	public void reset() {		
	}
}