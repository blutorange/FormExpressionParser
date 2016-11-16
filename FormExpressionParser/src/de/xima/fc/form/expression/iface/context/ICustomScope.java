package de.xima.fc.form.expression.iface.context;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.object.ALangObject;

/**
 * A custom scope that allows the environment to define certain functions or constants. For example,
 * an environment may provide a math scope containing functions such as sin or cos, and constants such as pi or e.
 * @author madgaksha
 *
 */
public interface ICustomScope {
	/**
	 * @param variableName Name of the variable to fetch.
	 * @return Value of the variable in this scope. <code>null</code> when the variable does not exist.
	 */
	@Nullable
	public ALangObject fetch(@Nonnull String variableName);
	/** @return Name of the scope that can be used by programs to access variables in this scope. */
	@Nonnull
	public String getScopeName();
}
