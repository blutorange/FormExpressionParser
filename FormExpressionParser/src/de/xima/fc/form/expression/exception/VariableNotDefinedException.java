package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.context.IBinding;

public class VariableNotDefinedException extends CatchableEvaluationException {
	public VariableNotDefinedException(final String name, final IBinding binding) {
		super(binding, String.format("Variable <%s> has not been defined at this point.", name));
		this.name = name;
	}
	public final String name;
}
