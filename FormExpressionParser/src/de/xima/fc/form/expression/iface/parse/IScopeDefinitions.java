package de.xima.fc.form.expression.iface.parse;

import java.io.Serializable;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;

public interface IScopeDefinitions extends Serializable {
	public ImmutableCollection<String> getExternal();
	public ImmutableCollection<IHeaderNode> getGlobal();
	public ImmutableMap<String, ImmutableCollection<IHeaderNode>> getManual();
	/** @return Whether the globals contain any variable declaration(s). */
	public boolean hasGlobalVariable();
	/** @return Whether the globals contain any function declaration(s). */
	public boolean hasGlobalFunction();
}