package de.xima.fc.form.expression.impl.variable.provider;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.impl.variable.SimpleVariableType;
import de.xima.fc.form.expression.object.RegexLangObject;

@NonNullByDefault
public abstract class RegexVariableProvider extends AVariableProvider<RegexLangObject> {
	private static final long serialVersionUID = 1L;
	private RegexVariableProvider() {
		super(SimpleVariableType.REGEX);
	}
	@Override
	public abstract RegexLangObject make();
}