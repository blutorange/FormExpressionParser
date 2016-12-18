package de.xima.fc.form.expression.iface.evaluate;

import java.io.Serializable;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.object.ALangObject;

@ParametersAreNonnullByDefault
public interface IGenericDotAccessorFunction<T extends ALangObject> extends IAccessorFunction<T>, Serializable {
	/**
	 * Each accessor may specify additional restrictions that are checked by the static code analysis.
	 * @param thisContext The specific type of the this context. Its {@link IVariableType#getBasicLangClass()}
	 *                    is guaranteed to be <code>T</code>.
	 * @param property The name of the property accessed.
	 * @return The specific variable type of the value returned.
	 */
	public IVariableType getReturnType(IVariableType thisContext, String property);

	/**
	 * Each accessor may specify additional restrictions that are checked by the static code analysis.
	 * @param thisContext The specific type of the this context. Its {@link IVariableType#getBasicLangClass()}
	 *                    is guaranteed to be <code>T</code>.
	 * @param property The name of the property accessed.
	 * @return Whether this type can provide the accessed property.
	 */
	public boolean isHandlingProperty(IVariableType thisContext, String property);

	/**
	 * @param property The property to be accessed.
	 * @return Whether this generic accessor support the given property.
	 */
	public boolean isHandlingProperty(String property);
}