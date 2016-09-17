package de.xima.fc.form.expression.exception;

import org.jetbrains.annotations.Nullable;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.object.NumberLangObject;

public class EvaluationException extends Exception {
	public EvaluationException(final IEvaluationContext ec) {
		super();
		this.ec = ec;
	}

	public EvaluationException(final String msg) {
		super(msg);
		ec = null;
	}

	public EvaluationException(final IEvaluationContext ec, final String msg) {
		super(msg);
		this.ec = ec;
	}

	public EvaluationException(final IEvaluationContext ec, final String msg, final Throwable throwable) {
		super(msg, throwable);
		this.ec = ec;
	}

	/**
	 * May be null when thrown from
	 * {@link NumberLangObject#divide(NumberLangObject)} etc. that do not have
	 * access to the context.
	 */
	@Nullable
	public final IEvaluationContext ec;
}
