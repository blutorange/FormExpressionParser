package de.xima.fc.form.expression.impl.scope;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableMap;

import de.xima.fc.form.expression.enums.EVariableSource;
import de.xima.fc.form.expression.exception.evaluation.VariableNotDefinedException;
import de.xima.fc.form.expression.iface.context.ICustomScope;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.iface.parse.IScopeInfo;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.util.CmnCnst.CustomScope;

public enum MathScope implements ICustomScope, IScopeInfo {
	INSTANCE
	;
	private final ImmutableMap<String, ALangObject> map = new ImmutableMap.Builder<String, ALangObject>()
			.put("e", NumberLangObject.getEInstance())
			.put("pi", NumberLangObject.getPiInstance())
			.build();

	@SuppressWarnings({ "unused", "null" })
	@Override
	public ALangObject fetch(@Nonnull final String variableName, @Nonnull final IEvaluationContext ec) throws VariableNotDefinedException {
		final ALangObject res = map.get(variableName);
		if (res == null)
			throw new VariableNotDefinedException(variableName, ec);
		return res;
	}
	@Override
	public String getScopeName() {
		return CustomScope.MATH;
	}

	@Override
	public boolean isProviding(final String variableName) {
		return map.containsKey(variableName);
	}

	@Override
	public EVariableSource getSource() {
		return EVariableSource.LIBRARY;
	}
}