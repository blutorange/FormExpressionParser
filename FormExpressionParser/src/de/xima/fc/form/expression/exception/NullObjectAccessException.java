package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.object.ALangObject;

public class NullObjectAccessException extends CatchableEvaluationException {

	public NullObjectAccessException(final ALangObject object, final IEvaluationContext ec) {
		super(ec, "Cannot call method on null object: " + object.toString());
		this.object = object;
	}

	public final ALangObject object;
}
