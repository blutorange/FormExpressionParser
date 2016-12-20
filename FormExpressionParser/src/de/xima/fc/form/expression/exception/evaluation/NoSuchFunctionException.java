package de.xima.fc.form.expression.exception.evaluation;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public class NoSuchFunctionException extends CatchableEvaluationException {
	private static final long serialVersionUID = 1L;

	public NoSuchFunctionException(final String type, final String name, final ALangObject thisContext,
			final IEvaluationContext ec) {
		super(ec,
				NullUtil.messageFormat(
						thisContext.isNull() ? CmnCnst.Error.NO_SUCH_FUNCTION_WITH_NULL
								: CmnCnst.Error.NO_SUCH_FUNCTION_WITH_THIS,
						type, name, thisContext.inspect()));
		this.name = name;
		this.thisContext = thisContext;
	}

	public final String name;
	@Nullable
	public final ALangObject thisContext;
}