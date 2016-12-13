package de.xima.fc.form.expression.impl.library;

import java.util.Collection;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;

import com.google.common.collect.ImmutableMap;

import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.exception.evaluation.VariableNotDefinedException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.ILibrary;
import de.xima.fc.form.expression.iface.evaluate.ILibraryContractFactory;
import de.xima.fc.form.expression.iface.evaluate.ILibraryScope;
import de.xima.fc.form.expression.iface.parse.ILibraryScopeContractFactory;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.util.Void;

@Immutable
@ParametersAreNonnullByDefault
public class GenericLibraryContractFactory implements ILibraryContractFactory {
	private static final long serialVersionUID = 1L;

	private final ImmutableMap<String, ILibraryScopeContractFactory<Void>> library;

	@SafeVarargs
	private GenericLibraryContractFactory(final ILibraryScopeContractFactory<Void>... libraryScopeList) {
		final ImmutableMap.Builder<String, ILibraryScopeContractFactory<Void>> builder = new ImmutableMap.Builder<>();
		for (final ILibraryScopeContractFactory<Void> f : libraryScopeList)
			builder.put(f.getScopeName(), f);
		library = builder.build();
	}

	@Override
	public ILibrary makeLibrary() {
		final ImmutableMap.Builder<String, ILibraryScope<Void>> builder = new ImmutableMap.Builder<>();
		for (final ILibraryScopeContractFactory<Void> lib : library.values()) {
			final ILibraryScope<Void> scope = lib.makeScope();
			builder.put(scope.getScopeName(), scope).build();
		}
		return new LibraryImpl(builder.build());
	}


	@Override
	public boolean isProvidingScope(final String scope) {
		return library.containsKey(scope);
	}

	@Nullable
	@Override
	public ILibraryScopeContractFactory<Void> getScopeFactory(final String scope) {
		return library.get(scope);
	}

	@SafeVarargs
	public static ILibraryContractFactory createWith(final ILibraryScopeContractFactory<Void>... libraryScopeList) {
		return new GenericLibraryContractFactory(libraryScopeList);
	}

	@Immutable
	private class LibraryImpl implements ILibrary {
		private final ImmutableMap<String, ILibraryScope<Void>> map;

		private LibraryImpl(final ImmutableMap<String, ILibraryScope<Void>> map) {
			this.map = map;
		}

		@SuppressWarnings({ "null", "unused" })
		@Override
		public ALangObject getVariable(final String scope, final String name, final IEvaluationContext ec)
				throws EvaluationException {
			final ILibraryScope<Void> customScope = map.get(scope);
			if (customScope == null)
				throw new VariableNotDefinedException(scope, name, ec);
			return customScope.fetch(name, Void.NULL, ec);
		}

		@Override
		public void reset() {
		}

		@Override
		public Collection<String> getProvidedScopes() {
			return map.keySet();
		}
	}
}
