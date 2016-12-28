package de.xima.fc.form.expression.iface.factory;

import javax.annotation.Nullable;
import org.eclipse.jdt.annotation.NonNullByDefault;
import javax.annotation.concurrent.Immutable;

import de.xima.fc.form.expression.iface.evaluate.IEmbedment;

@NonNullByDefault
@Immutable
public interface IEmbedmentContractFactory extends IContractFactory<IEmbedment> {
	/**
	 * Provides information about the scopes a certain embedment defines.
	 * @param embedment Embedment to get info for.
	 * @return List of scopes the given embedment defines.
	 */
	@Nullable
	public String[] getScopesForEmbedment(String embedment);
}