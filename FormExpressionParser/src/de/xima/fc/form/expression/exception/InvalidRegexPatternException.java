package de.xima.fc.form.expression.exception;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class InvalidRegexPatternException extends CatchableEvaluationException {
	private static final long serialVersionUID = 1L;
	public InvalidRegexPatternException(@Nonnull final String pattern, final int flags, @Nonnull final IEvaluationContext ec) {
		super(ec, NullUtil.format(CmnCnst.Error.INVALID_REGEX_PATTERN, pattern, new Integer(flags)));
		this.pattern = pattern;
		this.flags = flags;
	}
	public final String pattern;
	public final int flags;
}
