package de.xima.fc.form.expression.impl;

import de.xima.fc.form.expression.context.IBinding;
import de.xima.fc.form.expression.context.IEmbedment;
import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.ILogger;
import de.xima.fc.form.expression.context.INamespace;
import de.xima.fc.form.expression.context.IScope;
import de.xima.fc.form.expression.context.ITracer;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.impl.ReadScopedEvaluationContext.Builder;
import de.xima.fc.form.expression.impl.binding.OnDemandLookUpBinding;

public abstract class GenericEvaluationContext implements IEvaluationContext {

	private IBinding binding;
	private final INamespace namespace;
	private final ILogger logger;
	private final ITracer<Node> tracer;
	private final IScope scope;
	private final IEmbedment embedment;

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
	public GenericEvaluationContext(final IBinding binding, final IScope scope, final INamespace namespace, final ITracer<Node> tracer, final IEmbedment embedment, final ILogger logger) {
		this.binding = binding;
		this.namespace = namespace;
		this.scope = scope;
		this.tracer = tracer;
		this.logger = logger;
		this.embedment = embedment;
	}

	public void nestBinding() {
		setBinding(getBinding().nest(this));
	}

	public void unnestBinding() {
		setBinding(getBinding().unnest(this));
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
	public void setBinding(final IBinding binding) {
		this.binding = binding;
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
	public Void reset() {
		binding = binding.reset();
		scope.reset();
		embedment.reset();
		tracer.reset();
		return null;
	}
	
	/**
	 * To help you get started, use this method to acquire a new
	 * basic evaluation context. Details are subject to change,
	 * so this should only be used for testing purposes.
	 * @return Some evaluation context.
	 */
	public static IEvaluationContext getNewBasicEvaluationContext() {
		final Builder b = new Builder();
		b.setBinding(new OnDemandLookUpBinding());
		b.setScope(GenericScope.getNewEmptyScope());
		return b.build();
	}
}