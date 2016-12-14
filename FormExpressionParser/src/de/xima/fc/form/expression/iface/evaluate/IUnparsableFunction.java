package de.xima.fc.form.expression.iface.evaluate;

import de.xima.fc.form.expression.object.ALangObject;

public interface IUnparsableFunction<T extends ALangObject> extends IFunction<T> {
	public void unparseBody(StringBuilder builder);
}