package de.xima.fc.form.expression.iface.parse;

import javax.annotation.Nullable;
import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.enums.EVariableSource;
import de.xima.fc.form.expression.exception.parse.IllegalVariableSourceResolutionException;

@NonNullByDefault
public interface IScopedSourceResolvable extends ISourceResolvable {
	/**
	 * Does nothing when the variable is already resolved. Otherwise, it sets
	 * the scope and where to find that scope.
	 * 
	 * @param scope
	 *            Scope to set.
	 * @throws IllegalVariableSourceResolutionException 
	 */
	public void resolveSource(int source, EVariableSource sourceType, String scope) throws IllegalVariableSourceResolutionException;

	@Nullable
	public String getScope();


	public boolean hasScope();
}