package de.xima.fc.form.expression.iface.parse;

import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.iface.context.IExternalContext;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.visitor.UnparseVisitorConfig;

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
	public ALangObject evaluate(@Nullable final T ex) throws EvaluationException;

	public IEvaluationContextProvider<T> getFactory();

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
