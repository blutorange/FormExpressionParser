package de.xima.fc.form.expression.impl.logger;

import java.text.DateFormat;
import java.util.Date;

import javax.annotation.Nullable;
import org.eclipse.jdt.annotation.NonNullByDefault;

import org.apache.commons.lang3.StringUtils;

import de.xima.fc.form.expression.enums.ELogLevel;
import de.xima.fc.form.expression.exception.FormExpressionException;
import de.xima.fc.form.expression.iface.evaluate.ILogger;
import de.xima.fc.form.expression.util.CmnCnst;

@NonNullByDefault
public class SystemLogger implements ILogger {

	private final Level level;
	private final String prefix;

	private static class InstanceHolder {
		public final static SystemLogger ERROR = new SystemLogger(Level.ERROR, CmnCnst.Name.SYSTEM_LOGGER);
		public final static SystemLogger WARN = new SystemLogger(Level.WARN, CmnCnst.Name.SYSTEM_LOGGER);
		public final static SystemLogger INFO = new SystemLogger(Level.INFO, CmnCnst.Name.SYSTEM_LOGGER);
		public final static SystemLogger DEBUG = new SystemLogger(Level.DEBUG, CmnCnst.Name.SYSTEM_LOGGER);
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

	public SystemLogger(final Level level) {
		this(level, CmnCnst.NonnullConstant.STRING_EMPTY);
	}

	public SystemLogger(final Level level, final String prefix) {
		this.level = level;
		this.prefix = prefix;
	}

	@Override
	public void error(@Nullable final String message, @Nullable final Throwable cause) {
		if (level.numeric >= Level.ERROR.numeric)
			System.err.println(String.format(CmnCnst.SYSTEM_LOGGER_FORMAT, Level.ERROR, getCurrentDate(), prefix,
					buildMessage(message, cause)));
	}

	@Override
	public void warn(@Nullable final String message, @Nullable final Throwable cause) {
		if (level.numeric >= Level.WARN.numeric)
			System.err.println(String.format(CmnCnst.SYSTEM_LOGGER_FORMAT, Level.WARN, getCurrentDate(), prefix,
					buildMessage(message, cause)));
	}

	@Override
	public void info(@Nullable final String message, @Nullable final Throwable cause) {
		if (level.numeric >= Level.INFO.numeric)
			System.out.println(String.format(CmnCnst.SYSTEM_LOGGER_FORMAT, Level.INFO, getCurrentDate(), prefix,
					buildMessage(message, cause)));
	}

	@Override
	public void debug(@Nullable final String message, @Nullable final Throwable cause) {
		if (level.numeric >= Level.DEBUG.numeric)
			System.out.println(String.format(CmnCnst.SYSTEM_LOGGER_FORMAT, Level.DEBUG, getCurrentDate(), prefix,
					buildMessage(message, cause)));
	}

	public static ILogger get(final ELogLevel level) {
		switch (level) {
		case DEBUG:
			return InstanceHolder.DEBUG;
		case ERROR:
			return InstanceHolder.ERROR;
		case INFO:
			return InstanceHolder.INFO;
		case WARN:
			return InstanceHolder.WARN;
		default:
			throw new FormExpressionException();
		}
	}

	public static ILogger get(final String name, final ELogLevel level) {
		switch (level) {
		case DEBUG:
			return new SystemLogger(Level.DEBUG, name);
		case ERROR:
			return new SystemLogger(Level.ERROR, name);
		case INFO:
			return new SystemLogger(Level.INFO, name);
		case WARN:
			return new SystemLogger(Level.WARN, name);
		default:
			throw new FormExpressionException();
		}
	}

	private static String getCurrentDate() {
		final Date now = new Date();
		return DateFormat.getDateInstance().format(now) + StringUtils.SPACE + DateFormat.getTimeInstance().format(now);
	}

	@Override
	public void reset() {
	}

	public static String buildMessage(@Nullable final String message, @Nullable final Throwable cause) {
		final StringBuilder sb = new StringBuilder();
		if (message != null)
			sb.append(message).append(System.lineSeparator());
		appendAll(sb, cause);
		return sb.toString();
	}

	public static void appendStackTrace(final StringBuilder sb, @Nullable final Throwable cause) {
		if (cause != null)
			for (final StackTraceElement el : cause.getStackTrace())
				sb.append(el.toString()).append(System.lineSeparator());
	}

	public static void appendAll(final StringBuilder sb, @Nullable final Throwable cause) {
		for (Throwable c = cause; c != null; c = c.getCause()) {
			if (c != cause)
				sb.append("caused by:").append(System.lineSeparator()); //$NON-NLS-1$
			sb.append(c.getClass().getCanonicalName()).append(": ").append(c.getMessage()) //$NON-NLS-1$
					.append(System.lineSeparator());
			appendStackTrace(sb, c);
		}
	}
}