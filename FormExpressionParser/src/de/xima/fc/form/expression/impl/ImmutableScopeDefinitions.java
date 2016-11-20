package de.xima.fc.form.expression.impl;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.UnmodifiableIterator;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.IScopeDefinitions;

public class ImmutableScopeDefinitions implements IScopeDefinitions {
	@Nonnull
	private final ImmutableMap<String,Node> globalMap;
	@Nonnull
	private final ImmutableMap<String, ImmutableMap<String,Node>> manualMap;
	@Nonnull
	private final ImmutableSet<String> externalSet;
	
	@SuppressWarnings("null")
	public ImmutableScopeDefinitions(final @Nonnull Map<String, Node> globalMap,
			final Map<String, Map<String, Node>> manualMap, final @Nonnull Set<String> externalSet) {
		this.globalMap = ImmutableMap.copyOf(globalMap);
		this.externalSet = ImmutableSet.copyOf(externalSet);
		final ImmutableMap.Builder<String, ImmutableMap<String,Node>> builder = new ImmutableMap.Builder<>();
		for (final Entry<String,Map<String,Node>> entry : manualMap.entrySet())
			builder.put(entry.getKey(), ImmutableMap.copyOf(entry.getValue()));
		this.manualMap = builder.build();
	}
	
	@Override
	public Node getGlobal(final String name) {
		return globalMap.get(name);
	}

	@SuppressWarnings("null")
	@Override
	public Node getManual(final String scope, final String name) {
		final Map<String,Node> m = manualMap.get(scope);
		return m != null ? m.get(name) : null;
	}

	@Override
	public UnmodifiableIterator<Entry<String, Node>> getGlobal() {
		return globalMap.entrySet().iterator();
	}

	@SuppressWarnings("null")
	@Override
	public Iterator<Entry<String, Node>> getManualFor(final String scope) {
		final Map<String,Node> m = manualMap.get(scope);
		return m != null ? m.entrySet().iterator() : null;
	}

	@Override
	public Iterator<String> getManual() {
		return manualMap.keySet().iterator();
	}

	@Override
	public Iterator<String> getExternal() {
		return externalSet.iterator();
	}

	@Override
	public boolean hasGlobal(final String name) {
		return globalMap.containsKey(name);
	}

	@SuppressWarnings("null")
	@Override
	public boolean hasManual(final String scope, final String name) {
		final Map<String,Node> m = manualMap.get(scope);
		return m != null ? m.containsKey(name) : false;
	}

	@Override
	public boolean hasManual(final String scope) {
		return manualMap.containsKey(scope);
	}

	@Override
	public boolean hasExternal(final String name) {
		return externalSet.contains(name);
	}
}
