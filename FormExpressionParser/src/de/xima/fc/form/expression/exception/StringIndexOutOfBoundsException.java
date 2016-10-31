package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.util.CmnCnst;

public class StringIndexOutOfBoundsException extends CatchableEvaluationException {
	private static final long serialVersionUID = 1L;
	public StringIndexOutOfBoundsException(final String string, final int index, final IEvaluationContext ec) {
		super(ec, String.format(CmnCnst.Error.STRING_INDEX_OUT_OF_BOUNDS, new Integer(index), string));
		this.string = string;
		this.index = index;
	}

	public final String string;
	public final int index;
}
