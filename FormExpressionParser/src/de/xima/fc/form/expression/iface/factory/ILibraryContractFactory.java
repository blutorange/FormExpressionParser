package de.xima.fc.form.expression.iface.factory;

import javax.annotation.Nullable;
import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.ILibrary;
import de.xima.fc.form.expression.util.Void;

/**
 * <b>Must be immutable.</b>
 * @author madgaksha
 *
 */
@NonNullByDefault
public interface ILibraryContractFactory extends IContractFactory<ILibrary> {
	/**
	 * Provides information on whether all {@link ILibrary} created by this
	 * factory provide values for a certain scope.
	 * @param scope Scope to check for.
	 * @return Whether the scope is provided.
	 */
	public boolean isProvidingScope(String scope);

	/**
	 * Provides information about the scopes provided by all {@link IEvaluationContext}s this factory
	 * creates.
	 * @param scope Name of the scope to get info for.
	 * @return Information on the given scope. <code>null</code> iff the given scope is not provided.
	 * @see #isProvidingExternalScope(String)
	 */
	@Nullable
	public ILibraryScopeContractFactory<Void> getScopeFactory(String scope);
}