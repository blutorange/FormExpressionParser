package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.object.ALangObject;

public class IllegalArgumentTypeException extends CatchableEvaluationException {
	public final Class<? extends ALangObject> isClass, expectedClass;
	public final String functionName;
	public int argumentIndex;

	public IllegalArgumentTypeException(final String functionName, final int argumentIndex, final Class<? extends ALangObject> isClass,final Class<? extends ALangObject> expectedClass, final IEvaluationContext ec) {
		super(ec, "Encountered function " + functionName + "with argument of type " + isClass + " for argument " + argumentIndex + System.lineSeparator()
		+ "Expected " + expectedClass);
		this.functionName = functionName;
		this.argumentIndex = argumentIndex;
		this.isClass = isClass;
		this.expectedClass = expectedClass;
	}

}
