package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IFunction;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ALangObject.Type;

public class IllegalThisContextException extends CatchableEvaluationException {

	public IllegalThisContextException(final ALangObject thisContext, final Type expectedType,
			final IFunction<ALangObject> function, final IEvaluationContext ec) {
		super(ec,
				String.format(
						"Provided this context <%s> of type %s does not match the expected type %s for function <%s>.",
						thisContext.toString(), thisContext.getType(), expectedType,
						function.getDeclaredName().length() == 0 ? "(anonymous)" : function.getDeclaredName()));
		this.thisContext = thisContext;
		this.expectedType = expectedType;
		this.function = function;
	}

	public final ALangObject thisContext;
	public final ALangObject.Type expectedType;
	public final IFunction<ALangObject> function;

}
