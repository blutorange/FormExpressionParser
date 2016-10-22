package de.xima.fc.form.expression.impl.externalcontext;

import java.io.IOException;
import java.io.Writer;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.exception.EmbedmentOutputException;
import de.xima.fc.form.expression.impl.writer.SystemOutWriter;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.StringLangObject;
import de.xima.fc.form.expression.util.CmnCnst;

// Dummy for illustration, remove this and use the real FORMCYCLE class FormVersion.
public class FormcycleExternalContext extends AHtmlExternalContext {
	private Writer writer;

	// For demonstration purposes only. Replace with access to actual form elements.
	private static final ImmutableMap<String, String> nameMap;
	private static final ImmutableMap<String, String> aliasMap;
	static {
		final Builder<String, String> builderName = new Builder<String, String>();
		final Builder<String, String> builderAlias = new Builder<String, String>();
		// Elements by name
		builderName.put("tf1", "Hello");
		builderName.put("tf2", "World");
		builderName.put("tf3", "igel");
		builderName.put("lang", "tr");
		builderName.put("backslash", "\\");
		builderName.put("tfVorname", "Andre");
		builderName.put("tfNachname","Wachsmuth");
		// Elements by alias
		builderAlias.put("VornameUnicode", "André");
		builderAlias.put("NachnameUnicode","Wachsmuth");
		// Build
		nameMap = builderName.build();
		aliasMap = builderAlias.build();
	}

	public FormcycleExternalContext() {
		// Write to stdout for debugging.
		this.writer = SystemOutWriter.getInstance();
	}

	public FormcycleExternalContext(final Writer writer) {
		this.writer = writer;
	}


	/*
	public FormcycleExternalContext(Object customObject) {
		// this.customObject = customObject;
	}
	 */

	public String getFieldValueByName(final String name) {
		return nameMap.get(name);
	}
	public String getFieldValueByAlias(final String name) {
		return aliasMap.get(name);
	}

	@Override
	protected void output(String html) throws EmbedmentOutputException {
		if (writer == null) return;
		try {
			writer.write(html);
			writer.flush();
		}
		catch (IOException e) {
			throw new EmbedmentOutputException(e, this);
		}
	}

	@Override
	protected void finishOutput() throws EmbedmentOutputException {
		if (writer == null) return;
		try {
			writer.flush();
		}
		catch (IOException e) {
			throw new EmbedmentOutputException(e, this);
		}
		finally {
			writer = null;
		}
	}

	
	private final static ImmutableMap<String, ScopeImpl> scopeMap;
	private static enum ScopeImpl {
		FORM_FIELD {
			@Override
			public ALangObject fetch(final String name, final Object myObject) {
				final String value = aliasMap.get(name);
				if (value != null) return StringLangObject.create(value);
				return StringLangObject.create(nameMap.get(name));
			}
		};
		public abstract ALangObject fetch(String name, Object myObject);
	}
	static {
		scopeMap = new Builder<String, ScopeImpl>()
				.put(CmnCnst.CUSTOM_SCOPE_FORM_FIELD, ScopeImpl.FORM_FIELD)
				.build();
	}

	@Override
	public ALangObject fetchScopedVariable(final String scope, final String name, final IEvaluationContext ec) {
		final ScopeImpl s = scopeMap.get(scope);
		return s != null ? s.fetch(name, null) : null;
	}
}
