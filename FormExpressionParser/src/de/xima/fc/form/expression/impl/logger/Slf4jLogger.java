package de.xima.fc.form.expression.impl.logger;

import javax.annotation.Nullable;
import org.eclipse.jdt.annotation.NonNullByDefault;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.xima.fc.form.expression.enums.ELogLevel;
import de.xima.fc.form.expression.iface.evaluate.ILogger;
import de.xima.fc.form.expression.impl.logger.SystemLogger.Level;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
public class Slf4jLogger implements ILogger {
	private final Logger logger;
	private final ELogLevel level;
	/** @see Logger */
	public Slf4jLogger(final String name, final ELogLevel level) {
		logger = NullUtil.checkNotNull(LoggerFactory.getLogger(name));
		this.level = level;
	}
	@Override
	public void error(@Nullable final String message, @Nullable final Throwable cause) {
		if (level.numeric >= Level.ERROR.numeric)
			logger.error(message, cause);
	}
	@Override
	public void warn(@Nullable final String message, @Nullable final Throwable cause) {
		if (level.numeric >= Level.WARN.numeric)
			logger.warn(message, cause);
	}
	@Override
	public void info(@Nullable final String message, @Nullable final Throwable cause) {
		if (level.numeric >= Level.INFO.numeric)
			logger.info(message, cause);
	}
	@Override
	public void debug(@Nullable final String message, @Nullable final Throwable cause) {
		if (level.numeric >= Level.DEBUG.numeric)
			logger.debug(message, cause);
	}
	@Override
	public void reset() {
	}
}