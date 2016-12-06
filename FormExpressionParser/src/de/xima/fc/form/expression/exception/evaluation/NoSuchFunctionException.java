package de.xima.fc.form.expression.exception.evaluation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class NoSuchFunctionException extends CatchableEvaluationException {

	private static final long serialVersionUID = 1L;

	public NoSuchFunctionException(@Nonnull final String type, @Nonnull final String name, @Nonnull final IEvaluationContext ec) {
		super(ec, NullUtil.stringFormat(CmnCnst.Error.NO_SUCH_FUNCTION_1, type, name));
		this.name = name;
		thisContext = null;
	}

	public NoSuchFunctionException(@Nonnull final String type, @Nonnull final String name, @Nonnull final ALangObject thisContext, @Nonnull final IEvaluationContext ec) {
		super(ec, NullUtil.stringFormat(CmnCnst.Error.NO_SUCH_FUNCTION_2,
				type, name, thisContext.inspect(), thisContext.getType()));
		this.name = name;
		this.thisContext = thisContext;
	}

	public final @Nonnull String name;
	public final @Nullable ALangObject thisContext;
}
