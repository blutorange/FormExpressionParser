package de.xima.fc.form.expression.impl.variable.provider;

import de.xima.fc.form.expression.iface.parse.IVariableProvider;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.impl.variable.SimpleVariableType;
import de.xima.fc.form.expression.object.NullLangObject;

public enum NullVariableProvider implements IVariableProvider<NullLangObject> {
	INSTANCE;
	@Override
	public NullLangObject make() {
		return NullLangObject.getInstance();
	}
	@Override
	public IVariableType getType() {
		return SimpleVariableType.NULL;
	}
}