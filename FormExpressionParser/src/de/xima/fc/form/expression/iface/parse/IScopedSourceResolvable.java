package de.xima.fc.form.expression.iface.parse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.enums.EVariableSource;
import de.xima.fc.form.expression.exception.parse.IllegalVariableResolutionException;

public interface IScopedSourceResolvable extends ISourceResolvable {
	/**
	 * Does nothing when the variable is already resolved. Otherwise, it sets
	 * the scope and where to find that scope.
	 * 
	 * @param scope
	 *            Scope to set.
	 */
	public void resolveSource(@Nonnull EVariableSource source, @Nonnull String scope)
			throws IllegalVariableResolutionException;

	public void resolveSource(int source, @Nonnull String scope) throws IllegalVariableResolutionException;

	@Nullable
	public String getScope();


	public boolean hasScope();
}