package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.context.IBinding;
import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.INamespace;

/**
 * An evaluation exception that can be caught by the program itself via try-catch-clauses.
 * @author awa
 */
public class CatchableEvaluationException extends EvaluationException {
	public CatchableEvaluationException(final IEvaluationContext ec) {
		super(ec);
	}

	protected CatchableEvaluationException(final String msg) {
		super(msg);
	}

	public CatchableEvaluationException(final IBinding binding, final String msg) {
		super(binding, msg);
	}

	public CatchableEvaluationException(final INamespace namespace, final String msg) {
		super(namespace, msg);
	}

	public CatchableEvaluationException(final IEvaluationContext ec, final String msg) {
		super(ec, msg);
	}

	protected CatchableEvaluationException(final String msg, final Throwable throwable) {
		super(msg, throwable);
	}

	public CatchableEvaluationException(final IEvaluationContext ec, final String msg, final Throwable throwable) {
		super(ec, msg, throwable);
	}

}
