package de.xima.fc.form.expression.context;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ALangObject.Type;
import de.xima.fc.form.expression.object.NullLangObject;

/**
 * A function that can be called with any number of arguments. The thisContext
 * of a function must be set when the function object is created.
 *
 * @author madgaksha
 *
 */
public interface IFunction<T extends ALangObject> {
	/**
	 * @return The name of this function. The empty string <code>""</code> when
	 *         anonymous.
	 */
	@NotNull
	public String getDeclaredName();

	/**
	 * @return A list of the argument names as declared. Empty array when there
	 *         are no arguments.
	 */
	@NotNull
	public String[] getDeclaredArgumentList();

	@Nullable
	public String getVarArgsName();

	/**
	 * Runs the function and returns the result.
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
	@NotNull
	public ALangObject evaluate(IEvaluationContext ec, T thisContext, ALangObject... args) throws EvaluationException;

	/**
	 * @return Such that {@link Type#clazz} equals the type parameter T.
	 */
	@NotNull
	public ALangObject.Type getThisContextType();

	/**
	 * @return May be null for native code. The node with the user-defined code
	 *         of the function.
	 */
	@Nullable
	public Node getNode();
}
