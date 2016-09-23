package de.xima.fc.form.expression.context;

import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.util.IReset;

public interface IBinding extends IReset {
	
	/**
	 * Resets this binding all children, if any, created by nest.
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
	 */
	public IBinding nest();
	/**
	 * Gets the previous binding from which this binding was derived.
	 * All side effects of {@link #setVariable(String, ALangObject)}
	 * must be undone.
	 * @return The parent binding. Undefined behaviour when there is no such binding.
	 */
	public IBinding unnest();
}
