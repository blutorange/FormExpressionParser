package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.object.ALangObject;

public class NoSuchMethodException extends NoSuchFunctionException {

	private static final long serialVersionUID = 1L;

	public NoSuchMethodException(final EMethod method, final ALangObject thisContext, final IEvaluationContext ec) {
		super("method", String.format("%s(%s)", method.name(), method.methodName), thisContext, ec);
		this.method = method;
	}
	public final EMethod method;
}
