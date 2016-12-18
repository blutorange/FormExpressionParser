package de.xima.fc.form.expression.iface.evaluate;

import java.io.Serializable;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.object.ALangObject;

@ParametersAreNonnullByDefault
public interface IGenericBracketAssignerFunction<T extends ALangObject> extends IAssignerFunction<T>, Serializable {
	/**
	 * Each accessor may specify additional restrictions that are checked by the static code analysis.
	 * @param thisContext The specific type of the this context. Its {@link IVariableType#getBasicLangClass()}
	 *                    is guaranteed to be <code>T</code>.
	 * @return The specific variable type of the property to be accessed.
	 */
	public IVariableType getPropertyType(IVariableType thisContext);

	/**
	 * Each accessor may specify additional restrictions that are checked by the static code analysis.
	 * @param thisContext The specific type of the this context. Its {@link IVariableType#getBasicLangClass()}
	 *                    is guaranteed to be <code>T</code>.
	 * @return The specific variable type of the value to be assigned.
	 */
	public IVariableType getValueType(IVariableType thisContext);

	/** @return The class of the property this accessor accepts. It must be able to handle any subclass as well. */
	public ILangObjectClass getPropertyClass();
}