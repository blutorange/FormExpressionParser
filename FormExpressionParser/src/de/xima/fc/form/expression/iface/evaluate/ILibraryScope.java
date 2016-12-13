package de.xima.fc.form.expression.iface.evaluate;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.object.ALangObject;

/**
 * A custom scope that allows the environment to define certain functions or constants. For example,
 * an environment may provide a math scope containing functions such as sin or cos, and constants such as pi or e.
 * @author madgaksha
 */
public interface ILibraryScope<T> {
	/**
	 * @param variableName Name of the variable to fetch.
	 * @param object A custom object.
	 * @param ec The current evaluation context.
	 * @return Value of the variable in this scope.
	 * @throws EvaluationException When an illegal variable was requested.
	 */
	@Nonnull
	public ALangObject fetch(@Nonnull String variableName, @Nonnull T object, @Nonnull final IEvaluationContext ec)
			throws EvaluationException;

	/** @return Name of the scope that can be used by programs to access variables in this scope. */
	@Nonnull
	public String getScopeName();
}