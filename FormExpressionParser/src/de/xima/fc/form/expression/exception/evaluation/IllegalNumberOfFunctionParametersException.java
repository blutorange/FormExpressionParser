package de.xima.fc.form.expression.exception.evaluation;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.parse.IArgumentResolvable;
import de.xima.fc.form.expression.object.FunctionLangObject;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public class IllegalNumberOfFunctionParametersException extends CatchableEvaluationException {
	private static final long serialVersionUID = 1L;

	public IllegalNumberOfFunctionParametersException(final int shouldCount, final int isCount, final boolean isVarArgs,
			final IEvaluationContext ec) {
		super(ec, NullUtil.messageFormat(CmnCnst.Error.ILLEGAL_ARGUMENT_COUNT,
				Integer.valueOf(shouldCount - (isVarArgs ? 1 : 0)), Integer.valueOf(isCount)));
		this.isCount = isCount;
		this.isVarArgs = isVarArgs;
		this.shouldCountMin = shouldCount - (isVarArgs ? 1 : 0);
	}

	public IllegalNumberOfFunctionParametersException(final IArgumentResolvable node, final int isCount,
			final IEvaluationContext ec) {
		this(node.getArgumentCount(), isCount, node.hasVarArgs(), ec);
	}

	public IllegalNumberOfFunctionParametersException(final FunctionLangObject func, final int isCount,
			final IEvaluationContext ec) {
		this(func.getDeclaredArgumentCount(), isCount, func.hasVarArgs(), ec);
	}

	public final int isCount;
	public final int shouldCountMin;
	public final boolean isVarArgs;
}