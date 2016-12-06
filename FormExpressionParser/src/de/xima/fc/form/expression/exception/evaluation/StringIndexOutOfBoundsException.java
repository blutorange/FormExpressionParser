package de.xima.fc.form.expression.exception.evaluation;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class StringIndexOutOfBoundsException extends CatchableEvaluationException {
	private static final long serialVersionUID = 1L;

	public StringIndexOutOfBoundsException(@Nonnull final String string, final int index,
			@Nonnull final IEvaluationContext ec) {
		super(ec, NullUtil.stringFormat(CmnCnst.Error.STRING_INDEX_OUT_OF_BOUNDS, new Integer(index), string));
		this.string = string;
		this.index = index;
	}

	public final String string;
	public final int index;
}
