package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.context.IEvaluationContext;

public class NestingLevelTooDeepException extends CatchableEvaluationException {
	public NestingLevelTooDeepException(final int nestingDepth, final IEvaluationContext ec) {
		super(ec, "Nesting level too deep: " + nestingDepth);
		this.nestingDepth = nestingDepth;
	}
	public final int nestingDepth;
}
