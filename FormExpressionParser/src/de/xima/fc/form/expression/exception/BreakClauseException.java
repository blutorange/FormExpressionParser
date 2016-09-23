package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.context.IEvaluationContext;

public class BreakClauseException extends UncatchableEvaluationException {
	private final static String MESSAGE = "Break used outside of loop or switch.";
	public final String label; 
	public BreakClauseException(String label, IEvaluationContext ec) {
		super(ec, MESSAGE);
		this.label = label;
	}
	
	@Override
	public synchronized Throwable fillInStackTrace() {
		return this;
	}

}
