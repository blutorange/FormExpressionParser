package de.xima.fc.form.expression.iface.evaluate;

import java.io.Serializable;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.iface.parse.ILibraryScopeContractFactory;

@ParametersAreNonnullByDefault
public interface IExternalContextContractFactory<T> extends Serializable {
	public IExternalContext makeExternalContext(T object);
	public boolean isProvidingScope(String scope);
	@Nullable
	public ILibraryScopeContractFactory<T> getScopeFactory(final String scope);
}
