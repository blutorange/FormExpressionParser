package de.xima.fc.form.expression.exception;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import de.xima.fc.form.expression.context.IBinding;
import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.INamespace;
import de.xima.fc.form.expression.context.ITraceElement;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.util.CmnCnst;

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
		super(msgWithContext(StringUtils.EMPTY, ec));
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
		super(msgWithBinding(msg, binding));
		ec = null;
		this.binding = binding;
		this.namespace = null;
	}

	public EvaluationException(final INamespace namespace, final String msg) {
		super(msgWithNamespace(msg, namespace));
		ec = null;
		this.binding = null;
		this.namespace = namespace;
	}

	public EvaluationException(final IEvaluationContext ec, final String msg) {
		super(msgWithContext(msg, ec));
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
		super(msgWithContext(msg, ec), throwable);
		this.ec = ec;
		binding = ec == null ? null : ec.getBinding();
		namespace = ec == null ? null : ec.getNamespace();
	}

	/**
	 * Builds the exception and the stack trace.
	 * @param msg Message of the exception.
	 * @param ec Current context.
	 * @return The message with the stack trace.
	 */
	private static String msgWithContext(String msg, IEvaluationContext ec) {
		final StringBuilder sb = new StringBuilder();
		sb.append(msg).append(System.lineSeparator());
		appendTraceElement(sb, ec.getTracer().getCurrentlyProcessed());
		for (ITraceElement el : ec.getTracer().getStackTrace()) {
			sb.append(System.lineSeparator());
			appendTraceElement(sb, el);
		}
		sb.append(System.lineSeparator());
		sb.append("Evaluation context is ");
		sb.append(ec);
		return sb.toString();
	}	

	private static void appendTraceElement(StringBuilder sb, ITraceElement el) {
		sb.append("\tat ")
			.append(el == null ? CmnCnst.TRACER_POSITION_NAME_UNKNOWN : el.getPositionName())
			.append(" (");
		if (el == null) {
			sb.append(CmnCnst.TRACER_POSITION_UNKNOWN);
		}
		else {
			sb.append("line ")
				.append(el.getStartLine())
				.append(", column ")
				.append(el.getStartColumn());
		}		
		sb.append(')');
	}
	
	private static String msgWithBinding(String msg, IBinding binding) {
		return String.format("%s%sBinding is %s.", msg, System.lineSeparator(), String.valueOf(binding));
	}

	private static String msgWithNamespace(String msg, INamespace namespace) {
		return String.format("%s%sNamespace is %s.", msg, System.lineSeparator(), String.valueOf(namespace));
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
