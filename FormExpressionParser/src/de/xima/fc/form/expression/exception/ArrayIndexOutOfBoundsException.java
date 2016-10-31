package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ArrayLangObject;
import de.xima.fc.form.expression.util.CmnCnst;

public class ArrayIndexOutOfBoundsException extends CatchableEvaluationException {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("boxing")
	public ArrayIndexOutOfBoundsException(final ArrayLangObject array, final int index, final IEvaluationContext ec) {
		super(ec, String.format(CmnCnst.Error.ARRAY_INDEX_OUT_OF_BOUNDS, index, array));
		this.array = array.toArray();
		this.index = index;
	}

	public final ALangObject[] array;
	public final int index;
}
