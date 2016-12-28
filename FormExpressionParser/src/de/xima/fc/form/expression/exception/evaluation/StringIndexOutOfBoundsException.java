package de.xima.fc.form.expression.exception.evaluation;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
public class StringIndexOutOfBoundsException extends CatchableEvaluationException {
	private static final long serialVersionUID = 1L;

	public StringIndexOutOfBoundsException(final String string, final int index, final IEvaluationContext ec) {
		super(ec, NullUtil.messageFormat(CmnCnst.Error.STRING_INDEX_OUT_OF_BOUNDS, Integer.valueOf(index), string));
		this.string = string;
		this.index = index;
	}

	public final String string;
	public final int index;
}