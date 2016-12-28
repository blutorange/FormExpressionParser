package de.xima.fc.form.expression.impl.variable.provider;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.impl.variable.SimpleVariableType;
import de.xima.fc.form.expression.object.ExceptionLangObject;

@NonNullByDefault
public abstract class ExceptionVariableProvider extends AVariableProvider<ExceptionLangObject> {
	private static final long serialVersionUID = 1L;
	private ExceptionVariableProvider() {
		super(SimpleVariableType.EXCEPTION);
	}
	@Override
	public abstract ExceptionLangObject make();
}