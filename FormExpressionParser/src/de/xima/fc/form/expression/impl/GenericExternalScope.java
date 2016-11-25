package de.xima.fc.form.expression.impl;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.google.common.collect.ImmutableMap;

import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.exception.evaluation.VariableNotDefinedException;
import de.xima.fc.form.expression.iface.context.ICustomScope;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.iface.context.IExternalScope;
import de.xima.fc.form.expression.impl.scope.GenericScope;
import de.xima.fc.form.expression.object.ALangObject;

/**
 * @author mad_gaksha
 */
@Immutable
public class GenericExternalScope implements IExternalScope {
	/** Stores predefined scopes provided by the environment, eg math. */
	@Nonnull
	private final ImmutableMap<String, ICustomScope> custom;
	private String[] scopes;

	private GenericExternalScope(@Nonnull final ImmutableMap<String, ICustomScope> custom) {
		this.custom = custom;
	}

	@SuppressWarnings({ "unused", "null" })
	@Override
	public ALangObject getVariable(final String scope, final String name, final IEvaluationContext ec)
			throws EvaluationException {
		final ICustomScope customScope = custom.get(scope);
		if (customScope == null)
			throw new VariableNotDefinedException(scope, name, ec);
		return customScope.fetch(name, ec);
	}

	public final static class Builder {
		@Nullable private com.google.common.collect.ImmutableMap.Builder<String, ICustomScope> custom;
		@Nonnull
		public Builder() {
			init();
		}

		private void init() {
			custom = null;
		}

		@Nonnull
		private com.google.common.collect.ImmutableMap.Builder<String, ICustomScope> getCustom() {
			if (custom != null)
				return custom;
			return custom = new com.google.common.collect.ImmutableMap.Builder<>();
		}

		@Nonnull
		public Builder addObjects(@Nonnull final String scope, @Nonnull final Map<String, ALangObject> objects) {
			return addCustomScope(new GenericScope(scope, objects));
		}

		@Nonnull
		public Builder addCustomScope(@Nonnull final ICustomScope customScope) {
			getCustom().put(customScope.getScopeName(), customScope);
			return this;
		}

		@Nonnull
		public IExternalScope build() {
			final IExternalScope scope = new GenericExternalScope(getCustom().build());
			init();
			return scope;
		}
	}

	@Nonnull
	public static IExternalScope getEmptyScopeInstance() {
		return InstanceHolder.INSTANCE;
	}

	@Override
	public void reset() {
	}
	
	@Override
	public String[] getProvidedScopes() {
		if (scopes != null)
			return scopes;
		return scopes = custom.keySet().toArray(new String[custom.size()]);
	}

	private static class InstanceHolder {
		@Nonnull
		public final static IExternalScope INSTANCE = new Builder().build();
	}
}