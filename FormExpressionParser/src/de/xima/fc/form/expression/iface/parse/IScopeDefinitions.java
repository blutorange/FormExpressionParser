package de.xima.fc.form.expression.iface.parse;

import java.util.Iterator;
import java.util.Map.Entry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.grammar.Node;

public interface IScopeDefinitions {
	@Nullable
	public Node getGlobal(@Nonnull String name);
	@Nullable
	public Node getManual(@Nonnull String scope, @Nonnull String name);
	
	@Nonnull
	public Iterator<Entry<String,Node>> getGlobal();
	@Nullable
	public Iterator<Entry<String,Node>> getManualFor(@Nonnull String scope);
	@Nonnull
	public Iterator<String> getManual();
	@Nonnull
	public Iterator<String> getExternal();

	public boolean hasGlobal(@Nonnull String name);
	public boolean hasManual(@Nonnull String scope, @Nonnull String name);
	public boolean hasManual(@Nonnull String scope);
	public boolean hasExternal(@Nonnull String name);
}