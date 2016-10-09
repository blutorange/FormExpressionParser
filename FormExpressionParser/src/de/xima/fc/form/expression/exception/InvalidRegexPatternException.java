package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.context.IEvaluationContext;

public class InvalidRegexPatternException extends CatchableEvaluationException {

	public InvalidRegexPatternException(String pattern, int flags, IEvaluationContext ec) {
		super(ec, String.format("Invalid regex pattern %s and/or flags %s.", pattern, new Integer(flags)));
		this.pattern = pattern;
		this.flags = flags;
	}
	public final String pattern;
	public final int flags;
}
