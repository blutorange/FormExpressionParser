package de.xima.fc.form.expression.exception;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ArrayLangObject;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class ArrayIndexOutOfBoundsException extends CatchableEvaluationException {
	private static final long serialVersionUID = 1L;

	public ArrayIndexOutOfBoundsException(@Nonnull final ArrayLangObject array, final int index,
			@Nonnull final IEvaluationContext ec) {
		super(ec, NullUtil.format(CmnCnst.Error.ARRAY_INDEX_OUT_OF_BOUNDS, new Integer(index), array));
		this.array = array.toArray();
		this.index = index;
	}

	public final @Nonnull ALangObject[] array;
	public final int index;
}
