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
public interface IParametrizedCustomScope<T> {
	/**
	 * @see ICustomScope#fetch(String)
	 */
	@Nullable
	public ALangObject fetch(@Nonnull String variableName, @Nonnull T object);
	@Nonnull
	public String getScopeName();
}