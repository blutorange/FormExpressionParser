package de.xima.fc.form.expression.context;

import de.xima.fc.form.expression.object.ALangObject;

public interface ICustomScope {
	public ALangObject fetch(String variableName);
	public String getScopeName();
}
