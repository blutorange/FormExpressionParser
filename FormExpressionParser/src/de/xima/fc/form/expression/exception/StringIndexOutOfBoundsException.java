package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.context.IEvaluationContext;

public class StringIndexOutOfBoundsException extends CatchableEvaluationException {

	@SuppressWarnings("boxing")
	public StringIndexOutOfBoundsException(final String string, final int index, final IEvaluationContext ec) {
		super(ec, String.format("Index %s out of bounds for string <%s>", index, string));
		this.string = string;
		this.index = index;
	}

	public final String string;
	public final int index;
}
