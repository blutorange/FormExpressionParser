package de.xima.fc.form.expression.iface.evaluate;

import java.io.Serializable;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.object.ALangObject;

@ParametersAreNonnullByDefault
public interface IExpressionFunction<T extends ALangObject> extends IFunction<T>, Serializable {
	/**
	 * Returns info for expression methods called with certain variable types, eg <code>2+3</code>.
	 * @param lhs Left hand side.
	 * @param rhs Right hand side.
	 * @return The variable type returned by this expression method. <code>null</code> when this
	 * method is undefined for the given types.
	 */
	@Nullable
	public IVariableType getReturnTypeFor(IVariableType lhs, IVariableType rhs);
}