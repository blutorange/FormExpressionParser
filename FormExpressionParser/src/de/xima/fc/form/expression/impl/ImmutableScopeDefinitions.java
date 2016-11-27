package de.xima.fc.form.expression.impl;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import de.xima.fc.form.expression.iface.parse.IHeaderNode;
import de.xima.fc.form.expression.iface.parse.IScopeDefinitions;

public class ImmutableScopeDefinitions implements IScopeDefinitions {
	private static final long serialVersionUID = 1L;

	@Nonnull
	private final ImmutableCollection<String> externalSet;
	@Nonnull
	private final ImmutableCollection<IHeaderNode> globalList;
	@Nonnull
	private final ImmutableMap<String, ImmutableCollection<IHeaderNode>> manualMap;

	private int globalVariableCount;
	private int globalFunctionCount;

	public ImmutableScopeDefinitions(final @Nonnull Map<String, IHeaderNode> globalMap,
			final Map<String, Map<String, IHeaderNode>> manualMap, final @Nonnull Set<String> externalSet) {
		this.externalSet = ImmutableSet.copyOf(externalSet);

		final Collection<IHeaderNode> chn = globalMap.values();
		if (chn != null)
			this.globalList = ImmutableList.copyOf(chn);
		else
			this.globalList = ImmutableList.of();
		
		for (final IHeaderNode global : globalList)
			if (global.isFunction()) ++globalFunctionCount;
			else ++globalVariableCount;

		final ImmutableMap.Builder<String, ImmutableCollection<IHeaderNode>> manualBuilder = new ImmutableMap.Builder<>();
		for (final Entry<String, Map<String, IHeaderNode>> entry : manualMap.entrySet()) {
			final String key = entry.getKey();
			final Collection<IHeaderNode> vals = entry.getValue().values();
			if (vals != null && key != null)
				manualBuilder.put(key, new ImmutableList.Builder<IHeaderNode>().addAll(vals).build());
		}
		this.manualMap = manualBuilder.build();
	}

	@Override
	public ImmutableCollection<String> getExternal() {
		return externalSet;
	}

	@Override
	public ImmutableCollection<IHeaderNode> getGlobal() {
		return globalList;
	}

	@Override
	public ImmutableMap<String, ImmutableCollection<IHeaderNode>> getManual() {
		return manualMap;
	}

	@Override
	public boolean hasGlobalVariable() {
		return globalVariableCount > 0;
	}

	@Override
	public boolean hasGlobalFunction() {
		return globalFunctionCount > 0;
	}
}