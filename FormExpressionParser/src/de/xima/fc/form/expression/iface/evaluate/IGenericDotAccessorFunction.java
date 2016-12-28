package de.xima.fc.form.expression.iface.evaluate;

import java.io.Serializable;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.object.ALangObject;

@NonNullByDefault
public interface IGenericDotAccessorFunction<T extends ALangObject> extends IFunction<T>, Serializable {
	public IVariableType getReturnType(IVariableType thisContext, String property);
	public boolean isHandlingProperty(IVariableType thisContext, String property);

	public ILangObjectClass getReturnClass();
	public boolean isHandlingProperty(String property);
}