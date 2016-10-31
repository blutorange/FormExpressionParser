package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.util.CmnCnst;

public class MathDivisionByZeroException extends MathException {
	private static final long serialVersionUID = 1L;
	public MathDivisionByZeroException(final double divisor, final double dividend, final IEvaluationContext ec)  {
		super(String.format(CmnCnst.Error.MATH_DIVISION_BY_ZERO, new Double(divisor), new Double(dividend)), ec);
		this.divisor = divisor;
		this.dividend = dividend;
	}

	public MathDivisionByZeroException(final ALangObject divisor, final ALangObject dividend, final IEvaluationContext ec)  {
		super(String.format(CmnCnst.Error.MATH_DIVISION_BY_ZERO,divisor.inspect(), dividend.inspect()), ec);
		this.divisor = divisor;
		this.dividend = dividend;
	}

	public final Object divisor;
	public final Object dividend;
}
