package de.xima.fc.form.expression.exception.evaluation;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
public class MathDivisionByZeroException extends MathException {
	private static final long serialVersionUID = 1L;
	public MathDivisionByZeroException(final double divisor, final double dividend, final IEvaluationContext ec)  {
		super(NullUtil.messageFormat(CmnCnst.Error.MATH_DIVISION_BY_ZERO, new Double(divisor), new Double(dividend)), ec);
		this.divisor = NumberLangObject.create(divisor);
		this.dividend = NumberLangObject.create(dividend);
	}

	public MathDivisionByZeroException(final ALangObject divisor, final ALangObject dividend, final IEvaluationContext ec)  {
		super(NullUtil.messageFormat(CmnCnst.Error.MATH_DIVISION_BY_ZERO,divisor.inspect(), dividend.inspect()), ec);
		this.divisor = divisor;
		this.dividend = dividend;
	}

	public final ALangObject divisor;
	public final ALangObject dividend;
}