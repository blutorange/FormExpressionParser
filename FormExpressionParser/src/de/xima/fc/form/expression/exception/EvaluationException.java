package de.xima.fc.form.expression.exception;

import org.jetbrains.annotations.Nullable;

import de.xima.fc.form.expression.context.IBinding;
import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.object.NumberLangObject;

public class EvaluationException extends RuntimeException {
	public EvaluationException(final EvaluationException exception) {
		super(exception);
		ec = exception.ec;
		binding = exception.binding;
	}

	public EvaluationException(final IEvaluationContext ec) {
		super();
		this.ec = ec;
		binding = ec == null ? null : ec.getBinding();
	}

	protected EvaluationException(final String msg) {
		super(msg);
		ec = null;
		binding = null;
	}

	public EvaluationException(final IBinding binding, final String msg) {
		super(msg);
		ec = null;
		this.binding = binding;
	}

	public EvaluationException(final IEvaluationContext ec, final String msg) {
		super(msg);
		this.ec = ec;
		binding = ec == null ? null : ec.getBinding();
	}

	protected EvaluationException(final String msg, final Throwable throwable) {
		super(msg, throwable);
		ec = null;
		binding = null;
	}

	public EvaluationException(final IEvaluationContext ec, final String msg, final Throwable throwable) {
		super(msg, throwable);
		this.ec = ec;
		binding = ec == null ? null : ec.getBinding();
	}

	/**
	 * May be null when thrown from
	 * {@link NumberLangObject#divide(NumberLangObject)} etc. that do not have
	 * access to the context.
	 */
	@Nullable
	public final IEvaluationContext ec;
	public final IBinding binding;
}
