package de.xima.fc.form.expression.impl.logger;

import java.text.DateFormat;
import java.util.Date;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;

import de.xima.fc.form.expression.iface.evaluate.ILogger;
import de.xima.fc.form.expression.util.CmnCnst;

public class SystemLogger implements ILogger {

	@Nonnull private final Level level;
	@Nonnull private final String prefix;

	private static class InstanceHolder {
		@Nonnull public final static SystemLogger ERROR = new SystemLogger(Level.ERROR, CmnCnst.Name.SYSTEM_LOGGER);
		@Nonnull public final static SystemLogger WARN = new SystemLogger(Level.WARN, CmnCnst.Name.SYSTEM_LOGGER);
		@Nonnull public final static SystemLogger INFO = new SystemLogger(Level.INFO, CmnCnst.Name.SYSTEM_LOGGER);
		@Nonnull public final static SystemLogger DEBUG = new SystemLogger(Level.DEBUG, CmnCnst.Name.SYSTEM_LOGGER);
	}

	public static enum Level {
		OFF(0),
		ERROR(1),
		WARN(2),
		INFO(3),
		DEBUG(4),
		ALL(5);
		public final int numeric;

		private Level(final int numeric) {
			this.numeric = numeric;
		}
	}

	public SystemLogger(@Nonnull final Level level) {
		this(level, CmnCnst.NonnullConstant.STRING_EMPTY);
	}

	public SystemLogger(@Nonnull final Level level, @Nonnull final String prefix) {
		this.level = level;
		this.prefix = prefix;
	}

	@Override
	public void error(final String message) {
		if (level.numeric >= Level.ERROR.numeric)
			System.err.println(String.format(CmnCnst.SYSTEM_LOGGER_FORMAT, Level.ERROR, getCurrentDate(), prefix, message));
	}

	@Override
	public void warn(final String message) {
		if (level.numeric >= Level.WARN.numeric)
			System.err.println(String.format(CmnCnst.SYSTEM_LOGGER_FORMAT, Level.WARN, getCurrentDate(), prefix, message));
	}

	@Override
	public void info(final String message) {
		if (level.numeric >= Level.INFO.numeric)
			System.out.println(String.format(CmnCnst.SYSTEM_LOGGER_FORMAT, Level.INFO, getCurrentDate(), prefix, message));
	}

	@Override
	public void debug(final String message) {
		if (level.numeric >= Level.DEBUG.numeric)
			System.out.println(String.format(CmnCnst.SYSTEM_LOGGER_FORMAT, Level.DEBUG, getCurrentDate(), prefix, message));
	}

	@Nonnull
	public static SystemLogger getErrorLogger() {
		return InstanceHolder.ERROR;
	}

	@Nonnull
	public static SystemLogger getDebugLogger() {
		return InstanceHolder.DEBUG;
	}

	@Nonnull
	public static SystemLogger getWarnLogger() {
		return InstanceHolder.WARN;
	}

	@Nonnull
	public static SystemLogger getInfoLogger() {
		return InstanceHolder.INFO;
	}

	@Nonnull
	private static String getCurrentDate() {
		final Date now = new Date();
		return DateFormat.getDateInstance().format(now) + StringUtils.SPACE + DateFormat.getTimeInstance().format(now);
	}
}