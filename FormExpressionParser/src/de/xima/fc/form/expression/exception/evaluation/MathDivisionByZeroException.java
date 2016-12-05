package de.xima.fc.form.expression.exception.evaluation;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class MathDivisionByZeroException extends MathException {
	private static final long serialVersionUID = 1L;
	public MathDivisionByZeroException(final double divisor, final double dividend, @Nonnull final IEvaluationContext ec)  {
		super(NullUtil.format(CmnCnst.Error.MATH_DIVISION_BY_ZERO, new Double(divisor), new Double(dividend)), ec);
		this.divisor = divisor;
		this.dividend = dividend;
	}

	public MathDivisionByZeroException(final ALangObject divisor, final ALangObject dividend, @Nonnull final IEvaluationContext ec)  {
		super(NullUtil.format(CmnCnst.Error.MATH_DIVISION_BY_ZERO,divisor.inspect(), dividend.inspect()), ec);
		this.divisor = divisor;
		this.dividend = dividend;
	}

	public final Object divisor;
	public final Object dividend;
}
