package de.xima.fc.form.expression.impl.externalcontext;

import java.io.IOException;
import java.util.Map.Entry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.ImmutableMap;

import de.xima.fc.form.expression.exception.evaluation.EmbedmentOutputException;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.exception.evaluation.VariableNotDefinedException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IExternalContext;
import de.xima.fc.form.expression.iface.evaluate.IExternalContextContractFactory;
import de.xima.fc.form.expression.iface.evaluate.ILibraryScope;
import de.xima.fc.form.expression.iface.parse.ILibraryScopeContractFactory;
import de.xima.fc.form.expression.impl.library.FormcycleScope;
import de.xima.fc.form.expression.object.ALangObject;

@ParametersAreNonnullByDefault
public enum FormcycleExternalContextContractFactory implements IExternalContextContractFactory<Formcycle> {
	INSTANCE(FormcycleScope.FORM_FIELD);
	private ImmutableMap<String, ILibraryScopeContractFactory<Formcycle>> map;

	@SafeVarargs
	private FormcycleExternalContextContractFactory(final ILibraryScopeContractFactory<Formcycle>... factories) {
		final ImmutableMap.Builder<String, ILibraryScopeContractFactory<Formcycle>> builder = new ImmutableMap.Builder<>();
		for (final ILibraryScopeContractFactory<Formcycle> f : factories)
			builder.put(f.getScopeName(), f);
		map = builder.build();
	}

	@Override
	public IExternalContext makeExternalContext(final Formcycle formcycle) {
		final ImmutableMap.Builder<String, ILibraryScope<Formcycle>> builder = new ImmutableMap.Builder<>();
		for (final Entry<String, ILibraryScopeContractFactory<Formcycle>> entry : map.entrySet())
			// Key cannot be null as IParametrizedLibraryScope#getScopeName is nonnull.
			builder.put(entry.getKey(), entry.getValue().makeScope());
		return new FormcycleExternalContext(formcycle, builder.build());
	}

	@Override
	@Nullable
	public ILibraryScopeContractFactory<Formcycle> getScopeFactory(final String scope) {
		return map.get(scope);
	}

	@Override
	public boolean isProvidingScope(final String scope) {
		return map.containsKey(scope);
	}

	public static class FormcycleExternalContext extends AHtmlExternalContext {
		private final Formcycle formcycle;
		private final ImmutableMap<String, ILibraryScope<Formcycle>> map;

		public FormcycleExternalContext(final Formcycle formcycle, final ImmutableMap<String, ILibraryScope<Formcycle>> map) {
			this.formcycle = formcycle;
			this.map = map;
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
	}
}
