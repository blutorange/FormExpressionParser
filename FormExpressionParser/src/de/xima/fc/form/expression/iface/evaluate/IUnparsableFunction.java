package de.xima.fc.form.expression.iface.evaluate;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.object.ALangObject;

@NonNullByDefault
public interface IUnparsableFunction<T extends ALangObject> extends IFunction<T> {
	public void unparseBody(StringBuilder builder);
}