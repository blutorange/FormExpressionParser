package de.xima.fc.form.expression.impl.library;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;

import com.google.common.collect.ImmutableMap;

import de.xima.fc.form.expression.exception.FormExpressionException;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.exception.evaluation.VariableNotDefinedException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.ILibrary;
import de.xima.fc.form.expression.iface.evaluate.ILibraryScope;
import de.xima.fc.form.expression.iface.factory.ILibraryContractFactory;
import de.xima.fc.form.expression.iface.factory.ILibraryScopeContractFactory;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.util.Void;

/**
 * <p>
 * A library simply consisting of a {@link ILibraryScopeContractFactory} for
 * each supported scope. Retrieving values is delegated to the corresponding
 * library scope.
 * </p>
 * <p>
 * As it is immutable, you do not need to create a new instance every time.
 * </p>
 *
 * @author madgaksha
 */
@Immutable
@ParametersAreNonnullByDefault
public final class LibraryContractFactoryVoid implements ILibraryContractFactory {
	private static final long serialVersionUID = 1L;
	protected final ImmutableMap<String, ILibraryScopeContractFactory<Void>> library;

	@SafeVarargs
	private LibraryContractFactoryVoid(final ILibraryScopeContractFactory<Void>... libraryScopeList) {
		final ImmutableMap.Builder<String, ILibraryScopeContractFactory<Void>> builder = new ImmutableMap.Builder<>();
		for (final ILibraryScopeContractFactory<Void> f : libraryScopeList) {
			if (f == null)
				throw new IllegalArgumentException();
			builder.put(f.getScopeName(), f);
		}
		library = builder.build();
	}

	@Override
	public ILibrary make() {
		return new LibImpl();
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

	/**
	 * Builder for creating instances.
	 * @param libraryScopeList
	 * @return A library factory with the given library scopes.
	 * @throws FormExpressionException When any of the given scopes is <code>null</code>.
	 */
	@SafeVarargs
	public static ILibraryContractFactory buildWith(final ILibraryScopeContractFactory<Void>... libraryScopeList)
			throws FormExpressionException {
		return new LibraryContractFactoryVoid(libraryScopeList);
	}

	private class LibImpl implements ILibrary {
		private final Map<String, ILibraryScope<Void>> map;

		protected LibImpl() {
			map = new HashMap<>(library.size());
			for (final ILibraryScopeContractFactory<Void> lib : library.values()) {
				final ILibraryScope<Void> scope = lib.make();
				map.put(lib.getScopeName(), scope);
			}
		}

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
			for (final ILibraryScope<?> s : map.values())
				s.reset();
		}

		@SuppressWarnings("null")
		@Override
		public Collection<String> getProvidedScopes() {
			return map.keySet();
		}
	}
}