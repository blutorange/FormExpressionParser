package de.xima.fc.form.expression.iface.evaluate;

import java.io.Serializable;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.object.ALangObject;

@ParametersAreNonnullByDefault
public interface IAccessorFunction<T extends ALangObject> extends IFunction<T>, Serializable {
	/** @return The class of the return value this accessors return. The actual return value class may be a subclass. */
	public ILangObjectClass getReturnClass();
}
