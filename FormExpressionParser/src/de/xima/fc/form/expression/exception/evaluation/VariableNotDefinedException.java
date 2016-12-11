package de.xima.fc.form.expression.exception.evaluation;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public class VariableNotDefinedException extends CatchableEvaluationException {
	private static final long serialVersionUID = 1L;
	public VariableNotDefinedException(final String name, final IEvaluationContext ec) {
		super(ec, NullUtil.messageFormat(CmnCnst.Error.VARIABLE_NOT_DEFINED_LOCAL, name));
		this.name = name;
		this.scope = null;
	}

	public VariableNotDefinedException(final String scope, final String name, final IEvaluationContext ec) {
		super(ec, NullUtil.messageFormat(CmnCnst.Error.VARIABLE_NOT_DEFINED_SCOPED, scope, name));
		this.name = name;
		this.scope = scope;
	}

	public final @Nullable String scope;
	public final String name;
}