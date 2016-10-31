package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.util.CmnCnst;

public class CannotAcquireEvaluationContextException extends UncatchableEvaluationException {
	private static final long serialVersionUID = 1L;
	public CannotAcquireEvaluationContextException(final Throwable t) {
		super(CmnCnst.Error.CANNOT_ACQUIRE_EVALUATION_CONTEXT, t);
	}
}
