package de.xima.fc.form.expression.impl.variable;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import de.xima.fc.form.expression.iface.parse.IHeaderNode;
import de.xima.fc.form.expression.iface.parse.IScopeDefinitions;

public enum DummyScopeDefinitions implements IScopeDefinitions {
	INSTANCE;

	@Override
	public ImmutableCollection<String> getExternal() {
		return ImmutableList.of();
	}

	@Override
	public ImmutableCollection<IHeaderNode> getGlobal() {
		return ImmutableList.of();
	}

	@Override
	public ImmutableMap<String, ImmutableCollection<IHeaderNode>> getManual() {
		return ImmutableMap.of();
	}
}
