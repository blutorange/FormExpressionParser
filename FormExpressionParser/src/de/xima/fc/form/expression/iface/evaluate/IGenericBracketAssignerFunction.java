package de.xima.fc.form.expression.iface.evaluate;

import java.io.Serializable;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.object.ALangObject;

@ParametersAreNonnullByDefault
public interface IGenericBracketAssignerFunction<T extends ALangObject> extends IFunction<T>, Serializable {
	public IVariableType getPropertyType(IVariableType thisContext);
	public IVariableType getValueType(IVariableType thisContext);

	public ILangObjectClass getPropertyClass();
	public ILangObjectClass getValueClass();
}