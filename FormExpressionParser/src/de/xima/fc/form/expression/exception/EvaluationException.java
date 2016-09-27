package de.xima.fc.form.expression.exception;

import org.jetbrains.annotations.Nullable;

import de.xima.fc.form.expression.context.IBinding;
import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.INamespace;
import de.xima.fc.form.expression.object.NumberLangObject;

public class EvaluationException extends RuntimeException {
	public EvaluationException(final EvaluationException exception) {
		super(exception);
		if (exception != null) {
			ec = exception.ec;
			binding = exception.binding;
			namespace = exception.namespace;
		}
		else {
			ec = null;
			binding = null;
			namespace = null;
		}
	}

	public EvaluationException(final IEvaluationContext ec) {
		super();
		this.ec = ec;
		binding = ec == null ? null : ec.getBinding();
		namespace = ec == null ? null : ec.getNamespace();
	}

	protected EvaluationException(final String msg) {
		super(msg);
		ec = null;
		binding = null;
		namespace = null;
	}

	public EvaluationException(final IBinding binding, final String msg) {
		super(msg);
		ec = null;
		this.binding = binding;
		this.namespace = null;
	}

	public EvaluationException(final INamespace namespace, final String msg) {
		super(msg);
		ec = null;
		this.binding = null;
		this.namespace = namespace;
	}

	public EvaluationException(final IEvaluationContext ec, final String msg) {
		super(msg);
		this.ec = ec;
		binding = ec == null ? null : ec.getBinding();
		namespace = ec == null ? null : ec.getNamespace();
	}

	protected EvaluationException(final String msg, final Throwable throwable) {
		super(msg, throwable);
		ec = null;
		binding = null;
		namespace = null;
	}

	public EvaluationException(final IEvaluationContext ec, final String msg, final Throwable throwable) {
		super(msg, throwable);
		this.ec = ec;
		binding = ec == null ? null : ec.getBinding();
		namespace = ec == null ? null : ec.getNamespace();
	}

	/**
	 * May be null when thrown from
	 * {@link NumberLangObject#divide(NumberLangObject)} etc. that do not have
	 * access to the context.
	 */
	@Nullable
	public final IEvaluationContext ec;
	@Nullable
	public final IBinding binding;
	@Nullable
	public final INamespace namespace;
}
