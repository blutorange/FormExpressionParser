package de.xima.fc.form.expression.impl;

import javax.annotation.Nonnull;

import com.google.common.base.Optional;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.context.IEmbedment;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.iface.context.IExternalContext;
import de.xima.fc.form.expression.iface.context.ILogger;
import de.xima.fc.form.expression.iface.context.INamespace;
import de.xima.fc.form.expression.iface.context.IScope;
import de.xima.fc.form.expression.iface.context.ITracer;
import de.xima.fc.form.expression.impl.ReadScopedEvaluationContext.Builder;

public abstract class GenericEvaluationContext implements IEvaluationContext {

	@Nonnull protected final INamespace namespace;
	@Nonnull protected final ILogger logger;
	@Nonnull protected final ITracer<Node> tracer;
	@Nonnull protected final IScope scope;
	@Nonnull protected final IEmbedment embedment;
	@Nonnull protected Optional<IExternalContext> externalContext = Optional.absent();

	/**
	 * Creates a new evaluation context.
	 *
	 * @param binding
	 *            Binding to use.
	 * @param namespace
	 *            Namespace to use.
	 * @param logger
	 *            The logger used for logging.
	 */
	public GenericEvaluationContext(@Nonnull final IScope scope,
			@Nonnull final INamespace namespace, @Nonnull final ITracer<Node> tracer,
			@Nonnull final IEmbedment embedment, @Nonnull final ILogger logger) {
		this.namespace = namespace;
		this.scope = scope;
		this.tracer = tracer;
		this.logger = logger;
		this.embedment = embedment;
	}

	@Override
	public INamespace getNamespace() {
		return namespace;
	}

	@Override
	public ILogger getLogger() {
		return logger;
	}

	@Override
	public IEmbedment getEmbedment() {
		return embedment;
	}

	@Override
	public boolean variableNameEquals(final String name1, final String name2) {
		return name1.equals(name2);
	}

	@Override
	public IScope getScope() {
		return scope;
	}

	@Override
	public ITracer<Node> getTracer() {
		return tracer;
	}

	@Override
	public void reset() {
		scope.reset();
		embedment.reset();
		tracer.reset();
		externalContext = Optional.absent();
	}

	@Override
	public void setExternalContext(final IExternalContext externalContext) {
		this.externalContext = Optional.fromNullable(externalContext);
	}

	@Override
	public Optional<IExternalContext> getExternalContext() {
		return externalContext;
	}

	/**
	 * For testing / debugging only.
	 * @return Some evaluation context.
	 */
	@Nonnull
	public static IEvaluationContext getNewBasicEvaluationContext() {
		final Builder b = new Builder();
		b.setScope(GenericScope.getNewEmptyScope());
		return b.build();
	}
}