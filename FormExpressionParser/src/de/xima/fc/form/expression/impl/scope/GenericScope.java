package de.xima.fc.form.expression.impl.scope;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.google.common.collect.ImmutableMap;

import de.xima.fc.form.expression.exception.evaluation.VariableNotDefinedException;
import de.xima.fc.form.expression.iface.evaluate.ICustomScope;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.object.ALangObject;

@Immutable
public class GenericScope implements ICustomScope {
	@Nonnull
	private final String name;
	@Nonnull
	private final ImmutableMap<String, ALangObject> map;
	
	public GenericScope(@Nonnull final String name, @Nonnull final Map<String,ALangObject> map) {
		this.name = name;
		this.map = new ImmutableMap.Builder<String,ALangObject>().putAll(map).build();
	}
	
	@SuppressWarnings({ "null", "unused" })
	@Override
	public ALangObject fetch(@Nonnull final String variableName, @Nonnull final IEvaluationContext ec) throws VariableNotDefinedException {
		final ALangObject res = map.get(variableName);
		if (res == null)
			throw new VariableNotDefinedException(variableName, ec);
		return res;
	}

	@Override
	public String getScopeName() {
		return name;
	}
}
