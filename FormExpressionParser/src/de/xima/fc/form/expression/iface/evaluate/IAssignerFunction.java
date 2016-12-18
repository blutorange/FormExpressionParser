package de.xima.fc.form.expression.iface.evaluate;

import java.io.Serializable;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.object.ALangObject;

@ParametersAreNonnullByDefault
public interface IAssignerFunction<T extends ALangObject> extends IFunction<T>, Serializable {
	/** @return The class of the value to be set by this assigner. It must be able to handle any subclass as well. */
	public ILangObjectClass getValueClass();
}
