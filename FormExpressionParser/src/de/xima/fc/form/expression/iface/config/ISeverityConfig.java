package de.xima.fc.form.expression.iface.config;

import org.eclipse.jdt.annotation.NonNullByDefault;
import javax.annotation.concurrent.Immutable;

import de.xima.fc.form.expression.enums.ESeverityOption;
import de.xima.fc.form.expression.impl.config.SeverityConfig;

/**
 * Options for static code analysis, whether an error should be thrown or not.
 * @author madgaksha
 * @see SeverityConfig.Builder Builder for creating configurations.
 */
@Immutable
@NonNullByDefault
public interface ISeverityConfig {
	public boolean hasOption(ESeverityOption option);
}