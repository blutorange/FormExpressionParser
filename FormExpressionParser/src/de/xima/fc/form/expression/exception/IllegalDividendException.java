/* Generated By:JavaCC: Do not edit this line. ParseException.java Version 5.0 */
/* JavaCCOptions:KEEP_LINE_COL=null */
package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.object.ALangObject;

/**
 * This exception is thrown when an object support the division
 * operator, but not with a certain operand.
 * <br><br>
 * This exception should not be used for any other method other than {@link EMethod#SLASH}.
 */
public class IllegalDividendException extends IllegalArgumentValueException {

	private static final long serialVersionUID = 1L;

	public IllegalDividendException(final ALangObject divisor, final ALangObject dividend, final int index, final IEvaluationContext ec) {
		super(EMethod.SLASH.methodName, divisor, dividend, index, ec);
		this.dividend = dividend;
		this.divisor = divisor;
	}

	public final ALangObject divisor;
	public final ALangObject dividend;
}
