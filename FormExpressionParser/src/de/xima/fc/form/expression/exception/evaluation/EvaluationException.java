package de.xima.fc.form.expression.exception.evaluation;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang3.StringUtils;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.IPositionedError;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IExternalContext;
import de.xima.fc.form.expression.iface.evaluate.ITraceElement;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public class EvaluationException extends Exception implements IPositionedError {
	private static final long serialVersionUID = 1L;

	private static int beginLine, beginColumn, endLine, endColumn;
	private static boolean hasPosition;

	public EvaluationException(@Nullable final EvaluationException exception) {
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

	public EvaluationException(final IEvaluationContext ec) {
		super(msgWithContext(StringUtils.EMPTY, ec));
		this.ec = ec;
		externalContext = ec.getExternalContext();
	}

	public EvaluationException(final IEvaluationContext ec, final Throwable cause) {
		super(msgWithContext(cause.getMessage(), ec));
		this.ec = ec;
		externalContext = ec.getExternalContext();
	}

	public EvaluationException(final IEvaluationContext ec, final String msg) {
		super(msgWithContext(msg, ec));
		this.ec = ec;
		externalContext = ec.getExternalContext();
	}

	/**
	 * Used by other visitors.
	 *
	 * @param msg
	 *            Message.
	 * @param throwable
	 *            Throwable.
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
			setPositionInfo(ec.getTracer().getCurrentlyProcessed());
			appendTraceElement(sb, ec.getTracer().getCurrentlyProcessed());
			for (final ITraceElement el : ec.getTracer().getStackTrace()) {
				sb.append(System.lineSeparator());
				appendTraceElement(sb, el);
			}
		}
		sb.append(System.lineSeparator());

		if (ec != null)
			sb.append(NullUtil.messageFormat(CmnCnst.Error.EVALUATION_EXCEPTION_KNOWN_EC, ec,
					ec.getClass().getCanonicalName()));
		else
			sb.append(NullUtil.messageFormat(CmnCnst.Error.EVALUATION_EXCEPTION_UNKNOWN_EC));

		sb.append(System.lineSeparator());

		if (ec != null) {
			appendObject(sb, CmnCnst.Error.EVALUATION_EXCEPTION_NAMESPACE, ec.getNamespace());
			appendObject(sb, CmnCnst.Error.EVALUATION_EXCEPTION_LIBRARY, ec.getLibrary());
			appendObject(sb, CmnCnst.Error.EVALUATION_EXCEPTION_EMBEDMENT, ec.getEmbedment());
			appendObject(sb, CmnCnst.Error.EVALUATION_EXCEPTION_LOGGER, ec.getLogger());
			appendObject(sb, CmnCnst.Error.EVALUATION_EXCEPTION_TRACER, ec.getTracer());
		}

		final IExternalContext ex = ec != null ? ec.getExternalContext() : null;
		if (ex != null)
			appendObject(sb, CmnCnst.Error.EVALUATION_EXCEPTION_KNOWN_EX, ex);
		else
			sb.append(NullUtil.messageFormat(CmnCnst.Error.EVALUATION_EXCEPTION_UNKNOWN_EX));

		return NullUtil.checkNotNull(sb.toString());
	}

	private static void setPositionInfo(@Nullable final Node node) {
		if (node != null) {
			hasPosition = true;
			beginLine = node.getBeginLine();
			beginColumn = node.getBeginColumn();
			endLine = node.getEndLine();
			endColumn = node.getEndColumn();
		}
	}

	private static void appendObject(final StringBuilder sb, final String format, final Object object) {
		sb.append(NullUtil.messageFormat(format, object, object.getClass().getCanonicalName()))
				.append(System.lineSeparator());
	}

	private static void appendTraceElement(final StringBuilder sb, @Nullable final ITraceElement el) {
		sb.append('\t');
		if (el != null)
			sb.append(NullUtil.messageFormat(CmnCnst.Error.TRACER_KNOWN_POSITION, el.getMethodName(), el.getBeginLine(),
					el.getBeginColumn()));
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

	@Override
	public boolean isPositionInformationAvailable() {
		return hasPosition;
	}

	@Override
	public int getBeginLine() {
		return beginLine;
	}

	@Override
	public int getEndLine() {
		return endLine;
	}

	@Override
	public int getBeginColumn() {
		return beginColumn;
	}

	@Override
	public int getEndColumn() {
		return endColumn;
	}
}