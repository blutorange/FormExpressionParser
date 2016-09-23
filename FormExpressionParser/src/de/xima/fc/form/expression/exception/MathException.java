package de.xima.fc.form.expression.exception;

public class MathException extends CatchableEvaluationException {
	public MathException(final String details)  {
		super("Error during math operation: " + details);
		this.details = details;
	}

	public final String details;
}
