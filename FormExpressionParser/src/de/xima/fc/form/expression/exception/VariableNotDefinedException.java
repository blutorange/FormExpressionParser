package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.util.CmnCnst;

public class VariableNotDefinedException extends CatchableEvaluationException {
	private static final long serialVersionUID = 1L;
	public VariableNotDefinedException(final String name, final IEvaluationContext ec) {
		super(ec, String.format(CmnCnst.Error.VARIABLE_NOT_DEFINED_LOCAL, name));
		this.name = name;
		this.scope = null;
	}

	public VariableNotDefinedException(final String scope, final String name, final IEvaluationContext ec) {
		super(ec, String.format(CmnCnst.Error.VARIABLE_NOT_DEFINED_SCOPED, scope, name));
		this.name = name;
		this.scope = scope;
	}

	public final String scope;
	public final String name;
}
