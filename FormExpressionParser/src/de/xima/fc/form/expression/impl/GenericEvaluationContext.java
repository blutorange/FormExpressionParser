package de.xima.fc.form.expression.impl;

import de.xima.fc.form.expression.context.IBinding;
import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.INamespace;

public class GenericEvaluationContext implements IEvaluationContext {

	private IBinding binding;
	private final INamespace namespace;
	private final int recursionLimit;

	/**
	 * Creates a new evaluation context.
	 * @param binding Binding to use.
	 * @param namespace Namespace to use.
	 * @param recursionLimit The limit for recursive method calls.
	 */
	public GenericEvaluationContext(final IBinding binding, final INamespace namespace, final int recursionLimit) {
		this.binding = binding;
		this.namespace = namespace;
		this.recursionLimit = recursionLimit;
	}

	/**
	 * A new evaluation context with the given binding and namespace, and a recursion limit of 10.
	 * @param binding Binding to use.
	 * @param namespace Namespace to use.
	 */
	public GenericEvaluationContext(final IBinding binding, final INamespace namespace) {
		this(binding, namespace, 10);
	}

	@Override
	public void nestBinding() {
		setBinding(getBinding().nest());
	}

	@Override
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
	public boolean variableNameEquals(final String name1, final String name2) {
		return name1.equals(name2);
	}

	private void setBinding(final IBinding binding) {
		this.binding = binding;
	}

	@Override
	public int getRecursionLimit() {
		return recursionLimit;
	}
}
