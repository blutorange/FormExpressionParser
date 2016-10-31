package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.util.CmnCnst;

public class NestingLevelTooDeepException extends CatchableEvaluationException {
	private static final long serialVersionUID = 1L;
	public NestingLevelTooDeepException(final int nestingDepth, final IEvaluationContext ec) {
		super(ec, String.format(CmnCnst.Error.NESTING_LEVEL_TOO_DEEP, nestingDepth));
		this.nestingDepth = nestingDepth;
	}
	public final int nestingDepth;
}
