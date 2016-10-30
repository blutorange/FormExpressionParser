package de.xima.fc.form.expression.context;

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
	 * @return Value of the variable in this scope.*/
	public ALangObject fetch(String variableName);
	/** @return Name of the scope that can be used by programs to access variables in this scope. */
	public String getScopeName();
}
