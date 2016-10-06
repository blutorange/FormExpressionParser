package de.xima.fc.form.expression.impl.scope;

import de.xima.fc.form.expression.context.ICustomScope;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.util.CmnCnst;

public enum MathScope implements ICustomScope {
	INSTANCE
	;
	@Override
	public ALangObject fetch(String variableName) {
		//TODO
		return null;
	}

	@Override
	public String getScopeName() {
		return CmnCnst.CUSTOM_SCOPE_MATH;
	}
}
