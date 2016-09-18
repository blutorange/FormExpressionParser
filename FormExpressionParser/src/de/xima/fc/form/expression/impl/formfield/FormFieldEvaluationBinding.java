package de.xima.fc.form.expression.impl.formfield;

import java.util.HashMap;
import java.util.Map;

import de.xima.fc.form.expression.exception.VariableNotDefinedException;
import de.xima.fc.form.expression.impl.CloneBinding;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.StringLangObject;

public final class FormFieldEvaluationBinding extends CloneBinding {
	private static final Map<String, String> nameMap = new HashMap<>();
	private static final Map<String, String> aliasMap = new HashMap<>();

	public FormFieldEvaluationBinding(/* FormVersion formVersion*/) {
		// this.formVersion = formVersion;
	}

	static {
		// Elements by name
		nameMap.put("tf1", "Hello");
		nameMap.put("tf2", "World");
		nameMap.put("tf3", "igel");
		nameMap.put("lang", "tr");
		nameMap.put("backslash", "\\");
		nameMap.put("tfVorname", "Andre");
		nameMap.put("tfNachname","Wachsmuth");

		// Elements by alias
		aliasMap.put("VornameUnicode", "André");
		aliasMap.put("NachnameUnicode","Wachsmuth");
	}

	@Override
	protected ALangObject getDefaultValue(final String name) throws VariableNotDefinedException {
		String value = nameMap.get(name);
		if (value == null) value = aliasMap.get(name);
		if (value == null) throw new VariableNotDefinedException(name, this);
		return StringLangObject.create(value);
	}


}
