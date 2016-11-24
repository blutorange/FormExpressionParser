package de.xima.fc.form.expression.iface.parse;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.exception.parse.IllegalVariableSourceResolutionException;

public interface ISourceResolvable {
	/**
	 * Does nothing when the variable is already resolved.
	 * Otherwise, sets the source on the symbol table.
	 * @param scope Scope to set.
	 */
	public void resolveSource(int source) throws IllegalVariableSourceResolutionException;
	
	/**
	 * @param ec Current evaluation context for throwing exceptions.
	 * @return The resolved source.
	 */
	public int getSource();

	/** @return Whether the variable was resolved. */
	public boolean isResolved();
	
	/** @return The name of the variable to which this resolvable refers. */
	@Nonnull
	public String getVariableName();
	
//	/**
//	 * <p>
//	 * A variable (reference) is said to be inactive when the value
//	 * it holds is never queried. Note however that it may be set
//	 * any number of times. Consider the following program:
//	 * <pre>
//	 * a = 9;
//	 * b = 10;
//	 * c = 11;
//	 * b = a * 2;
//	 * c *= 2;
//	 * </pre>
//	 * <ul>
//	 * <li>Variable a is accessed and thus it is active.</li>
//	 * <li>Variable b is assigned to, but never read from and thus inactive.</li>
//	 * <li>Variable c is inactive because <code>c*=2</code> only changes the value of the variable wihtout any side-effects (that would affect other variables).</li>
//	 * </ul>
//	 * </p>
//	 * <p>
//	 * This method must return <code>false</code> unless {@link #active()} was called.
//	 * </p>
//	 * @return Whether this variable reference is active.
//	 */
//	public boolean isActive();
//	/**
//	 * Activates this variables reference.
//	 * @see #isActive()
//	 */
//	public void activate();
}