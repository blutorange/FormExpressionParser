package de.xima.fc.form.expression.context;

import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.object.ALangObject;

public interface IScope {
	/**
	 * @param scope Name of the scope.
	 * @param name Name of the scoped variable.
	 * @return The value of the variable. <code>null</code> when the variable does not exist.
	 * @throws EvaluationException When the variable cannot be retrieved for
	 * any reason other than that it does not exist.
	 */
	public ALangObject getVariable(String scope, String name) throws EvaluationException;
	public void setVariable(String scope, String name, ALangObject value) throws EvaluationException;
}
