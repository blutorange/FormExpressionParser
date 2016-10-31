package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IFunction;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ALangObject.Type;
import de.xima.fc.form.expression.util.CmnCnst;

public class IllegalThisContextException extends CatchableEvaluationException {
	private static final long serialVersionUID = 1L;
	public IllegalThisContextException(final ALangObject thisContext, final Type expectedType,
			final IFunction<ALangObject> function, final IEvaluationContext ec) {
		super(ec,
				String.format(
						CmnCnst.Error.ILLEGAL_THIS_CONTEXT,
						thisContext.toString(), thisContext.getType(), expectedType,
						function.getDeclaredName().length() == 0 ? CmnCnst.TRACER_POSITION_NAME_ANONYMOUS_FUNCTION : function.getDeclaredName()));
		this.thisContext = thisContext;
		this.expectedType = expectedType;
		this.function = function;
	}

	public final ALangObject thisContext;
	public final ALangObject.Type expectedType;
	public final IFunction<ALangObject> function;

}
