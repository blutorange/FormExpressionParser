package de.xima.fc.form.expression.iface.parse;

import java.io.Serializable;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;

import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.iface.context.IExternalContext;
import de.xima.fc.form.expression.object.ALangObject;

public interface IFormExpression<T extends IExternalContext> extends Serializable {
	/**
	 * Evaluates (executes) this program and returns the result. You must supply the
	 * evaluation context yourself.
	 * @param ec Evaluation context to be used, this may affect the returned result.
	 * @param ex External context to be used, this may affect the returned result.
	 * @return The result.
	 * @throws EvaluationException When any error occurred during the evaluation.
	 */
	@Nonnull
	public ALangObject evaluate(@Nonnull final T ex) throws EvaluationException;

	public IEvaluationContextContractFactory<T> getSpecs();

	/**
	 * @return A list of comment this program contains.
	 */
	@Nonnull
	public ImmutableList<IComment> getComments();

}
