package de.xima.fc.form.expression.impl;

import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.collections4.map.MultiKeyMap;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

import de.xima.fc.form.expression.context.ICustomScope;
import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IExternalContext;
import de.xima.fc.form.expression.context.IScope;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.util.NullUtil;

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

	@SuppressWarnings("null")
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
		final Optional<IExternalContext> ex = ec.getExternalContext();
		return value != null ? value : ex.isPresent() ? ex.get().fetchScopedVariable(scope, name, ec) : null;
	}

	@Override
	public void setVariable(final String scope, final String name, final ALangObject value) throws EvaluationException {
		map.put(scope, name, value);
	}

	public final static class Builder {
		@Nullable private MultiKeyMap<String, ALangObject> map;
		@Nullable private com.google.common.collect.ImmutableMap.Builder<String, ICustomScope> custom;
		private boolean useCustomScope = false;
		private final int initialSize;

		public Builder() {
			this(16);
		}

		@Nonnull
		public Builder(final int initialSize) {
			this.initialSize = initialSize;
			init();
		}

		private void init() {
			map = null;
			custom = null;
			useCustomScope = false;
		}

		@Nonnull
		private MultiKeyMap<String, ALangObject> getMap() {
			if (map != null)
				return map;
			return map = NullUtil.checkNotNull(
					MultiKeyMap.multiKeyMap(new HashedMap<MultiKey<? extends String>, ALangObject>(initialSize)));
		}

		@Nonnull
		private com.google.common.collect.ImmutableMap.Builder<String, ICustomScope> getCustom() {
			if (custom != null)
				return custom;
			return custom = new com.google.common.collect.ImmutableMap.Builder<>();
		}

		@Nonnull
		public Builder addObjects(@Nonnull final String scope, @Nonnull final Map<String, ALangObject> objects) {
			for (final Entry<String, ALangObject> entry : objects.entrySet())
				getMap().put(scope, entry.getKey(), entry.getValue());
			return this;
		}

		@Nonnull
		public Builder addCustomScope(@Nullable final ICustomScope customScope) {
			if (customScope == null)
				return this;
			useCustomScope = true;
			getCustom().put(customScope.getScopeName(), customScope);
			return this;
		}

		@Nonnull
		public IScope build() {
			final IScope scope = new GenericScope(getMap(), useCustomScope ? getCustom().build() : null);
			init();
			return scope;
		}
	}

	@Nonnull
	public static IScope getNewEmptyScope() {
		return getNewEmptyScope(16);
	}

	@Nonnull
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