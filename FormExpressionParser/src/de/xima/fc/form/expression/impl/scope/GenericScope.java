package de.xima.fc.form.expression.impl.scope;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.google.common.collect.ImmutableMap;

import de.xima.fc.form.expression.iface.context.ICustomScope;
import de.xima.fc.form.expression.object.ALangObject;

@Immutable
public class GenericScope implements ICustomScope {
	@Nonnull
	private final String name;
	@Nonnull
	private final ImmutableMap<String, ALangObject> map;
	
	public GenericScope(@Nonnull String name, @Nonnull Map<String,ALangObject> map) {
		this.name = name;
		this.map = new ImmutableMap.Builder<String,ALangObject>().putAll(map).build();
	}
	
	@Override
	public ALangObject fetch(@Nonnull String variableName) {
		return map.get(variableName);
	}

	@Override
	public String getScopeName() {
		return name;
	}
}
