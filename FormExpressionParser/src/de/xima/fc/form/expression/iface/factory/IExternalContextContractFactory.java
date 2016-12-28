package de.xima.fc.form.expression.iface.factory;

import javax.annotation.Nullable;
import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.iface.evaluate.IExternalContext;

@NonNullByDefault
public interface IExternalContextContractFactory<T> extends IParamContractFactory<IExternalContext, T> {
	public boolean isProvidingScope(String scope);
	@Nullable
	public ILibraryScopeContractFactory<T> getScopeFactory(final String scope);
}