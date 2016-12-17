package de.xima.fc.form.expression.iface.evaluate;

import java.io.Serializable;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.object.ALangObject;

@ParametersAreNonnullByDefault
public interface IDotAccessorFunction<T extends ALangObject> extends IFunction<T>, Serializable {
	@Nullable
	public IVariableType getDotAccessorReturnType(IVariableType thisContext);
}