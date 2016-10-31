package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.util.CmnCnst;

public class NumberTooLongForLongException extends MathException {
	private static final long serialVersionUID = 1L;

	public NumberTooLongForLongException(final double number, final IEvaluationContext ec)  {
		super(String.format(CmnCnst.Error.NUMBER_TOO_LONG_FOR_LONG, number), ec);
		this.number = number;
	}

	public final double number;
}
