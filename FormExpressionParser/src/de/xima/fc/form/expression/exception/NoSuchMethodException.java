package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.object.ALangObject;

public class NoSuchMethodException extends NoSuchFunctionException {

	private static final long serialVersionUID = 1L;

	public NoSuchMethodException(final String name, final IEvaluationContext ec) {
		super("method", name, ec);
	}

	public NoSuchMethodException(final String name, final ALangObject thisContext, final IEvaluationContext ec) {
		super("method", name, thisContext, ec);
	}
}
