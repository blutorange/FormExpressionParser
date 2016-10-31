package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.util.CmnCnst;

public class NumberTooLongForIntException extends MathException {
	private static final long serialVersionUID = 1L;

	public NumberTooLongForIntException(final double number, final IEvaluationContext ec)  {
		super(String.format(CmnCnst.Error.NUMBER_TOO_LONG_FOR_INT, number), ec);
		this.number = number;
	}

	public final double number;
}
