package de.xima.fc.form.expression.iface.factory;

import java.io.Serializable;

import org.eclipse.jdt.annotation.NonNullByDefault;
import javax.annotation.concurrent.Immutable;

/**
 * <b>Must be immutable.</b>
 * @author madgaksha
 */
@Immutable
@NonNullByDefault
public interface IParamContractFactory<T,S> extends Serializable {
	/**
	 * @param level
	 * @return Creates an adhering to the specifications as
	 * indicated by additional methods of this contract factory.
	 */
	public T make(S object);
}