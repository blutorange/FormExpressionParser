package de.xima.fc.form.expression.iface.context;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.exception.CannotUnnestGlobalNestingException;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.exception.NestingLevelTooDeepException;
import de.xima.fc.form.expression.exception.VariableNotDefinedException;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.util.IReset;

/**
 * @author madgaksha
 * @param <T>
 *            Class of the variables this binding stores. For evaluation this
 *            will be {@link ALangObject}, but there are some special purposes
 *            requiring other classes, such as the
 *            {@link VariableTypeCheckVisitor}.
 */
public interface IBinding extends IReset {

	/**
	 * Resets this binding. This means that {@link #isGlobal()} now returns <code>false</code>
	 * and {@link #getVariable(String)} now returns <code>null</code> for all variable names.
	 * No call that has been made to any method of this instance before the call to this method
	 * may affect the return value of any subsequent call to any method.
	 */
	@Override
	public void reset();

	/**
	 * Must return the same object every time it is called with equivalent
	 * variable name, unless the value of the variable was changed with
	 * {@link #setVariable(String, ALangObject)} in the mean time.
	 * Implementations are free to decide whether variable names are treated
	 * case-insensitively or are mapped in any other way.
	 * <br><br>
	 * Details on how variables are retrieved from different nesting levels
	 * my be decided by implementations.
	 * @param name
	 *            Variable name.
	 * @return The value of the variable, or null when it does
	 *         not exist.
	 * @throws EvaluationException
	 *             When the value of the variable cannot be retrieved for any
	 *             reason other than that it does not exist. Must not throw
	 *             an exception when the variable does not exist, but return
	 *             <code>null</code> instead.
	 */
	@Nullable
	public ALangObject getVariable(@Nonnull String name) throws EvaluationException;

	/**
	 * Sets a local variable. Must never look it up in any scopes. When the variable
	 * does not exist yet, it will be created. After it has been set,
	 * {@link #getVariable(String)} must not throw a {@link VariableNotDefinedException}
	 * anymore.
	 * @param name Name of the variable.
	 * @param value Value to set.
	 * @throws EvaluationException When the variable cannot be set.
	 */
	public void setVariable(@Nonnull String name, @Nonnull ALangObject value) throws EvaluationException;

	/**
	 * Modifies this binding. {@link #getVariable(String)} must return the same value
	 * unless #{@link #setVariable(String, ALangObject)} was called.
	 * @throws NestingLevelTooDeepException When the nesting level has been reached.
	 */
	public void nest(@Nonnull IEvaluationContext ec) throws NestingLevelTooDeepException;

	/**
	 * Same as {@link #nest()}, but without falling back to any parent bindings other than
	 * the global binding.
	 * The returned binding must otherwise be empty. {@link #unnest()} must still
	 * be able to unnest the nesting.
	 * @throws NestingLevelTooDeepException When the nesting level has been reached.
	 */
	public void nestLocal(@Nonnull IEvaluationContext ec) throws NestingLevelTooDeepException;

	/**
	 * Gets the previous binding from which this binding was derived.
	 * All side effects of {@link #setVariable(String, ALangObject)}
	 * must be undone.
	 * @return The parent binding. Undefined behaviour when there is no such binding.
	 * @throws CannotUnnestGlobalNestingException When this binding is at the global level.
	 */
	public void unnest(@Nonnull IEvaluationContext ec) throws CannotUnnestGlobalNestingException;

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

	/**
	 * @return Whether the nesting limit has been reached and a call to {@link #nest(IEvaluationContext)}
	 * or {@link #nestLocal(IEvaluationContext)} will throw an error.
	 */
	public boolean isAtMaximumNestingLimit();

	/**
	 * @return Whether this binding is currently at the global scope, ie. at a nesting level of 0. Calling
	 * {@link #unnest(IEvaluationContext)} when at the global scope throws an error.
	 */
	public boolean isGlobal();
}