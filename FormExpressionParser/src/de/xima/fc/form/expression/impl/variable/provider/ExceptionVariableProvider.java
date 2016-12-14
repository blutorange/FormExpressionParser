package de.xima.fc.form.expression.impl.variable.provider;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.impl.variable.SimpleVariableType;
import de.xima.fc.form.expression.object.ExceptionLangObject;

@ParametersAreNonnullByDefault
public abstract class ExceptionVariableProvider extends AVariableProvider<ExceptionLangObject> {
	private static final long serialVersionUID = 1L;
	private ExceptionVariableProvider() {
		super(SimpleVariableType.EXCEPTION);
	}
	@Override
	public abstract ExceptionLangObject make();
}