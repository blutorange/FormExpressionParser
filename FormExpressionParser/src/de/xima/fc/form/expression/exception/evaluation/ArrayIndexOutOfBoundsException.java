package de.xima.fc.form.expression.exception.evaluation;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ArrayLangObject;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public class ArrayIndexOutOfBoundsException extends CatchableEvaluationException {
	private static final long serialVersionUID = 1L;

	public ArrayIndexOutOfBoundsException(final ArrayLangObject array, final int index, final IEvaluationContext ec) {
		super(ec, NullUtil.messageFormat(CmnCnst.Error.ARRAY_INDEX_OUT_OF_BOUNDS, Integer.valueOf(index), array));
		this.array = array.toArray();
		this.index = index;
	}

	public final ALangObject[] array;
	public final int index;
}