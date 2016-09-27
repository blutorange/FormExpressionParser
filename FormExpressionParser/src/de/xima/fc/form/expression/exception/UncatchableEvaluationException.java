package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.context.IBinding;
import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.INamespace;

/**
 * An evaluation exception that cannot be caught by the program itself via try-catch-clauses.
 * @author awa
 */
public class UncatchableEvaluationException extends EvaluationException {

	public UncatchableEvaluationException(final IEvaluationContext ec) {
		super(ec);
	}

	protected UncatchableEvaluationException(final String msg) {
		super(msg);
	}

	public UncatchableEvaluationException(final IBinding binding, final String msg) {
		super(binding, msg);
	}

	public UncatchableEvaluationException(final INamespace namespace, final String msg) {
		super(namespace, msg);
	}

	public UncatchableEvaluationException(final IEvaluationContext ec, final String msg) {
		super(ec, msg);
	}

	protected UncatchableEvaluationException(final String msg, final Throwable throwable) {
		super(msg, throwable);
	}

	public UncatchableEvaluationException(final IEvaluationContext ec, final String msg, final Throwable throwable) {
		super(ec, msg, throwable);
	}
}
