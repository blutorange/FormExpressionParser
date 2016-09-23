package de.xima.fc.form.expression.impl.logger;

import java.text.DateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import de.xima.fc.form.expression.context.ILogger;

public class SystemLogger implements ILogger {

	private final Level level;
	private final String prefix;
	
	private static class InstanceHolder {
		public final static SystemLogger ERROR = new SystemLogger(Level.ERROR, "SystemLogger:");
		public final static SystemLogger WARN = new SystemLogger(Level.WARN, "SystemLogger:");
		public final static SystemLogger INFO = new SystemLogger(Level.INFO, "SystemLogger:");
		public final static SystemLogger DEBUG = new SystemLogger(Level.DEBUG, "SystemLogger:");
	}
	
	public static enum Level {
		OFF(0),
		ERROR(1),
		WARN(2),
		INFO(3),
		DEBUG(4),
		ALL(5);
		public final int numeric;
		private Level(int numeric) {
			this.numeric = numeric;
		}
	}
	
	public SystemLogger(Level level) {
		this(level, StringUtils.EMPTY);
	}
	public SystemLogger(Level level, String prefix) {
		this.level = level;
		this.prefix = prefix;
	}
	
	@Override
	public void error(String message) {
		if (level.numeric >= Level.ERROR.numeric) System.err.println(String.format("[ERROR] (%s) %s %s", getDate(), message));
	}

	@Override
	public void warn(String message) {
		if (level.numeric >= Level.WARN.numeric) System.err.println(String.format("[WARN] (%s) %s %s", getDate(), prefix, message));
	}

	@Override
	public void info(String message) {
		if (level.numeric >= Level.INFO.numeric) System.out.println(String.format("[INFO] (%s) %s %s", getDate(), prefix, message));
	}

	@Override
	public void debug(String message) {
		if (level.numeric >= Level.DEBUG.numeric) System.out.println(String.format("[DEBUG] (%s) %s %s", getDate(), prefix, message));
	}
	
	private String getDate() {
		final Date now = new Date();
		return DateFormat.getDateInstance().format(now) + " " + DateFormat.getTimeInstance().format(now);
	}
	
	public static SystemLogger getErrorLogger() {
		return InstanceHolder.ERROR;
	}
	public static SystemLogger getDebugLogger() {
		return InstanceHolder.DEBUG;
	}
	public static SystemLogger getWarnLogger() {
		return InstanceHolder.WARN;
	}
	public static SystemLogger getInfoLogger() {
		return InstanceHolder.INFO;
	}
}