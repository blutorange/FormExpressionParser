package de.xima.fc.form.expression.iface.evaluate;

import java.io.Serializable;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.object.ALangObject;

@ParametersAreNonnullByDefault
public interface IDotAssignerFunction<T extends ALangObject> extends IAssignerFunction<T>, Serializable {
	/**
	 * Each accessor may specify additional restrictions that are checked by static code analysis.
	 * @param thisContext The specific type of the this context. Its {@link IVariableType#getBasicLangClass()}
	 *                    is guaranteed to be <code>T</code>.
	 * @param property The property to be assigned.
	 * @return The specific variable type of the value to be assigned.
	 */
	public IVariableType getValueType(IVariableType thisContext);
}