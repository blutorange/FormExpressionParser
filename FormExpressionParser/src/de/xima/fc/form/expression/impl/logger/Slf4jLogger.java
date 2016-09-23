package de.xima.fc.form.expression.impl.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.xima.fc.form.expression.context.ILogger;

public class Slf4jLogger implements ILogger {
	private final Logger logger;
	/** @see Logger */
	public Slf4jLogger(Class<?> clazz) {
		logger = LoggerFactory.getLogger(clazz);
	}
	@Override
	public void error(String message) {
		logger.error(message);
		
	}
	@Override
	public void warn(String message) {
		logger.warn(message);		
	}
	@Override
	public void info(String message) {
		logger.info(message);
	}
	@Override
	public void debug(String message) {
		logger.debug(message);
	}
}
