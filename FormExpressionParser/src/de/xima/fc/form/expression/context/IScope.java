package de.xima.fc.form.expression.context;

import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.util.IReset;

public interface IScope extends IReset {
	/**
	 * Retrieves a variable with the given scope and name.
	 * @param scope Name of the scope.
	 * @param name Name of the scoped variable.
	 * @return The value of the variable. <code>null</code> when the variable does not exist.
	 * @throws EvaluationException When the variable cannot be retrieved for
	 * any reason other than that it does not exist.
	 */
	public ALangObject getVariable(String scope, String name, IEvaluationContext ec) throws EvaluationException;
	/**
	 * Stores a value as the variable with the given scope and name.
	 * @param scope Name of the scope.
	 * @param name Name of the scoped variable.
	 * @param value Value to be set. Non-null.
	 * @throws EvaluationException When the variable cannot be set for any reason.
	 */
	public void setVariable(String scope, String name, ALangObject value) throws EvaluationException;
}
