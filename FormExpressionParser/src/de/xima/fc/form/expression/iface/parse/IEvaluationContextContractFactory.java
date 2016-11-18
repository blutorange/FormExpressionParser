package de.xima.fc.form.expression.iface.parse;

import java.io.Serializable;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.exception.CannotAcquireEvaluationContextException;
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
	public IEvaluationContext getContextWithExternal(T ex) throws CannotAcquireEvaluationContextException;
	public boolean isProvidingExternalScope(String scope);
	@Nonnull
	public String[] getScopesForEmbedment(String embedment);
}
