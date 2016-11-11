package de.xima.fc.form.expression.impl;

import javax.annotation.Nonnull;

import com.google.common.base.Optional;

import de.xima.fc.form.expression.context.IBinding;
import de.xima.fc.form.expression.context.IEmbedment;
import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IExternalContext;
import de.xima.fc.form.expression.context.ILogger;
import de.xima.fc.form.expression.context.INamespace;
import de.xima.fc.form.expression.context.IScope;
import de.xima.fc.form.expression.context.ITracer;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.impl.ReadScopedEvaluationContext.Builder;
import de.xima.fc.form.expression.impl.binding.OnDemandLookUpBinding;

public abstract class GenericEvaluationContext implements IEvaluationContext {

	@Nonnull protected final IBinding binding;
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
	public GenericEvaluationContext(@Nonnull final IBinding binding, @Nonnull final IScope scope,
			@Nonnull final INamespace namespace, @Nonnull final ITracer<Node> tracer,
			@Nonnull final IEmbedment embedment, @Nonnull final ILogger logger) {
		this.binding = binding;
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
	public IBinding getBinding() {
		return binding;
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
		binding.reset();
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
	 * To help you get started, use this method to acquire a new
	 * basic evaluation context. Details are subject to change,
	 * so this should only be used for testing purposes.
	 * @return Some evaluation context.
	 */
	@Nonnull
	public static IEvaluationContext getNewBasicEvaluationContext() {
		final Builder b = new Builder();
		b.setBinding(new OnDemandLookUpBinding());
		b.setScope(GenericScope.getNewEmptyScope());
		return b.build();
	}
}