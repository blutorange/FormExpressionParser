package de.xima.fc.form.expression.exception.evaluation;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class NumberTooLongForLongException extends MathException {
	private static final long serialVersionUID = 1L;

	public NumberTooLongForLongException(final double number, @Nonnull final IEvaluationContext ec)  {
		super(NullUtil.stringFormat(CmnCnst.Error.NUMBER_TOO_LONG_FOR_LONG, number), ec);
		this.number = number;
	}

	public final double number;
}
