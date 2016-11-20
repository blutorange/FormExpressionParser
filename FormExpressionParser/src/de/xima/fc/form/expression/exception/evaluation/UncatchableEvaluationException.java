package de.xima.fc.form.expression.exception.evaluation;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.iface.context.IEvaluationContext;

/**
 * An evaluation exception that cannot be caught by the program itself via
 * try-catch-clauses.
 *
 * @author awa
 */
public class UncatchableEvaluationException extends EvaluationException {
	private static final long serialVersionUID = 1L;

	public UncatchableEvaluationException(@Nonnull final IEvaluationContext ec) {
		super(ec);
	}

	public UncatchableEvaluationException(@Nonnull final IEvaluationContext ec, @Nonnull final String msg) {
		super(ec, msg);
	}

	protected UncatchableEvaluationException(@Nonnull final String msg, @Nonnull final Throwable throwable) {
		super(msg, throwable);
	}

	public UncatchableEvaluationException(@Nonnull final IEvaluationContext ec, @Nonnull final String msg,
			@Nonnull final Throwable throwable) {
		super(ec, msg, throwable);
	}
}
