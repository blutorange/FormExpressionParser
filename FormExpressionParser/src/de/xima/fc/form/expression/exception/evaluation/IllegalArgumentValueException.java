/* Generated By:JavaCC: Do not edit this line. ParseException.java Version 5.0 */
/* JavaCCOptions:KEEP_LINE_COL=null */
package de.xima.fc.form.expression.exception.evaluation;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IFunction;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

/**
 * This exception is thrown when an object support the division operator, but
 * not with a certain operand.
 *
 */
@ParametersAreNonnullByDefault
public class IllegalArgumentValueException extends CatchableEvaluationException {

	private static final long serialVersionUID = 1L;

	public IllegalArgumentValueException(@Nullable final IFunction<?> function, final String functionName,
			final ALangObject thisContext, final ALangObject argument, final int index, final IEvaluationContext ec) {
		super(ec, NullUtil.messageFormat(CmnCnst.Error.ILLEGAL_ARGUMENT_VALUE, thisContext.inspect(),
				Integer.valueOf(index), functionName, argument.inspect()));
		this.functionName = functionName;
		this.thisContext = thisContext;
		this.index = index;
		this.argument = argument;
		this.function = function;
	}

	public IllegalArgumentValueException(final IFunction<?> function, final ALangObject thisContext,
			final ALangObject argument, final int index, final IEvaluationContext ec) {
		this(function, function.getDeclaredName(), thisContext, argument, index, ec);
	}

	public final ALangObject thisContext;
	public final ALangObject argument;
	public final int index;
	public final String functionName;
	public final @Nullable IFunction<?> function;
}