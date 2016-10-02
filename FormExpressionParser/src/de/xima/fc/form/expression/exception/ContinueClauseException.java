package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.context.IEvaluationContext;

public class ContinueClauseException extends UncatchableEvaluationException {
	private final static String MESSAGE = "Continue used outside of loop or switch, or label does not match any loop or switch.";
	public final String label;
	public ContinueClauseException(final String label, final IEvaluationContext ec) {
		super(ec, MESSAGE);
		this.label = label;
	}
}
