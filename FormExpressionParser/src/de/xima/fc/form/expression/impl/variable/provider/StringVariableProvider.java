package de.xima.fc.form.expression.impl.variable.provider;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.impl.variable.SimpleVariableType;
import de.xima.fc.form.expression.object.StringLangObject;

@ParametersAreNonnullByDefault
public abstract class StringVariableProvider extends AVariableProvider<StringLangObject> {
	private static final long serialVersionUID = 1L;
	private StringVariableProvider() {
		super(SimpleVariableType.STRING);
	}
	@Override
	public abstract StringLangObject make();
}