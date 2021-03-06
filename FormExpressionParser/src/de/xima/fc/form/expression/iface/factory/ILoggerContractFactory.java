package de.xima.fc.form.expression.iface.factory;

import java.io.Serializable;

import javax.annotation.Nullable;
import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.enums.ELogLevel;
import de.xima.fc.form.expression.iface.evaluate.ILogger;

@NonNullByDefault
public interface ILoggerContractFactory extends Serializable {
	/**
	 * @param name A name (or message) to identify messages from different loggers.
	 * @return A logger for logging.
	 */
	public ILogger make(String name, @Nullable ELogLevel level);
}