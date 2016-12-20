package de.xima.fc.form.expression.iface.parse;

import java.io.Serializable;

import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;

@ParametersAreNonnullByDefault
public interface IScopeDefinitions extends Serializable {
	public ImmutableCollection<String> getExternal();
	public ImmutableCollection<IHeaderNode> getGlobal();
	public ImmutableMap<String, ImmutableCollection<IHeaderNode>> getManual();
}