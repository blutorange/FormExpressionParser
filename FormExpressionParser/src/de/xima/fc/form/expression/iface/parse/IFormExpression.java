package de.xima.fc.form.expression.iface.parse;

import java.io.Serializable;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationWarning;
import de.xima.fc.form.expression.iface.evaluate.IExternalContext;
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

	/**
	 * @return A list of comment this program contains.
	 */
	@Nonnull
	public ImmutableList<IComment> getComments();

	/**
	 * <p>
	 * Checks the sanity of this program for a particular
	 * external context instance. All external context classes
	 * are required to tell for which scope and name all their
	 * instances return a value, but this value may be different
	 * for each instance and it may be a default value.
	 * </p>
	 * <p>
	 * This allows external contexts to output a warning when a
	 * they can only provide a default value for a certain scope
	 * and variable. For example, this can be used to check whether
	 * the current form version contains form fields for all field::variables
	 * used in the program.
	 * </p>
	 * @param ex The external context to check with.
	 * @return A list of warnings.
	 * @throws EvaluationException 
	 */
	@Nonnull
	public ImmutableCollection<IEvaluationWarning> analyze(@Nonnull final T ex) throws EvaluationException;

	/**
	 * Specifications to which the external contexts provided to
	 * {@link #equals(Object)} and {@link #analyze(IExternalContext)} must
	 * adhere to.
	 * @return The specifications.
	 * @see IEvaluationContextContractFactory
	 */
	@Nonnull
	public IEvaluationContextContractFactory<T> getSpecs();
}
