package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.context.IEvaluationContext;

public class BreakClauseException extends UncatchableEvaluationException {
	private final static String MESSAGE = "Break used outside of loop or switch, or label does not match any loop or switch.";
	public final String label;
	public BreakClauseException(final String label, final IEvaluationContext ec) {
		super(ec, MESSAGE);
		this.label = label;
	}
}
