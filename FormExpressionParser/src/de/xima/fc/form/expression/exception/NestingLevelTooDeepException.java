package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.context.IBinding;

public class NestingLevelTooDeepException extends CatchableEvaluationException {
	public NestingLevelTooDeepException(int nestingDepth, IBinding binding) {
		super(binding, "Nesting level too deep: " + nestingDepth);
		this.nestingDepth = nestingDepth;
	}
	public final int nestingDepth; 
}
