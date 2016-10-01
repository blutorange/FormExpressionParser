package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.object.ALangObject;

public class NoSuchFunctionException extends CatchableEvaluationException {

	private static final long serialVersionUID = 1L;

	public NoSuchFunctionException(final String type, final String name, final IEvaluationContext ec) {
		super(ec, String.format("No such %s named %s.", type, name));
		this.name = name;
		thisContext = null;
	}

	public NoSuchFunctionException(final String type, final String name, final ALangObject thisContext, final IEvaluationContext ec) {
		super(ec, String.format("No such %s named <%s> for object <%s> of type %s.",
				type, name, thisContext.inspect(), thisContext.getType()));
		this.name = name;
		this.thisContext = thisContext;
	}

	public final String name;
	public final ALangObject thisContext;
}
