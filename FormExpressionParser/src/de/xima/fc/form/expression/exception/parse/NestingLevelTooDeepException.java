package de.xima.fc.form.expression.exception.parse;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public class NestingLevelTooDeepException extends NestingLevelException {
	private static final long serialVersionUID = 1L;

	public NestingLevelTooDeepException(final int nestingDepth) {
		super(NullUtil.messageFormat(CmnCnst.Error.NESTING_LEVEL_TOO_DEEP, Integer.valueOf(nestingDepth)));
		this.nestingDepth = nestingDepth;
	}

	public final int nestingDepth;
}