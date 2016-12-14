package de.xima.fc.form.expression.iface.config;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;

import de.xima.fc.form.expression.iface.factory.IFormExpressionFactory;
import de.xima.fc.form.expression.impl.config.UnparseConfig;

/**
 * Options for unparsing a program, ie. formatting.
 * @author madgaksha
 * @see IFormExpressionFactory#format(String, IUnparseConfig) Factory method for formatting a program.
 * @see UnparseConfig.Builder Builder for creating configurations.
 */
@Immutable
@ParametersAreNonnullByDefault
public interface IUnparseConfig {
	/**
	 * @return The prefix for indentation, may consists of spaces and tabs.
	 */
	public String getIndentPrefix();

	/**
	 * @return The character(s) marking a line feed or line break. May consists of <code>\n</code> and <code>\r</code>
	 */
	public String getLinefeed();

	/**
	 * @return The character(s) for an optional space. May consist of spaces and tabs.
	 */
	public String getOptionalSpace();

	/**
	 * @return The character(s) for a required space. May consist of spaces and tabs.
	 */
	public String getRequiredSpace();

	/**
	 * @return Whether comments should be output or not.
	 */
	public boolean isKeepComments();
}
