package de.xima.fc.form.expression.impl.logger;

import java.util.logging.Logger;

import de.xima.fc.form.expression.context.ILogger;

public class JavaLogger implements ILogger {
	private final Logger logger;
	/** @see Logger */
	public JavaLogger(String name) {
		logger = Logger.getLogger(name);
	}
	/** @see Logger */
	public JavaLogger(String name, String resourceBundle) {
		logger = Logger.getLogger(name, resourceBundle);
	}
	@Override
	public void error(String message) {
		logger.severe(message);
	}
	@Override
	public void warn(String message) {
		logger.warning(message);
	}
	@Override
	public void info(String message) {
		logger.info(message);
	}
	@Override
	public void debug(String message) {
		logger.fine(message);
	}
}
