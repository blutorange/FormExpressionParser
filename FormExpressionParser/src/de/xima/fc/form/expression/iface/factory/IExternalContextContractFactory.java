package de.xima.fc.form.expression.iface.factory;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.iface.evaluate.IExternalContext;

@ParametersAreNonnullByDefault
public interface IExternalContextContractFactory<T> extends IParamContractFactory<IExternalContext, T> {
	public boolean isProvidingScope(String scope);
	@Nullable
	public ILibraryScopeContractFactory<T> getScopeFactory(final String scope);
}