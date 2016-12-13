package de.xima.fc.form.expression.iface.evaluate;

import java.io.Serializable;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.iface.parse.ILibraryScopeContractFactory;
import de.xima.fc.form.expression.util.Void;

@ParametersAreNonnullByDefault
public interface ILibraryContractFactory extends Serializable {
	/**
	 * @return Creates a library adhering to the specifications as
	 * indicates by the contract factory.
	 */
	public ILibrary makeLibrary();

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