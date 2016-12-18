package de.xima.fc.form.expression.iface.evaluate;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.object.ALangObject;

@ParametersAreNonnullByDefault
public interface IUnparsableFunction<T extends ALangObject> extends IFunction<T> {
	public void unparseBody(StringBuilder builder);
}