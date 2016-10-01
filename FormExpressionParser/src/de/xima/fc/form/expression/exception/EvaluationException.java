package de.xima.fc.form.expression.exception;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.ITraceElement;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.util.CmnCnst;

public class EvaluationException extends RuntimeException {
	public EvaluationException(final EvaluationException exception) {
		super(exception);
		if (exception != null) {
			ec = exception.ec;
		}
		else {
			ec = null;
		}
	}

	public EvaluationException(final IEvaluationContext ec) {
		super(msgWithContext(StringUtils.EMPTY, ec));
		this.ec = ec;
	}

	public EvaluationException(final IEvaluationContext ec, final String msg) {
		super(msgWithContext(msg, ec));
		this.ec = ec;
	}

	/**
	 * Used by other visitors.
	 * @param msg Message.
	 * @param throwable Throwable.
	 */
	protected EvaluationException(final String msg, final Throwable throwable) {
		super(msg, throwable);
		ec = null;
	}

	public EvaluationException(final IEvaluationContext ec, final String msg, final Throwable throwable) {
		super(msgWithContext(msg, ec), throwable);
		this.ec = ec;
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
		appendTraceElement(sb, ec.getTracer().getCurrentlyProcessed());
		for (final ITraceElement el : ec.getTracer().getStackTrace()) {
			sb.append(System.lineSeparator());
			appendTraceElement(sb, el);
		}
		sb.append(System.lineSeparator());
		sb.append("Evaluation context is ");
		sb.append(ec);
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
}
