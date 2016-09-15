package de.xima.fc.form.expression.error;

import de.xima.fc.form.expression.context.IEvaluationContext;

public class EvaluationException extends Exception {
	public EvaluationException(final IEvaluationContext ec) {
		super();
		this.ec = ec;
	}

	public EvaluationException(final IEvaluationContext ec, final String msg) {
		super(msg);
		this.ec = ec;
	}

	public EvaluationException(final IEvaluationContext ec, final String msg, final Throwable throwable) {
		super(msg, throwable);
		this.ec = ec;
	}

	public final IEvaluationContext ec;
}
