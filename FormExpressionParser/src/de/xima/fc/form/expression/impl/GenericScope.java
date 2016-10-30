package de.xima.fc.form.expression.impl;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.collections4.map.MultiKeyMap;

import com.google.common.collect.ImmutableMap;

import de.xima.fc.form.expression.context.ICustomScope;
import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IScope;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.object.ALangObject;

/**
 * <b>Not thread-safe!</b>
 *
 * @author mad_gaksha
 */
public class GenericScope implements IScope {
	/** Stores scopes defined by the program. */
	private final MultiKeyMap<String, ALangObject> map;
	/** Stores predefined scopes provided by the environment, eg math. */
	private final ImmutableMap<String, ICustomScope> custom;

	private GenericScope(final MultiKeyMap<String, ALangObject> map, final ImmutableMap<String, ICustomScope> custom) {
		this.map = map;
		this.custom = custom;
	}

	@Override
	public ALangObject getVariable(final String scope, final String name, final IEvaluationContext ec)
			throws EvaluationException {
		ALangObject value = map.get(scope, name);
		if (value != null)
			return value;
		if (custom != null) {
			final ICustomScope customScope = custom.get(scope);
			value = customScope != null ? customScope.fetch(name) : null;
		}
		return value != null ? value
				: ec.getExternalContext() != null ? ec.getExternalContext().fetchScopedVariable(scope, name, ec) : null;
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
			if (map == null)
				map = MultiKeyMap.multiKeyMap(new HashedMap<MultiKey<? extends String>, ALangObject>(initialSize));
			return map;
		}

		private com.google.common.collect.ImmutableMap.Builder<String, ICustomScope> getCustom() {
			if (custom == null)
				custom = new com.google.common.collect.ImmutableMap.Builder<>();
			return custom;
		}

		public Builder addObjects(final String scope, final Map<String, ALangObject> objects) {
			for (final Entry<String, ALangObject> entry : objects.entrySet())
				getMap().put(scope, entry.getKey(), entry.getValue());
			return this;
		}

		public Builder addCustomScope(final ICustomScope customScope) {
			if (customScope == null)
				return this;
			useCustomScope = true;
			getCustom().put(customScope.getScopeName(), customScope);
			return this;
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

	public static IScope getNewEmptyScope(final int initialSize) {
		final MultiKeyMap<String, ALangObject> map = MultiKeyMap
				.multiKeyMap(new HashedMap<MultiKey<? extends String>, ALangObject>(initialSize));
		return new GenericScope(map, null);
	}

	@Override
	public void reset() {
		map.clear();
	}
}