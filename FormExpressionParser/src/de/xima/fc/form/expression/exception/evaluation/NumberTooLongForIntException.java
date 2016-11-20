package de.xima.fc.form.expression.exception.evaluation;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class NumberTooLongForIntException extends MathException {
	private static final long serialVersionUID = 1L;

	public NumberTooLongForIntException(final double number, @Nonnull final IEvaluationContext ec)  {
		super(NullUtil.format(CmnCnst.Error.NUMBER_TOO_LONG_FOR_INT, number), ec);
		this.number = number;
	}

	public final double number;
}
