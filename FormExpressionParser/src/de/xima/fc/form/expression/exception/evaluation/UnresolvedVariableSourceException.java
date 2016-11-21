package de.xima.fc.form.expression.exception.evaluation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class UnresolvedVariableSourceException extends UncatchableEvaluationException {
	private static final long serialVersionUID = 1L;

	public UnresolvedVariableSourceException(@Nullable final String scope, @Nonnull final String name, @Nonnull final IEvaluationContext ec) {
		super(ec, format(scope,name));
		this.scope = scope;
		this.name = name;
	}

	@Nonnull
	private static String format(@Nullable final String scope, @Nonnull final String name) {
		if (scope == null)
			return NullUtil.format(CmnCnst.Error.UNSCOPED_VARIABLE_NOT_RESOLVED, name);
		return NullUtil.format(CmnCnst.Error.SCOPED_VARIABLE_NOT_RESOLVED, scope, name);
	}
	
	@Nullable
	public final String scope;
	@Nonnull
	public final String name;
}
