package de.xima.fc.form.expression.iface.factory;

import java.io.Serializable;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;

/**
 * <b>Must be immutable.</b>
 * @author madgaksha
 */
@Immutable
@ParametersAreNonnullByDefault
public interface IContractFactory<T> extends Serializable {
	/**
	 * @param level
	 * @return Creates an adhering to the specifications as
	 * indicated by additional methods of this contract factory.
	 */
	public T make();
}