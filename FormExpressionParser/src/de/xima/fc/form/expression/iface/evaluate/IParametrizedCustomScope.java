package de.xima.fc.form.expression.iface.evaluate;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
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
	 * @throws EvaluationException When an illegal variable is requested.
	 */
	@Nonnull
	public ALangObject fetch(@Nonnull String variableName, @Nonnull T object, @Nonnull final IEvaluationContext ec) throws EvaluationException;
	@Nonnull
	public String getScopeName();
}