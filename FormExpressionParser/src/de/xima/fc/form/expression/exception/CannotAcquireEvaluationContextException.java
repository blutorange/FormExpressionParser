package de.xima.fc.form.expression.exception;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.util.CmnCnst;

public class CannotAcquireEvaluationContextException extends UncatchableEvaluationException {
	private static final long serialVersionUID = 1L;
	public CannotAcquireEvaluationContextException(@Nonnull final Throwable t) {
		super(CmnCnst.Error.CANNOT_ACQUIRE_EVALUATION_CONTEXT, t);
	}
}
