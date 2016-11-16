package de.xima.fc.form.expression.iface.parse;

import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.ObjectPool;

import com.google.common.collect.ImmutableList;

import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.iface.context.IExternalContext;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.visitor.UnparseVisitorConfig;

public interface IFormExpression extends Serializable {
	/**
	 * Evaluates (executes) this program and returns the result. This method obtains the
	 * evaluation context from the provided pool.
	 * @param pool Pool to be used for obtaining an evaluation context.
	 * @param ex External context to be used, this may affect the returned result.
	 * @return The result.
	 * @throws EvaluationException When any error occurred during the evaluation.
	 * @see #evaluate(IEvaluationContext, IExternalContext)
	 */
	@Nonnull
	public ALangObject evaluate(@Nonnull final ObjectPool<IEvaluationContext> pool,
			@Nullable final IExternalContext externalContext) throws EvaluationException;

	/**
	 * Evaluates (executes) this program and returns the result. This method
	 * creates a new evaluation context with the provided factory.
	 * @param factory Factory to be used for creating a new evaluation context.
	 * @param ex External context to be used, this may affect the returned result.
	 * @return The result.
	 * @throws EvaluationException When any error occurred during the evaluation.
	 * @see #evaluate(IEvaluationContext, IExternalContext)
	 */
	@Nonnull
	public ALangObject evaluate(@Nonnull final BasePooledObjectFactory<IEvaluationContext> factory,
			@Nullable final IExternalContext externalContext) throws EvaluationException;

	/**
	 * Evaluates (executes) this program and returns the result. You must supply the
	 * evaluation context yourself.
	 * @param ec Evaluation context to be used, this may affect the returned result.
	 * @param ex External context to be used, this may affect the returned result.
	 * @return The result.
	 * @throws EvaluationException When any error occurred during the evaluation.
	 */
	@Nonnull
	public ALangObject evaluate(@Nonnull final IEvaluationContext ec, @Nullable final IExternalContext ex)
			throws EvaluationException;

	/**
	 * A string that is a valid form expression program and is equivalent to this program.
	 * @param config Number of white spaces etc. When <code>null</code>, uses some default configuration.
	 * @return A syntactically valid represenation of this program.
	 */
	@Nonnull
	public String unparse(@Nullable UnparseVisitorConfig config);

	/**
	 * @return A list of comment this program contains.
	 */
	@Nonnull
	public ImmutableList<IComment> getComments();

}
