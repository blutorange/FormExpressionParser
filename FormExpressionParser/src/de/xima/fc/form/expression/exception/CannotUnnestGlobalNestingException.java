package de.xima.fc.form.expression.exception;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.util.CmnCnst;

public class CannotUnnestGlobalNestingException extends NestingLevelException {
	private static final long serialVersionUID = 1L;

	public CannotUnnestGlobalNestingException(final @Nonnull IEvaluationContext ec) {
		super(CmnCnst.Error.CANNOT_UNNEST_GLOBAL_BINDING, ec);
	}

}
