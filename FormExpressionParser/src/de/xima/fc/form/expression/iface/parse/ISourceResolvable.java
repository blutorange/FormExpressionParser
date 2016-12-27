package de.xima.fc.form.expression.iface.parse;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.enums.EVariableSource;
import de.xima.fc.form.expression.exception.parse.IllegalVariableSourceResolutionException;

@ParametersAreNonnullByDefault
public interface ISourceResolvable {
	/**
	 * Does nothing when the variable is already resolved.
	 * Otherwise, sets the source on the symbol table.
	 * @param scope Scope to set.
	 * @throws IllegalVariableSourceResolutionException When the source has already been resolved.
	 */
	public void resolveSource(int source, EVariableSource type) throws IllegalVariableSourceResolutionException;

	/**
	 * @param source The new source.
	 * @throws IllegalVariableSourceResolutionException When the source has already been resolved.
	 */
	public void resolveClosureSource(int source) throws IllegalVariableSourceResolutionException;
	
	/**
	 * @param ec Current evaluation context for throwing exceptions.
	 * @return The resolved source.
	 */
	public int getBasicSource();
	public int getClosureSource();
	
	public EVariableSource getSourceType();

	/** @return Whether the variable was resolved. */
	public boolean isBasicSourceResolved();
	public boolean isClosureSourceResolved();

	
	/** @return The name of the variable to which this resolvable refers. */
	public String getVariableName();

	public void convertEnvironmentalToClosure() throws IllegalVariableSourceResolutionException;
}