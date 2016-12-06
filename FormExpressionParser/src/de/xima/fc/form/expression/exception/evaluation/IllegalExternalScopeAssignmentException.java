package de.xima.fc.form.expression.exception.evaluation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class IllegalExternalScopeAssignmentException extends UncatchableEvaluationException {
	private static final long serialVersionUID = 1L;
	public IllegalExternalScopeAssignmentException(@Nullable final String scope, @Nonnull final String name, @Nonnull final IEvaluationContext ec) {
		super(ec, NullUtil.stringFormat(CmnCnst.Error.ASSIGNMENT_OF_EXTERNALLY_SCOPED_VARIABLE, scope, name));
		this.scope = scope;
		this.name = name;
	}

	@Nullable
	public final String scope;
	@Nonnull
	public final String name;
}
