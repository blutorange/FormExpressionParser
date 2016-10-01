package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.context.IEvaluationContext;

public class MathException extends CatchableEvaluationException {
	public MathException(final String details, final IEvaluationContext ec)  {
		super(ec, String.format("Error during math operation: %s", details));
		this.details = details;
	}

	public final String details;
}
