package de.xima.fc.form.expression.impl.variable.provider;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.impl.variable.SimpleVariableType;
import de.xima.fc.form.expression.object.NumberLangObject;

@ParametersAreNonnullByDefault
public abstract class NumberVariableProvider extends AVariableProvider<NumberLangObject> {
	private static final long serialVersionUID = 1L;
	private NumberVariableProvider() {
		super(SimpleVariableType.NUMBER);
	}
	@Override
	public abstract NumberLangObject make();
}