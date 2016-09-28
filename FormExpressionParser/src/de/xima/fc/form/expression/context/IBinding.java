package de.xima.fc.form.expression.context;

import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.exception.NestingLevelTooDeepException;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.util.IReset;

public interface IBinding extends IReset {

	/**
	 * Resets this binding and all children, if any, created by {@link #nest()}.
	 * Must not reset any parents that created this binding.
	 */
	@Override
	public void reset();

	/**
	 * Must return the same object every time it is called with equivalent
	 * variable name, unless the value of the variable was changed with
	 * {@link #setVariable(String, ALangObject)} in the mean time.
	 * Implementations are free to decide whether variable names are treated
	 * case-insensitively or are mapped in any other way.
	 *
	 * @param name
	 *            Variable name.
	 * @return The value of the variable, or {@link NullLangObject} when it does
	 *         not exist. May also return null, which is converted to
	 *         {@link NullLangObject} automatically.
	 * @throws EvaluationException
	 *             When the value of the variable cannot be retrieved for any
	 *             reason other than that it does not exist.
	 */
	public ALangObject getVariable(String name) throws EvaluationException;
	public void setVariable(String name, ALangObject value) throws EvaluationException;

	/**
	 * Creates a new binding derived from the this binding. {@link #getVariable(String)}
	 * must return the same value unless #{@link #setVariable(String, ALangObject)} was called.
	 * <br><br>
	 * Implementations are not required to return a new object, merely a binding that
	 * behaves as specified.
	 * @return An {@link IBinding} that keeps the current variables and can be unnested to
	 * the current binding undoing all changes made to the nested binding.
	 */
	public IBinding nest() throws NestingLevelTooDeepException;

	/**
	 * Same as {@link #nest()}, but without falling back to the parent binding.
	 * The returned binding must be empty. However, {@link #unnest()} must still
	 * return the previous binding (a binding equivalent to this one).
	 * @return
	 */
	public IBinding nestLocal();

	/**
	 * Gets the previous binding from which this binding was derived.
	 * All side effects of {@link #setVariable(String, ALangObject)}
	 * must be undone.
	 * @return The parent binding. Undefined behaviour when there is no such binding.
	 */
	public IBinding unnest();

	/**
	 * The limit on nesting. Each if-clause, loop, try-clause, switch,
	 * and function call will add an additional nesting. Thus, this places
	 * a limit on the recursion depth as well.
	 * @return The number of times {@link #nest()} or {@link #nestLocal()}
	 * can be called without unnesting before a {@link NestingLevelTooDeepException}
	 * will be thrown. May return a negative number when unlimited, preferably
	 * <code>-1</code>.
	 */
	public int getNestingLimit();
}
