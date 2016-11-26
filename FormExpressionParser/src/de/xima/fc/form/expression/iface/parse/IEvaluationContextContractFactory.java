package de.xima.fc.form.expression.iface.parse;

import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.iface.context.IExternalContext;

/**
 * Contains methods for getting details on the {@link IEvaluationContext}
 * created by {@link #getContextWithExternal(IExternalContext)}.
 *
 * @author madgaksha
 *
 * @param <T>
 *            Type of the required external context.
 */
public interface IEvaluationContextContractFactory<T extends IExternalContext> extends Serializable {
	@Nonnull
	public IEvaluationContext getContextWithExternal(@Nonnull T ex);
	/**
	 * Provides information about the scopes provided by all {@link IEvaluationContext}s this factory
	 * creates.
	 * @param scope Name of the scope to get info for.
	 * @return Information on the given scope. <code>null</code> iff the given scope is not provided.
	 * @see #isProvidingExternalScope(String)
	 */
	@Nullable
	public IScopeInfo getExternalScopeInfo(@Nonnull String scope);
	/**
	 * Provides information on whether all {@link IEvaluationContext} created by this
	 * factory provide a certain scope.
	 * @param scope Scope to check for.
	 * @return Whether the scope is provided.
	 */
	public boolean isProvidingExternalScope(@Nonnull String scope);
	/**
	 * Provides information about the scopes a certain embedment defines.
	 * @param embedment Embedment to get info for.
	 * @return List of scopes the given embedment defines.
	 */
	@Nullable
	public String[] getScopesForEmbedment(@Nonnull String embedment);
}
