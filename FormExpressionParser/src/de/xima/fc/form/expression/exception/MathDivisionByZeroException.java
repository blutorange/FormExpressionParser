package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.object.ALangObject;

public class MathDivisionByZeroException extends MathException {

	@SuppressWarnings("boxing")
	public MathDivisionByZeroException(final double divisor, final double dividend)  {
		super("Division by zero: " + divisor + dividend);
		this.divisor = divisor;
		this.dividend = dividend;
	}

	public MathDivisionByZeroException(final ALangObject divisor, final ALangObject dividend)  {
		super("Division by zero: " + divisor.inspect() + dividend.inspect());
		this.divisor = divisor;
		this.dividend = dividend;
	}

	public final Object divisor;
	public final Object dividend;
}
