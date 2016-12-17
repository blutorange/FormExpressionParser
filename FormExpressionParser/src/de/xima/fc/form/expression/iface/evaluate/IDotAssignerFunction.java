package de.xima.fc.form.expression.iface.evaluate;

import java.io.Serializable;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.object.ALangObject;

@ParametersAreNonnullByDefault
public interface IDotAssignerFunction<T extends ALangObject> extends IFunction<T>, Serializable {
	public boolean isDotAssignerDefined(IVariableType thisContext, IVariableType value);
}