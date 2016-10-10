package de.xima.fc.form.expression.exception;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IExternalContext;
import de.xima.fc.form.expression.context.ITraceElement;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.util.CmnCnst;

public class EvaluationException extends RuntimeException {
	public EvaluationException(final EvaluationException exception) {
		super(exception);
		if (exception != null) {
			ec = exception.ec;
			externalContext = ec.getExternalContext();
		}
		else {
			ec = null;
			externalContext = null;
		}
	}

	public EvaluationException(final IEvaluationContext ec) {
		super(msgWithContext(StringUtils.EMPTY, ec));
		this.ec = ec;
		externalContext = ec == null ? null : ec.getExternalContext();
	}

	public EvaluationException(final IEvaluationContext ec, final String msg) {
		super(msgWithContext(msg, ec));
		this.ec = ec;
		externalContext = ec == null ? null : ec.getExternalContext();
	}

	/**
	 * Used by other visitors.
	 * @param msg Message.
	 * @param throwable Throwable.
	 */
	protected EvaluationException(final String msg, final Throwable throwable) {
		super(msg, throwable);
		ec = null;
		externalContext = null;
	}

	protected EvaluationException(final IExternalContext externalContext, final String msg, final Throwable throwable) {
		super(msg, throwable);
		ec = null;
		this.externalContext = externalContext;
	}

	public EvaluationException(final IEvaluationContext ec, final String msg, final Throwable throwable) {
		super(msgWithContext(msg, ec), throwable);
		this.ec = ec;
		externalContext = ec == null ? null : ec.getExternalContext();
	}

	/**
	 * Builds the exception and the stack trace.
	 *
	 * @param msg
	 *            Message of the exception.
	 * @param ec
	 *            Current context.
	 * @return The message with the stack trace.
	 */
	private static String msgWithContext(final String msg, final IEvaluationContext ec) {
		final StringBuilder sb = new StringBuilder();
		sb.append(msg).append(System.lineSeparator());
		if (ec != null) {
			appendTraceElement(sb, ec.getTracer().getCurrentlyProcessed());
			for (final ITraceElement el : ec.getTracer().getStackTrace()) {
				sb.append(System.lineSeparator());
				appendTraceElement(sb, el);
			}
		}
		sb.append(System.lineSeparator())
		.append("Evaluation context is ")
		.append(ec);
		if (ec != null) {
			sb.append('(')
			.append(ec.getClass().getCanonicalName())
			.append(')')
			.append(System.lineSeparator())
			.append("External context is ")
			.append(ec.getExternalContext());
			if (ec.getExternalContext() != null) {
				sb.append('(')
				.append(ec.getExternalContext().getClass().getCanonicalName())
				.append(')');
			}
		}
		return sb.toString();
	}

	private static void appendTraceElement(final StringBuilder sb, final ITraceElement el) {
		sb.append("\tat ").append(el == null ? CmnCnst.TRACER_POSITION_NAME_UNKNOWN : el.getMethodName())
		.append(" (");
		if (el == null) {
			sb.append(CmnCnst.TRACER_POSITION_UNKNOWN);
		}
		else {
			sb.append("line ").append(el.getStartLine()).append(", column ").append(el.getStartColumn());
		}
		sb.append(')');
	}

	/**
	 * May be null when thrown from
	 * {@link NumberLangObject#divide(NumberLangObject)} etc. that do not have
	 * access to the context.
	 */
	@Nullable
	public final IEvaluationContext ec;

	@Nullable
	public final IExternalContext externalContext;
}
