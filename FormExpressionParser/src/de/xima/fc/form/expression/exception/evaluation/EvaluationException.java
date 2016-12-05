package de.xima.fc.form.expression.exception.evaluation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IExternalContext;
import de.xima.fc.form.expression.iface.evaluate.ITraceElement;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.util.CmnCnst;

public class EvaluationException extends Exception {
	private static final long serialVersionUID = 1L;

	public EvaluationException(final EvaluationException exception) {
		super(exception);
		if (exception != null) {
			ec = exception.ec;
			externalContext = ec != null ? ec.getExternalContext() : null;
		}
		else {
			ec = null;
			externalContext = null;
		}
	}

	public EvaluationException(@Nonnull final IEvaluationContext ec) {
		super(msgWithContext(StringUtils.EMPTY, ec));
		this.ec = ec;
		externalContext = ec.getExternalContext();
	}

	public EvaluationException(@Nonnull final IEvaluationContext ec, @Nonnull final String msg) {
		super(msgWithContext(msg, ec));
		this.ec = ec;
		externalContext = ec.getExternalContext();
	}

	/**
	 * Used by other visitors.
	 * @param msg Message.
	 * @param throwable Throwable.
	 */
	protected EvaluationException(@Nonnull final String msg, @Nonnull final Throwable throwable) {
		super(msg, throwable);
		ec = null;
		externalContext = null;
	}

	protected EvaluationException(@Nonnull final IExternalContext externalContext, @Nonnull final String msg,
			@Nonnull final Throwable throwable) {
		super(msg, throwable);
		ec = null;
		this.externalContext = externalContext;
	}

	public EvaluationException(@Nonnull final IEvaluationContext ec, @Nonnull final String msg,
			@Nonnull final Throwable throwable) {
		super(msgWithContext(msg, ec), throwable);
		this.ec = ec;
		externalContext = ec.getExternalContext();
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
	private static String msgWithContext(@Nullable final String msg, @Nullable final IEvaluationContext ec) {
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
		.append(CmnCnst.Error.EVALUATION_EXCEPTION_EC)
		.append(' ')
		.append(ec);
		if (ec != null) {
			sb.append('(')
			.append(ec.getClass().getCanonicalName())
			.append(')')
			.append(System.lineSeparator())
			.append(CmnCnst.Error.EVALUATION_EXCEPTION_EX)
			.append(' ')
			.append(ec.getExternalContext());
			final IExternalContext ex = ec.getExternalContext();
			if (ex != null) {
				sb.append('(')
				.append(ex.getClass().getCanonicalName())
				.append(')');
			}
		}
		return sb.toString();
	}

	private static void appendTraceElement(@Nonnull final StringBuilder sb, @Nullable final ITraceElement el) {
		sb.append('\t')
		.append(CmnCnst.Error.EVALUATION_EXCEPTION_AT)
		.append(' ')
		.append(el == null ? CmnCnst.TRACER_POSITION_NAME_UNKNOWN : el.getMethodName())
		.append(' ')
		.append('(');
		if (el == null) {
			sb.append(CmnCnst.TRACER_POSITION_UNKNOWN);
		}
		else {
			sb.append(CmnCnst.Error.EVALUATION_EXCEPTION_LINE)
			.append(' ')
			.append(el.getStartLine())
			.append(',')
			.append(' ')
			.append(CmnCnst.Error.EVALUATION_EXCEPTION_COLUMN)
			.append(' ')
			.append(el.getStartColumn());
		}
		sb.append(')');
	}

	/**
	 * May be null when thrown from
	 * {@link NumberLangObject#divide(NumberLangObject)} etc. that do not have
	 * access to the context.
	 */
	@Nullable
	public final transient IEvaluationContext ec;

	@Nullable
	public final transient IExternalContext externalContext;
}
