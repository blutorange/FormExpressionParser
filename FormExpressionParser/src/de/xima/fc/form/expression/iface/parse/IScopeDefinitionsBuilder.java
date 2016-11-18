package de.xima.fc.form.expression.iface.parse;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.grammar.Node;

public interface IScopeDefinitionsBuilder {
	public boolean hasGlobal(@Nonnull String name);
	public boolean hasManual(@Nonnull String scope, @Nonnull String name);
	public boolean hasManual(@Nonnull String scope);
	public boolean hasExternal(@Nonnull String name);

	public void addGlobal(@Nonnull String name, @Nonnull Node node);
	public void addManual(@Nonnull String scope, @Nonnull String name, @Nonnull Node node);
	public void addExternal(@Nonnull String name);

	@Nonnull
	public IScopeDefinitions build();
}