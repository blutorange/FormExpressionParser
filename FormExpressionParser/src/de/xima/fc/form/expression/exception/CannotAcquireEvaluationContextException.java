package de.xima.fc.form.expression.exception;

public class CannotAcquireEvaluationContextException extends UncatchableEvaluationException {
	public CannotAcquireEvaluationContextException(Throwable t) {
		super("Failed to acquire evaluation context.", t);
	}
}
