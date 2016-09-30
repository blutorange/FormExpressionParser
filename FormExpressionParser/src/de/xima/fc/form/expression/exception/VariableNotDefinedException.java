package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.context.IEvaluationContext;

public class VariableNotDefinedException extends CatchableEvaluationException {
	public VariableNotDefinedException(final String name, final IEvaluationContext ec) {
		super(ec, String.format("Variable <%s> not defined at this point.", name));
		this.name = name;
	}
	public final String name;
}
