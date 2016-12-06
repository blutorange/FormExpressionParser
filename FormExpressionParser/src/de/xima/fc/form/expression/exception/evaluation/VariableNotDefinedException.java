package de.xima.fc.form.expression.exception.evaluation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class VariableNotDefinedException extends CatchableEvaluationException {
	private static final long serialVersionUID = 1L;
	public VariableNotDefinedException(@Nonnull final String name, @Nonnull final IEvaluationContext ec) {
		super(ec, NullUtil.stringFormat(CmnCnst.Error.VARIABLE_NOT_DEFINED_LOCAL, name));
		this.name = name;
		this.scope = null;
	}

	public VariableNotDefinedException(@Nonnull final String scope, @Nonnull final String name, @Nonnull final IEvaluationContext ec) {
		super(ec, NullUtil.stringFormat(CmnCnst.Error.VARIABLE_NOT_DEFINED_SCOPED, scope, name));
		this.name = name;
		this.scope = scope;
	}

	public final @Nullable String scope;
	public final @Nonnull String name;
}
