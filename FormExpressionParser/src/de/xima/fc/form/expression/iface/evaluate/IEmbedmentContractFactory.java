package de.xima.fc.form.expression.iface.evaluate;

import java.io.Serializable;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;

@ParametersAreNonnullByDefault
@Immutable
public interface IEmbedmentContractFactory extends Serializable {
	public IEmbedment makeEmbedment();

	/**
	 * Provides information about the scopes a certain embedment defines.
	 * @param embedment Embedment to get info for.
	 * @return List of scopes the given embedment defines.
	 */
	@Nullable
	public String[] getScopesForEmbedment(String embedment);
}
