package de.xima.fc.form.expression.exception.evaluation;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.iface.context.IExternalContext;

/**
 * An evaluation exception that can be caught by the program itself via try-catch-clauses.
 * @author awa
 */
public class CatchableEvaluationException extends EvaluationException {
	private static final long serialVersionUID = 1L;

	public CatchableEvaluationException(@Nonnull final IEvaluationContext ec) {
		super(ec);
	}
	public CatchableEvaluationException(@Nonnull final IEvaluationContext ec, @Nonnull final String msg) {
		super(ec, msg);
	}

	public CatchableEvaluationException(@Nonnull final IExternalContext externalContext, @Nonnull final String msg, @Nonnull final Throwable throwable) {
		super(externalContext, msg, throwable);
	}

	public CatchableEvaluationException(@Nonnull final IEvaluationContext ec, @Nonnull final String msg, @Nonnull final Throwable throwable) {
		super(ec, msg, throwable);
	}

}
