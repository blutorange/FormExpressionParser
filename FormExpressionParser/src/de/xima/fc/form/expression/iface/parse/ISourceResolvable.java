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
	
	@Nonnull
	public String getVariableName();
}