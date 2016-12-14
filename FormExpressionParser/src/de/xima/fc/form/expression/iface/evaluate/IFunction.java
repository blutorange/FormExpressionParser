package de.xima.fc.form.expression.iface.evaluate;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.NullLangObject;

/**
 * A function that can be called with any number of arguments. The thisContext
 * of a function must be set when the function object is created.
 * @author madgaksha
 *
 */
public interface IFunction<T extends ALangObject> {
	/**
	 * @return The name of this function. The empty string <code>""</code> when
	 *         anonymous.
	 */
	@Nonnull
	public String getDeclaredName();

	/**
	 * @return Name of the i-th declared argument.
	 */
	@Nonnull
	public String getDeclaredArgument(int i) throws ArrayIndexOutOfBoundsException;

	/**
	 * @return The number of arguments, including the optional varArgs.
	 */
	public int getDeclaredArgumentCount();

	/**
	 * Executes the function and returns the result.
	 *
	 * @param ec
	 *            Evaluation context.
	 * @param thisContext
	 *            The this context for the function call, or
	 *            {@link NullLangObject} when there is none (anonymous
	 *            functions).
	 * @param args
	 *            Arguments for the function.
	 * @return Result of the function.
	 * @throws EvaluationException
	 */
	@Nonnull
	public ALangObject evaluate(@Nonnull IEvaluationContext ec, @Nonnull T thisContext, @Nonnull ALangObject... args)
			throws EvaluationException;

	/**
	 * @return Such that {@link ILangObjectClass#getLangObjectClass()} equals the type parameter T.
	 */
	@Nonnull
	public ILangObjectClass getThisContextType();

	/**
	 * Indicates whether the last argument is a varArgs argument. If it is, {@link #getDeclaredArgumentCount()} must
	 * return a value larger than zero. During evaluation, all additional arguments are
	 * put in an array and passed as the value of the last declared parameter.
	 * @return Whether the last parameter is varArgs.
	 */
	public boolean hasVarArgs();
}
