package de.xima.fc.form.expression.impl.scope;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import de.xima.fc.form.expression.context.ICustomScope;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.StringLangObject;
import de.xima.fc.form.expression.util.CmnCnst;

public final class FormFieldScope implements ICustomScope {
	private final FormVersion formVersion;
	
	// Dummy for illustration, remove this and use the real FORMCYCLE class FormVersion.
	public final static class FormVersion {
		private static final ImmutableMap<String, String> nameMap;
		private static final ImmutableMap<String, String> aliasMap;
		static {
			Builder<String, String> builderName = new Builder<String, String>();
			Builder<String, String> builderAlias = new Builder<String, String>();			
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
		public String getFieldValueByName(String name) {
			return nameMap.get(name);
		}
		public String getFieldValueByAlias(String name) {
			return aliasMap.get(name);
		}
	}
	
	public FormFieldScope(final FormVersion formVersion) {
		this.formVersion = formVersion;
	}

	@Override
	public ALangObject fetch(String variableName) {
		final String value = formVersion.getFieldValueByAlias(variableName);
		if (value != null) return StringLangObject.create(value);
		return StringLangObject.create(formVersion.getFieldValueByName(variableName));
	}

	@Override
	public String getScopeName() {
		return CmnCnst.CUSTOM_SCOPE_FORM_FIELD;
	}
}
