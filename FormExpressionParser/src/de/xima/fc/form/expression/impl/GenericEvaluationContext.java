package de.xima.fc.form.expression.impl;

import de.xima.fc.form.expression.context.IBinding;
import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.ILogger;
import de.xima.fc.form.expression.context.INamespace;
import de.xima.fc.form.expression.context.IScope;
import de.xima.fc.form.expression.context.ITracer;
import de.xima.fc.form.expression.grammar.Node;

public abstract class GenericEvaluationContext implements IEvaluationContext {

	private IBinding binding;
	private final INamespace namespace;
	private final ILogger logger;
	final ITracer<Node> tracer;
	private final IScope scope;
	private final int recursionLimit;
	
	/**
	 * Creates a new evaluation context.
	 * 
	 * @param binding
	 *            Binding to use.
	 * @param namespace
	 *            Namespace to use.
	 * @param logger
	 *            The logger used for logging.
	 * @param recursionLimit
	 *            The limit for recursive method calls.
	 */
	public GenericEvaluationContext(final IBinding binding, final IScope scope, final INamespace namespace, final ITracer<Node> tracer, final ILogger logger,
			final int recursionLimit) {
		this.binding = binding;
		this.namespace = namespace;
		this.scope = scope;
		this.tracer = tracer;
		this.logger = logger;
		this.recursionLimit = recursionLimit;
	}

	public void nestBinding() {
		setBinding(getBinding().nest());
	}

	public void unnestBinding() {
		setBinding(getBinding().unnest());
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
	public boolean variableNameEquals(final String name1, final String name2) {
		return name1.equals(name2);
	}

	@Override
	public void setBinding(final IBinding binding) {
		this.binding = binding;
	}

	@Override
	public int getRecursionLimit() {
		return recursionLimit;
	}

	@Override
	public IScope getScope() {
		return scope;
	}
	
	@Override
	public ITracer<Node> getTracer() {
		return tracer;
	}
}