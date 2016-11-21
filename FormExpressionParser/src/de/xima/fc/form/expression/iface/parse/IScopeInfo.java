package de.xima.fc.form.expression.iface.parse;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.EVariableSource;

public interface IScopeInfo {
	public boolean isProviding(String variableName);
	@Nonnull
	public String getScopeName();
	@Nonnull
	public EVariableSource getSource();
}