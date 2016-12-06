package de.xima.fc.form.expression.exception.parse;

import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class NestingLevelTooDeepException extends NestingLevelException {
	private static final long serialVersionUID = 1L;
	public NestingLevelTooDeepException(final int nestingDepth) {
		super(NullUtil.stringFormat(CmnCnst.Error.NESTING_LEVEL_TOO_DEEP, nestingDepth));
		this.nestingDepth = nestingDepth;
	}
	public final int nestingDepth;
}
