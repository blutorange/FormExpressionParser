package de.xima.fc.form.expression.impl.externalcontext;

import java.io.IOException;
import java.io.Writer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import de.xima.fc.form.expression.exception.evaluation.EmbedmentOutputException;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.exception.evaluation.VariableNotDefinedException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.parse.IScopeInfo;
import de.xima.fc.form.expression.impl.scope.FormcycleScope;
import de.xima.fc.form.expression.impl.writer.SystemWriter;
import de.xima.fc.form.expression.object.ALangObject;

// Dummy for illustration, remove this and use the class FormVersion etc.
public class FormcycleExternalContext extends AHtmlExternalContext {
	@Nullable
	private Writer writer;
	@Nonnull
	private final Formcycle formcycle;

	// TODO replace with an actual type to be used.
	private final static ImmutableMap<String, FormcycleScope> scopeMap;
	static {
		scopeMap = new Builder<String, FormcycleScope>()
				.put(FormcycleScope.FORM_FIELD.getScopeName(), FormcycleScope.FORM_FIELD)
				.build();
	}

	public FormcycleExternalContext() {
		this(SystemWriter.getSystemOutInstance());
	}

	public FormcycleExternalContext(@Nonnull final Writer writer) {
		this(writer, new Formcycle());
	}

	public FormcycleExternalContext(@Nonnull final Writer writer, @Nonnull final Formcycle formcycle) {
		this.writer = writer;
		this.formcycle = formcycle;
	}

	@Override
	protected void output(@Nullable final String html) throws EmbedmentOutputException {
		final Writer writer = this.writer;
		if (writer == null) return;
		try {
			writer.write(html);
			writer.flush();
		}
		catch (final IOException e) {
			throw new EmbedmentOutputException(e, this);
		}
	}

	@Override
	protected void finishOutput() throws EmbedmentOutputException {
		final Writer writer = this.writer;
		if (writer == null) return;
		try {
			writer.flush();
		}
		catch (final IOException e) {
			throw new EmbedmentOutputException(e, this);
		}
		finally {
			this.writer = null;
		}
	}

	@SuppressWarnings({ "null", "unused" })
	@Nonnull
	@Override
	public ALangObject fetchScopedVariable(@Nonnull final String scope, @Nonnull final String name,
			@Nonnull final IEvaluationContext ec) throws EvaluationException {
		@Nullable final FormcycleScope s = scopeMap.get(scope);
		if (s == null) {
			throw new VariableNotDefinedException(scope, name, ec);
		}
		return s.fetch(name, formcycle, ec);
	}

	@Nullable
	public static IScopeInfo getScopeInfo(final String scope) {
		return scopeMap.get(scope);
	}
	
	public static boolean isProvidingScope(final String scope) {
		return scopeMap.containsKey(scope);
	}
	
	// For demonstration purposes only. Replace with real class.
	public static class Formcycle {
		private static final ImmutableMap<String, String> nameMap;
		private static final ImmutableMap<String, String> aliasMap;
		static {
			final Builder<String, String> builderName = new Builder<String, String>();
			final Builder<String, String> builderAlias = new Builder<String, String>();
			// Elements by name
			builderName.put("tf1", "Hello"); //$NON-NLS-1$ //$NON-NLS-2$
			builderName.put("tf2", "World"); //$NON-NLS-1$ //$NON-NLS-2$
			builderName.put("tf3", "igel"); //$NON-NLS-1$ //$NON-NLS-2$
			builderName.put("lang", "tr"); //$NON-NLS-1$ //$NON-NLS-2$
			builderName.put("backslash", "\\"); //$NON-NLS-1$ //$NON-NLS-2$
			builderName.put("tfVorname", "Andre"); //$NON-NLS-1$ //$NON-NLS-2$
			builderName.put("tfNachname","Wachsmuth"); //$NON-NLS-1$ //$NON-NLS-2$
			// Elements by alias
			builderAlias.put("VornameUnicode", "André"); //$NON-NLS-1$ //$NON-NLS-2$
			builderAlias.put("NachnameUnicode","Wachsmuth"); //$NON-NLS-1$ //$NON-NLS-2$
			// Build
			nameMap = builderName.build();
			aliasMap = builderAlias.build();
		}
		@Nullable
		public String getByAlias(final String alias) {
			return aliasMap.get(alias);
		}
		@Nullable
		public String getByName(final String name) {
			return nameMap.get(name);
		}
	}
}
