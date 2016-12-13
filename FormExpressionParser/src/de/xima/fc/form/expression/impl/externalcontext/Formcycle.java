package de.xima.fc.form.expression.impl.externalcontext;

import java.io.Writer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;

import de.xima.fc.form.expression.impl.writer.SystemWriter;

public class Formcycle {
	@Nonnull
	private final Writer writer;
	private static final ImmutableMap<String, String> nameMap;
	private static final ImmutableMap<String, String> aliasMap;
	public Formcycle() {
		this(SystemWriter.getSystemOutInstance());
	}
	public Formcycle(@Nonnull final Writer writer) {
		this.writer = writer;
	}
	static {
		final ImmutableMap.Builder<String, String> builderName = new ImmutableMap.Builder<>();
		final ImmutableMap.Builder<String, String> builderAlias = new ImmutableMap.Builder<>();
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
	@Nonnull
	public Writer getWriter() {
		return writer;
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