package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.context.IEvaluationContext;

public class IllegalNumberOfArgumentsException extends CatchableEvaluationException {
	public final int isCount, expectedCountMin, expectedCountMax;
	public final String functionName;

	public IllegalNumberOfArgumentsException(final String functionName, final int isCount, final int expectedCount,
			final IEvaluationContext ec) {
		super(ec, "Encountered " + isCount + " argument(s) for function " + functionName + ", " + "expected at least " + expectedCount);
		this.isCount = isCount;
		expectedCountMin = expectedCount;
		expectedCountMax = expectedCount;
		this.functionName = functionName;
	}

	public IllegalNumberOfArgumentsException(final String functionName, final int isCount, final int expectedCountMin,
			final int expectedCountMax, final IEvaluationContext ec) {
		super(ec, "Encountered " + isCount + "number of arguments for function " + functionName + System.lineSeparator()
		+ "expected " + expectedCountMin + " to " + expectedCountMax);
		this.isCount = isCount;
		this.expectedCountMin = expectedCountMin;
		this.expectedCountMax = expectedCountMax;
		this.functionName = functionName;
	}
}
