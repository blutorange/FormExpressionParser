package de.xima.fc.form.expression.impl.externalcontext;

import de.xima.fc.form.expression.iface.factory.IExternalContextContractFactory;
import de.xima.fc.form.expression.iface.factory.ILibraryScopeContractFactory;

public abstract class AGenericExternalContextFactory<T> implements IExternalContextContractFactory<T> {
	private static final long serialVersionUID = 1L;
	@Override
	public abstract AGenericExternalContext make(final T object);
	@Override
	public boolean isProvidingScope(final String scope) {
		return false;
	}
	@Override
	public ILibraryScopeContractFactory<T> getScopeFactory(final String scope) {
		return null;
	}
}