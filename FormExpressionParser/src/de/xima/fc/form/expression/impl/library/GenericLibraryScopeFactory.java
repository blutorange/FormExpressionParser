package de.xima.fc.form.expression.impl.library;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;

import com.google.common.collect.ImmutableMap;

import de.xima.fc.form.expression.enums.EVariableSource;
import de.xima.fc.form.expression.exception.evaluation.VariableNotDefinedException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.ILibraryScope;
import de.xima.fc.form.expression.iface.parse.ILibraryScopeContractFactory;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.util.Void;

@Immutable
@ParametersAreNonnullByDefault
public final class GenericLibraryScopeFactory implements ILibraryScope<Void>, ILibraryScopeContractFactory<Void> {
	private final String name;
	private final ImmutableMap<String, LibVar> map;

	private GenericLibraryScopeFactory(final String name, final ImmutableMap<String, LibVar> map) {
		this.name = name;
		this.map = map;
	}

	@SuppressWarnings({ "unused", "null" })
	@Override
	public ALangObject fetch(final String variableName, final Void object, final IEvaluationContext ec)
			throws VariableNotDefinedException {
		final LibVar res = map.get(variableName);
		// Eclipse/Guava screws up the checking. map#get *can* return null.
		if (res == null)
			throw new VariableNotDefinedException(variableName, ec);
		return res.value;
	}

	@SuppressWarnings({ "null" })
	@Nullable
	@Override
	public IVariableType getVariableType(final String variableName) {
		final LibVar res = map.get(variableName);
		return res != null ? res.type : null;
	}

	@Override
	public String getScopeName() {
		return name;
	}

	@Override
	public ILibraryScope<Void> makeScope() {
		return this;
	}

	@Override
	public boolean isProviding(final String variableName) {
		return map.containsKey(variableName);
	}

	@Override
	public EVariableSource getSource() {
		return EVariableSource.LIBRARY;
	}

	public static class Builder {
		@Nullable
		private ImmutableMap.Builder<String, LibVar> builder;
		private final String scopeName;

		private ImmutableMap.Builder<String, LibVar> getBuilder() {
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
		public Builder addVariable(final String variableName, final LibVar value) {
			getBuilder().put(variableName, value);
			return this;
		}

		public ILibraryScopeContractFactory<Void> build() {
			final ILibraryScopeContractFactory<Void> factory = new GenericLibraryScopeFactory(scopeName,
					getBuilder().build());
			builder = null;
			return factory;
		}
	}
}