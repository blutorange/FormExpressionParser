package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.context.IEvaluationContext;

public class ReturnClauseException extends UncatchableEvaluationException {
	private final static String MESSAGE = "Return clause used outside a function.";
	public ReturnClauseException(final IEvaluationContext ec) {
		super(ec, MESSAGE);
	}
}
