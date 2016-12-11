package de.xima.fc.form.expression.exception.evaluation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IExternalContext;
import de.xima.fc.form.expression.iface.evaluate.ITraceElement;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

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
		sb.append(System.lineSeparator());
		
		if (ec != null)
			sb.append(NullUtil.messageFormat(CmnCnst.Error.EVALUATION_EXCEPTION_KNOWN_EC, ec, ec.getClass().getCanonicalName()));
		else 
			sb.append(NullUtil.messageFormat(CmnCnst.Error.EVALUATION_EXCEPTION_UNKNOWN_EC));
		
		sb.append(System.lineSeparator());
		
		final IExternalContext ex = ec != null ? ec.getExternalContext() : null;
		if (ex != null)
			sb.append(NullUtil.messageFormat(CmnCnst.Error.EVALUATION_EXCEPTION_KNOWN_EX, ex, ex.getClass().getCanonicalName()));
		else 
			sb.append(NullUtil.messageFormat(CmnCnst.Error.EVALUATION_EXCEPTION_UNKNOWN_EX));

		return sb.toString();
	}

	private static void appendTraceElement(@Nonnull final StringBuilder sb, @Nullable final ITraceElement el) {
		sb.append('\t');
		if (el != null)
			sb.append(NullUtil.messageFormat(CmnCnst.Error.TRACER_KNOWN_POSITION, el.getMethodName(), el.getStartLine(), el.getStartColumn()));
		else
			sb.append(NullUtil.messageFormat(CmnCnst.Error.TRACER_UNKNOWN_POSITION));
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
