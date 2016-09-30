package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.context.IEvaluationContext;

public class VariableNotDefinedException extends CatchableEvaluationException {
	public VariableNotDefinedException(final String name, final IEvaluationContext ec) {
		super(ec, String.format("Variable %s not resolvable to a defined variable.", name));
		this.name = name;
		this.scope = null;
	}
	
	public VariableNotDefinedException(String scope, String name, IEvaluationContext ec) {
		super(ec, String.format("Variable %s::%s not defined at this point.", scope, name));
		this.name = name;
		this.scope = scope;
	}

	public final String scope;
	public final String name;
}
