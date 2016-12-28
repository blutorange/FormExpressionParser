package de.xima.fc.form.expression.impl.externalcontext;

import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import de.xima.fc.form.expression.impl.writer.SystemWriter;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ArrayLangObject;
import de.xima.fc.form.expression.object.HashLangObject;
import de.xima.fc.form.expression.object.StringLangObject;

public class Formcycle {
	@Nonnull
	private final Writer writer;
	private ALangObject actionResult;
	@Nonnull
	private static final ImmutableMap<String, String> nameMap;
	@Nonnull
	private static final ImmutableMap<String, String> aliasMap;
	@Nonnull
	private static final List<Map<String,String>> actionResultMap;
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
	static {
		final ImmutableList.Builder<Map<String,String>> builderList = new ImmutableList.Builder<>();

		final ImmutableMap.Builder<String, String> builderMap1 = new ImmutableMap.Builder<>();
		builderMap1.put("foo", "bar"); //$NON-NLS-1$ //$NON-NLS-2$
		builderMap1.put("hello", "world"); //$NON-NLS-1$ //$NON-NLS-2$
		builderList.add(builderMap1.build());

		final ImmutableMap.Builder<String, String> builderMap2 = new ImmutableMap.Builder<>();
		builderMap2.put("error", "true"); //$NON-NLS-1$ //$NON-NLS-2$
		builderMap2.put("message", "This is an error"); //$NON-NLS-1$ //$NON-NLS-2$
		builderList.add(builderMap2.build());

		actionResultMap = builderList.build();
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
	@Nonnull
	private List<Map<String,String>> getActionResult() {
		return actionResultMap;
	}
	@Nonnull
	public ALangObject getActionResultAsLangObject() {
		if (actionResult != null)
			return actionResult;
		final ArrayLangObject array = ArrayLangObject.create();
		for (final Map<String,String> map : getActionResult()) {
			final HashLangObject hash = HashLangObject.create();
			for (final Entry<String,String> entry : map.entrySet())
				hash.put(StringLangObject.create(entry.getKey()), StringLangObject.create(entry.getValue()));
			array.add(hash);
		}
		return actionResult = array;
	}
}