package de.xima.fc.form.expression.context;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.object.ALangObject;

public interface IMethod2Function<T extends ALangObject> {
	public EMethod getMethod();
	public IFunction<T> getFunction();
}