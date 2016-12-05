package de.xima.fc.form.expression.exception.evaluation;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IFunction;
import de.xima.fc.form.expression.iface.parse.IArgumentResolvable;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class IllegalNumberOfFunctionParametersException extends CatchableEvaluationException {
	private static final long serialVersionUID = 1L;
	public IllegalNumberOfFunctionParametersException(final int shouldCount, final int isCount, final boolean isVarArgs, @Nonnull final IEvaluationContext ec) {
		super(ec, NullUtil.format(CmnCnst.Error.ILLEGAL_ARGUMENT_COUNT, shouldCount - (isVarArgs ? 1 : 0), isCount));
		this.isCount = isCount;
		this.isVarArgs = isVarArgs;
		this.shouldCountMin = shouldCount - (isVarArgs ? 1 : 0);
	}
	public IllegalNumberOfFunctionParametersException(@Nonnull final IArgumentResolvable node, final int isCount, @Nonnull final IEvaluationContext ec) {
		this(node.getArgumentCount(), isCount, node.hasVarArgs(), ec);
	}
	public IllegalNumberOfFunctionParametersException(@Nonnull final IFunction<?> func, final int isCount, @Nonnull final IEvaluationContext ec) {
		this(func.getDeclaredArgumentCount(), isCount, func.hasVarArgs(), ec);
	}
	public final int isCount;
	public final int shouldCountMin;
	public final boolean isVarArgs;
}