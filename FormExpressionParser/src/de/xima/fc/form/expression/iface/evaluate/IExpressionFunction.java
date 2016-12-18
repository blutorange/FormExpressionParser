package de.xima.fc.form.expression.iface.evaluate;

import java.io.Serializable;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.object.ALangObject;

@ParametersAreNonnullByDefault
public interface IExpressionFunction<T extends ALangObject> extends IAccessorFunction<T>, Serializable {
	/**
	 * Each expression method may specify additional restrictions that are checked by static code analysis.
	 * @param thisContext The specific type of the this context. Its {@link IVariableType#getBasicLangClass()}
	 *                    is guaranteed to be <code>T</code>.
	 * @return The variable type returned by this expression method.
	 */
	public IVariableType getReturnType(IVariableType thisContext);

	/**
	 * @param thisContext The specific type of the this context. Its {@link IVariableType#getBasicLangClass()}
	 *                    is guaranteed to be <code>T</code>.
	 * @return Type of the value to the right.
	 */
	public IVariableType getRhsType(IVariableType thisContext);

	/** @return Class of the returned value. */
	public ILangObjectClass getRhsClass();
}