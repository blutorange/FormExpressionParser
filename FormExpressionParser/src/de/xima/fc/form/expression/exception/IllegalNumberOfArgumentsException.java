package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IFunction;
import de.xima.fc.form.expression.util.CmnCnst;

public class IllegalNumberOfArgumentsException extends CatchableEvaluationException {
	private static final long serialVersionUID = 1L;
	public final int isCount, expectedCountMin, expectedCountMax;
	public final String functionName;

	public IllegalNumberOfArgumentsException(final IFunction<?> function, final int isCount, final IEvaluationContext ec) {
		this(function.getDeclaredName(), isCount, function.getDeclaredArgumentList().length, ec);
	}

	public IllegalNumberOfArgumentsException(final String functionName, final int isCount, final int expectedCount,
			final IEvaluationContext ec) {
		super(ec, String.format(CmnCnst.Error.ILLEGAL_NUMBER_OF_ARGUMENTS_EXACT, isCount, functionName, expectedCount));
		this.isCount = isCount;
		expectedCountMin = expectedCount;
		expectedCountMax = expectedCount;
		this.functionName = functionName;
	}

	public IllegalNumberOfArgumentsException(final String functionName, final int isCount, final int expectedCountMin,
			final int expectedCountMax, final IEvaluationContext ec) {
		super(ec, String.format(CmnCnst.Error.ILLEGAL_NUMBER_OF_ARGUMENTS_RANGE, isCount, functionName, expectedCountMin,expectedCountMax ));
		this.isCount = isCount;
		this.expectedCountMin = expectedCountMin;
		this.expectedCountMax = expectedCountMax;
		this.functionName = functionName;
	}
}
