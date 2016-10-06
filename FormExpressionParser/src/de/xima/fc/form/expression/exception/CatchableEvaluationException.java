package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IExternalContext;

/**
 * An evaluation exception that can be caught by the program itself via try-catch-clauses.
 * @author awa
 */
public class CatchableEvaluationException extends EvaluationException {
	public CatchableEvaluationException(final IEvaluationContext ec) {
		super(ec);
	}
	public CatchableEvaluationException(final IEvaluationContext ec, final String msg) {
		super(ec, msg);
	}

	public CatchableEvaluationException(IExternalContext externalContext, final String msg, Throwable throwable) {
		super(externalContext, msg, throwable);
	}
	
	public CatchableEvaluationException(final IEvaluationContext ec, final String msg, final Throwable throwable) {
		super(ec, msg, throwable);
	}

}
