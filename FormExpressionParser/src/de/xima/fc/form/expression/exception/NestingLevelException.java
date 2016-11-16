package de.xima.fc.form.expression.exception;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.iface.context.IEvaluationContext;

public class NestingLevelException extends UncatchableEvaluationException {
	private static final long serialVersionUID = 1L;
	public NestingLevelException(final @Nonnull String msg, final @Nonnull IEvaluationContext ec) {
		super(ec, msg);
	}
}
