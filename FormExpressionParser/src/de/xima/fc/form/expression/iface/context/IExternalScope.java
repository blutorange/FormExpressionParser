package de.xima.fc.form.expression.iface.context;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.util.IReset;

public interface IExternalScope extends IReset {
	/**
	 * Retrieves a variable with the given scope and name.
	 * As semantic checks are performed before executing
	 * the program, an error must be thrown when a variable
	 * happens not to exist or cannot be retrieved.
	 * 
	 * @param scope
	 *            Name of the scope.
	 * @param name
	 *            Name of the scoped variable.
	 * @return The value of the variable.
	 * @throws EvaluationException
	 *             When the variable cannot be retrieved, eg. when it does not exist.
	 */
	@Nonnull
	public ALangObject getVariable(@Nonnull String scope, @Nonnull String name, @Nonnull IEvaluationContext ec)
			throws EvaluationException;
	/**
	 * @return List of scope names with scope provides.
	 */
	@Nonnull
	public String[] getProvidedScopes();
}