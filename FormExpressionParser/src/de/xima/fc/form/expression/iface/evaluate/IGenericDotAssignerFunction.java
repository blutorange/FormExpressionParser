package de.xima.fc.form.expression.iface.evaluate;

import java.io.Serializable;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.object.ALangObject;

@ParametersAreNonnullByDefault
public interface IGenericDotAssignerFunction<T extends ALangObject> extends IAssignerFunction<T>, Serializable {
	/**
	 * Each assigner may specify additional restrictions that are checked by the static code analysis.
	 * @param thisContext The specific type of the this context. Its {@link IVariableType#getBasicLangClass()}
	 *                    is guaranteed to be <code>T</code>.
	 * @param property The property to be assigned.
	 * @return The specific variable type of the value to be assigned.
	 */
	public IVariableType getValueType(IVariableType thisContext, String property);

	/**
	 * Each assigner may specify additional restrictions that are checked by the static code analysis.
	 * @param thisContext The specific type of the this context. Its {@link IVariableType#getBasicLangClass()}
	 *                    is guaranteed to be <code>T</code>.
	 * @param property The name of the property assigned to.
	 * @return Whether this type can assigned to the given property.
	 */
	public boolean isHandlingProperty(IVariableType thisContext, String property);

	/**
	 * @param property The property to be assigned to.
	 * @return Whether this generic accessor support the given property.
	 */
	public boolean isHandlingProperty(String property);
}