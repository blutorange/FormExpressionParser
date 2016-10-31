package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.util.CmnCnst;

public class MathException extends CatchableEvaluationException {
	private static final long serialVersionUID = 1L;

	public MathException(final String details, final IEvaluationContext ec)  {
		super(ec, String.format(CmnCnst.Error.MATH, details));
		this.details = details;
	}

	public final String details;
}
