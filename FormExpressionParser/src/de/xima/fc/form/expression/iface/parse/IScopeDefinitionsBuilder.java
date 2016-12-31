package de.xima.fc.form.expression.iface.parse;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public interface IScopeDefinitionsBuilder {
	public void addGlobal(String name, IHeaderNode node);
	public void addGlobal(Map<String, IHeaderNode> entries);
	public void addManual(String scope, String name, IHeaderNode node);
	public void addManual(String scope, Map<String, IHeaderNode> entries);
	public void addExternal(String name);
	public void addExternal(Collection<String> names);

	public Set<String> getExternal();

	@Nullable
	public IHeaderNode getGlobal(String name);
	public Map<String,IHeaderNode> getGlobal();

	@Nullable
	public IHeaderNode getManual(String scope, String name);
	public Map<String, Map<String, IHeaderNode>> getManual();

	public boolean hasGlobal(String name);
	public boolean hasManual(String scope, String name);
	public boolean hasManual(String scope);
	public boolean hasExternal(String name);

	public IScopeDefinitions build();
}