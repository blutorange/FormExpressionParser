package de.xima.fc.form.expression.impl;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.collections4.map.MultiKeyMap;

import com.google.common.collect.ImmutableMap;

import de.xima.fc.form.expression.context.ICustomScope;
import de.xima.fc.form.expression.context.IScope;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.object.ALangObject;

/**
 * <b>Not thread-safe!</b>
 * @author mad_gaksha
 */
public class GenericScope implements IScope {
	private final MultiKeyMap<String, ALangObject> map;
	private final ImmutableMap<String, ICustomScope> custom;

	private GenericScope(final MultiKeyMap<String, ALangObject> map, final ImmutableMap<String, ICustomScope> custom) {
		this.map = map;
		this.custom = custom;
	}

	@Override
	public ALangObject getVariable(final String scope, final String name) throws EvaluationException {
		final ALangObject value = map.get(scope, name);
		if (custom == null || value != null) return value;
		final ICustomScope customScope = custom.get(scope);
		return customScope == null ? null : customScope.fetch(name);
	}

	@Override
	public void setVariable(final String scope, final String name, final ALangObject value) throws EvaluationException {
		map.put(scope, name, value);
	}

	public final static class Builder {
		private MultiKeyMap<String, ALangObject> map;
		private com.google.common.collect.ImmutableMap.Builder<String, ICustomScope> custom;
		private boolean useCustomScope = false;
		private final int initialSize;

		public Builder() {
			this(16);
		}
		public Builder(final int initialSize) {
			this.initialSize = initialSize;
			init();
		}

		private void init() {
			map = null;
			custom = null;
			useCustomScope = false;
		}

		private MultiKeyMap<String, ALangObject> getMap() {
			if (map == null) map = MultiKeyMap.multiKeyMap(new HashedMap<MultiKey<? extends String>, ALangObject>(initialSize));
			return map;
		}

		private com.google.common.collect.ImmutableMap.Builder<String, ICustomScope> getCustom() {
			if (custom == null) custom = new com.google.common.collect.ImmutableMap.Builder<>();
			return custom;
		}

		public void addObjects(final String scope, final Map<String, ALangObject> objects) {
			for (final Entry<String, ALangObject> entry : objects.entrySet())
				getMap().put(scope, entry.getKey(), entry.getValue());
		}

		public void addCustomScope(final ICustomScope customScope) {
			if (customScope == null ) return;
			useCustomScope = true;
			getCustom().put(customScope.getScopeName(), customScope);
		}

		public IScope build() {
			final IScope scope = new GenericScope(getMap(), useCustomScope ? getCustom().build() : null);
			init();
			return scope;
		}
	}
	
	public static IScope getNewEmptyScope() {
		return getNewEmptyScope(16);
	}
	
	public static IScope getNewEmptyScope(int initialSize) {
		final MultiKeyMap<String, ALangObject> map = MultiKeyMap.multiKeyMap(new HashedMap<MultiKey<? extends String>, ALangObject>(initialSize));
		return new GenericScope(map, null);
	}

	@Override
	public Void reset() {
		map.clear();
		return null;
	}
}