package de.xima.fc.form.expression.iface.parse;

import java.util.Iterator;
import java.util.Map.Entry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IScopeDefinitionsBuilder {
	public void addGlobal(@Nonnull String name, @Nonnull IHeaderNode node);
	public void addManual(@Nonnull String scope, @Nonnull String name, @Nonnull IHeaderNode node);
	public void addExternal(@Nonnull String name);
	
	@Nullable
	public IHeaderNode getGlobal(@Nonnull String name);
	@Nullable
	public IHeaderNode getManual(@Nonnull String scope, @Nonnull String name);
	
	@Nonnull
	public Iterator<Entry<String,IHeaderNode>> getGlobal();
	@Nullable
	public Iterator<Entry<String,IHeaderNode>> getManual(@Nonnull String scope);
	@Nonnull
	public Iterator<String> getManual();
	@Nonnull
	public Iterator<IHeaderNode> getManualAll();
	@Nonnull
	public Iterator<String> getExternal();

	public boolean hasGlobal(@Nonnull String name);
	public boolean hasManual(@Nonnull String scope, @Nonnull String name);
	public boolean hasManual(@Nonnull String scope);
	public boolean hasExternal(@Nonnull String name);
	
	@Nonnull
	public IScopeDefinitions build();
}