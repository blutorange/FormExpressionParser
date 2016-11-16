package de.xima.fc.form.expression.exception;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Function;
import com.google.common.base.Optional;

import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.iface.context.IExternalContext;
import de.xima.fc.form.expression.iface.context.ITraceElement;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.util.CmnCnst;

public class EvaluationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public EvaluationException(final EvaluationException exception) {
		super(exception);
		if (exception != null) {
			ec = exception.ec;
			externalContext = exception.ec.transform(new Function<IEvaluationContext, Optional<IExternalContext>>(){
				@Override
				public Optional<IExternalContext> apply(final IEvaluationContext ec) {
					return ec != null ? ec.getExternalContext() : Optional.<IExternalContext>absent();
				}
			}).or(Optional.<IExternalContext>absent());
		}
		else {
			ec = Optional.absent();
			externalContext = Optional.absent();
		}
	}

	public EvaluationException(@Nonnull final IEvaluationContext ec) {
		super(msgWithContext(StringUtils.EMPTY, ec));
		this.ec = Optional.fromNullable(ec);
		externalContext = ec.getExternalContext();
	}

	public EvaluationException(@Nonnull final IEvaluationContext ec, @Nonnull final String msg) {
		super(msgWithContext(msg, ec));
		this.ec = Optional.fromNullable(ec);
		externalContext = ec.getExternalContext();
	}

	/**
	 * Used by other visitors.
	 * @param msg Message.
	 * @param throwable Throwable.
	 */
	protected EvaluationException(@Nonnull final String msg, @Nonnull final Throwable throwable) {
		super(msg, throwable);
		ec = Optional.absent();
		externalContext = Optional.absent();
	}

	protected EvaluationException(@Nonnull final IExternalContext externalContext, @Nonnull final String msg,
			@Nonnull final Throwable throwable) {
		super(msg, throwable);
		ec = Optional.absent();
		this.externalContext = Optional.fromNullable(externalContext);
	}

	public EvaluationException(@Nonnull final IEvaluationContext ec, @Nonnull final String msg,
			@Nonnull final Throwable throwable) {
		super(msgWithContext(msg, ec), throwable);
		this.ec = Optional.fromNullable(ec);
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
			final Optional<IExternalContext> ex = ec.getExternalContext();
			if (ex.isPresent()) {
				sb.append('(')
				.append(ex.get().getClass().getCanonicalName())
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
	@Nonnull
	public final Optional<IEvaluationContext> ec;

	@Nonnull
	public final Optional<IExternalContext> externalContext;
}
