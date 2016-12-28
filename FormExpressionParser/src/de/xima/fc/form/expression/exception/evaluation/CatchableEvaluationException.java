package de.xima.fc.form.expression.exception.evaluation;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IExternalContext;

/**
 * An evaluation exception that can be caught by the program itself via try-catch-clauses.
 * @author awa
 */
@NonNullByDefault
public class CatchableEvaluationException extends EvaluationException {
	private static final long serialVersionUID = 1L;

	public CatchableEvaluationException(final IEvaluationContext ec) {
		super(ec);
	}
	public CatchableEvaluationException(final IEvaluationContext ec, final Throwable cause) {
		super(ec, cause);
	}
	public CatchableEvaluationException(final IEvaluationContext ec, final String msg) {
		super(ec, msg);
	}

	public CatchableEvaluationException(final IExternalContext externalContext, final String msg, final Throwable throwable) {
		super(externalContext, msg, throwable);
	}

	public CatchableEvaluationException(final IEvaluationContext ec, final String msg, final Throwable throwable) {
		super(ec, msg, throwable);
	}

}
