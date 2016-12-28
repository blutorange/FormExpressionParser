package de.xima.fc.form.expression.exception.evaluation;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public class NumberTooLongForIntException extends MathException {
	private static final long serialVersionUID = 1L;

	public NumberTooLongForIntException(final double number, final IEvaluationContext ec)  {
		super(NullUtil.messageFormat(CmnCnst.Error.NUMBER_TOO_LONG_FOR_INT, Double.valueOf(number)), ec);
		this.number = number;
	}

	public final double number;
}