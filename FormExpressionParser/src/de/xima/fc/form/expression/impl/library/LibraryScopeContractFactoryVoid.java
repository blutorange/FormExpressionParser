package de.xima.fc.form.expression.impl.library;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.common.collect.ImmutableMap;

import de.xima.fc.form.expression.enums.EVariableSource;
import de.xima.fc.form.expression.exception.evaluation.VariableNotDefinedException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.ILibraryScope;
import de.xima.fc.form.expression.iface.factory.ILibraryScopeContractFactory;
import de.xima.fc.form.expression.iface.parse.IVariableProvider;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.impl.variable.provider.SimpleFunctionVariableProvider;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.util.Void;

@Immutable
@NonNullByDefault
public final class LibraryScopeContractFactoryVoid implements ILibraryScopeContractFactory<Void> {
	private static final long serialVersionUID = 1L;

	private final String name;
	private final ImmutableMap<String, IVariableProvider<?>> map;

	protected LibraryScopeContractFactoryVoid(final String name, final ImmutableMap<String, IVariableProvider<?>> map) {
		this.name = name;
		this.map = map;
	}

	@Nullable
	@Override
	public IVariableType getVariableType(final String variableName) {
		final IVariableProvider<?> res = map.get(variableName);
		return res != null ? res.getType() : null;
	}

	@Override
	public String getScopeName() {
		return name;
	}

	@Override
	public ILibraryScope<Void> make() {
		return new LibImpl(map);
	}

	@Override
	public boolean isProviding(final String variableName) {
		return map.containsKey(variableName);
	}

	@Override
	public EVariableSource getSource() {
		return EVariableSource.LIBRARY;
	}

	private class LibImpl implements ILibraryScope<Void> {
		private final ImmutableMap<String, IVariableProvider<?>> provider;
		private final Map<String, ALangObject> objectMap;
		public LibImpl(final ImmutableMap<String, IVariableProvider<?>> provider) {
			this.provider = provider;
			this.objectMap = new HashMap<>();
			reset();
		}
		@Override
		public ALangObject fetch(final String variableName, final Void object, final IEvaluationContext ec)
				throws VariableNotDefinedException {
			final ALangObject res = objectMap.get(variableName);
			if (res == null)
				throw new VariableNotDefinedException(variableName, ec);
			return res;
		}

		@Override
		public void reset() {
			objectMap.clear();
			for (final Entry<String, IVariableProvider<?>> entry : provider.entrySet())
				objectMap.put(entry.getKey(), entry.getValue().make());
		}
	}

	public static class Builder {
		@Nullable
		private ImmutableMap.Builder<String, IVariableProvider<?>> builder;
		private final String scopeName;

		private ImmutableMap.Builder<String, IVariableProvider<?>> getBuilder() {
			if (builder != null)
				return builder;
			return builder = new ImmutableMap.Builder<>();
		}

		public Builder(final String scopeName) {
			this.scopeName = scopeName;
		}

		/**
		 * Adds a variable with a given name.
		 * @param variableName Name of the variable to add.
		 * @param value Value of the variable, including its type.
		 * @return this for chaining.
		 */
		public Builder addVariable(final String variableName, final IVariableProvider<?> value) {
			getBuilder().put(variableName, value);
			return this;
		}

		public Builder addFunction(final SimpleFunctionVariableProvider<?> value) {
			getBuilder().put(value.getDeclaredName(), value);
			return this;
		}

		public ILibraryScopeContractFactory<Void> build() {
			final ILibraryScopeContractFactory<Void> factory = new LibraryScopeContractFactoryVoid(scopeName,
					getBuilder().build());
			builder = null;
			return factory;
		}
	}
}