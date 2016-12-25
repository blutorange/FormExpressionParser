package de.xima.fc.form.expression.iface.factory;

import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * <b>Must be immutable.</b>
 * @author madgaksha
 */
@Immutable
public interface IContractFactory<T> extends Serializable {
	/**
	 * @param level
	 * @return Creates an adhering to the specifications as
	 * indicated by additional methods of this contract factory.
	 */
	@Nonnull
	public T make();
}