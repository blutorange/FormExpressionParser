package de.xima.fc.form.expression.iface.evaluate;

import java.io.Serializable;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.object.ALangObject;

@ParametersAreNonnullByDefault
public interface IGenericDotAssignerFunction<T extends ALangObject> extends IFunction<T>, Serializable {
	public IVariableType getValueType(IVariableType thisContext, String property);
	public boolean isHandlingProperty(IVariableType thisContext, String property);

	public boolean isHandlingProperty(String property);
	public ILangObjectClass getValueClass();
}