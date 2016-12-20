package de.xima.fc.form.expression.impl.externalcontext;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.ImmutableMap;

import de.xima.fc.form.expression.exception.evaluation.EmbedmentOutputException;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.exception.evaluation.VariableNotDefinedException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IExternalContext;
import de.xima.fc.form.expression.iface.evaluate.ILibraryScope;
import de.xima.fc.form.expression.iface.factory.IExternalContextContractFactory;
import de.xima.fc.form.expression.iface.factory.ILibraryScopeContractFactory;
import de.xima.fc.form.expression.impl.library.ELibraryScopeContractFactoryFormcycle;
import de.xima.fc.form.expression.object.ALangObject;

@ParametersAreNonnullByDefault
public enum EExternalContextContractFactoryFormcycle implements IExternalContextContractFactory<Formcycle> {
	INSTANCE(ELibraryScopeContractFactoryFormcycle.values());

	private ImmutableMap<String, ILibraryScopeContractFactory<Formcycle>> library;

	@SafeVarargs
	private EExternalContextContractFactoryFormcycle(final ILibraryScopeContractFactory<Formcycle>... factories) {
		final ImmutableMap.Builder<String, ILibraryScopeContractFactory<Formcycle>> builder = new ImmutableMap.Builder<>();
		for (final ILibraryScopeContractFactory<Formcycle> f : factories)
			builder.put(f.getScopeName(), f);
		library = builder.build();
	}

	@Override
	public IExternalContext make(final Formcycle formcycle) {
		return new FormcycleExternalContext(formcycle);
	}

	@Override
	@Nullable
	public ILibraryScopeContractFactory<Formcycle> getScopeFactory(final String scope) {
		return library.get(scope);
	}

	@Override
	public boolean isProvidingScope(final String scope) {
		return library.containsKey(scope);
	}

	private class FormcycleExternalContext extends AHtmlExternalContext {
		private final Formcycle formcycle;
		private final Map<String, ILibraryScope<Formcycle>> map;

		public FormcycleExternalContext(final Formcycle formcycle) {
			map = new HashMap<>(library.size());
			for (final ILibraryScopeContractFactory<Formcycle> lib : library.values()) {
				final ILibraryScope<Formcycle> scope = lib.make();
				map.put(lib.getScopeName(), scope);
			}
			this.formcycle = formcycle;
		}

		@Override
		protected void output(@Nullable final String html) throws EmbedmentOutputException {
			try {
				formcycle.getWriter().write(html);
				formcycle.getWriter().flush();
			}
			catch (final IOException e) {
				throw new EmbedmentOutputException(e, this);
			}
		}

		@Override
		protected void finishOutput() throws EmbedmentOutputException {
			try {
				formcycle.getWriter().flush();
			}
			catch (final IOException e) {
				throw new EmbedmentOutputException(e, this);
			}
		}

		@Override
		public ALangObject fetchScopedVariable(@Nonnull final String scope, @Nonnull final String name,
				@Nonnull final IEvaluationContext ec) throws EvaluationException {
			final ILibraryScope<Formcycle> s = map.get(scope);
			if (s == null)
				throw new VariableNotDefinedException(scope, name, ec);
			return s.fetch(name, formcycle, ec);
		}

		@Override
		public void reset() {
			for (final ILibraryScope<?> s : map.values())
				s.reset();
		}
	}
}
