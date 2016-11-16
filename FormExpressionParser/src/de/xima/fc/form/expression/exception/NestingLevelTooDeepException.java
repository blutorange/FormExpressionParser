package de.xima.fc.form.expression.exception;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class NestingLevelTooDeepException extends CatchableEvaluationException {
	private static final long serialVersionUID = 1L;
	public NestingLevelTooDeepException(final int nestingDepth, @Nonnull final IEvaluationContext ec) {
		super(ec, NullUtil.format(CmnCnst.Error.NESTING_LEVEL_TOO_DEEP, nestingDepth));
		this.nestingDepth = nestingDepth;
	}
	public final int nestingDepth;
}
