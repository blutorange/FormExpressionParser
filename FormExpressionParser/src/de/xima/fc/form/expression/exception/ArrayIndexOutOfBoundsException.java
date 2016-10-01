package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ArrayLangObject;

public class ArrayIndexOutOfBoundsException extends CatchableEvaluationException {

	@SuppressWarnings("boxing")
	public ArrayIndexOutOfBoundsException(final ArrayLangObject array, final int index, final IEvaluationContext ec) {
		super(ec, String.format("Index %s out of bounds for array %s.", index, array));
		this.array = array.toArray();
		this.index = index;
	}

	public final ALangObject[] array;
	public final int index;
}
