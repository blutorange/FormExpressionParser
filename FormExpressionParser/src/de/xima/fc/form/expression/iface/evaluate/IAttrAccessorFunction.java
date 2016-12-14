package de.xima.fc.form.expression.iface.evaluate;

import java.io.Serializable;

import de.xima.fc.form.expression.object.ALangObject;

public interface IAttrAccessorFunction<T extends ALangObject> extends IFunction<T>, Serializable {
	//public IVariableType getReturnTypeFor(IVariableType thisContext, IVariableType... args);
}