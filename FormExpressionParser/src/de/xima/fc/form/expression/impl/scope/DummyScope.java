package de.xima.fc.form.expression.impl.scope;

import de.xima.fc.form.expression.enums.EScopeSource;
import de.xima.fc.form.expression.iface.context.ICustomScope;
import de.xima.fc.form.expression.iface.parse.IScopeInfo;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.util.CmnCnst;

public enum DummyScope implements ICustomScope, IScopeInfo {
	INSTANCE;
	@Override
	public boolean isProviding(final String variableName) {
		return false;
	}
	@Override
	public String getScopeName() {
		return CmnCnst.NonnullConstant.EMPTY_STRING;
	}
	@Override
	public ALangObject fetch(final String variableName) {
		return null;
	}
	@Override
	public EScopeSource getSource() {
		return EScopeSource.LIBRARY;
	}
}