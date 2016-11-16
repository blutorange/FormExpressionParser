package de.xima.fc.form.expression.impl.scope;

import com.google.common.collect.ImmutableMap;

import de.xima.fc.form.expression.iface.context.ICustomScope;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.util.CmnCnst.CustomScope;

public enum MathScope implements ICustomScope {
	INSTANCE
	;
	private final ImmutableMap<String, ALangObject> map = new ImmutableMap.Builder<String, ALangObject>()
			.put("e", NumberLangObject.getEInstance())
			.put("pi", NumberLangObject.getPiInstance())
			.build();

	@Override
	public ALangObject fetch(final String variableName) {
		return map.get(variableName);
	}

	@Override
	public String getScopeName() {
		return CustomScope.MATH;
	}
}
