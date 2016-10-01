package de.xima.fc.form.expression.exception;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import de.xima.fc.form.expression.context.IBinding;
import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.INamespace;
import de.xima.fc.form.expression.grammar.Node;
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

	@SuppressWarnings("boxing")
	private static String msgWithContext(String msg, IEvaluationContext ec) {
		final Node n = ec.getEvaluationVisitor().getCurrentlyProcessedNode();
		if (n == null) return String.format("(line unknown) %s%sEvaluation context is %s.", msg, System.lineSeparator(), String.valueOf(ec));
		return String.format("(line %s, column %s) %s%sEvaluation context is %s.", n.getStartLine(), n.getStartColumn(), msg, System.lineSeparator(), String.valueOf(ec));
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
