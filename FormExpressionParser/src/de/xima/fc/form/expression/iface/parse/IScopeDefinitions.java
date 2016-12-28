package de.xima.fc.form.expression.iface.parse;

import java.io.Serializable;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;

@NonNullByDefault
public interface IScopeDefinitions extends Serializable {
	public ImmutableCollection<String> getExternal();
	public ImmutableCollection<IHeaderNode> getGlobal();
	public ImmutableMap<String, ImmutableCollection<IHeaderNode>> getManual();
}