package de.xima.fc.form.expression.impl;

import de.xima.fc.form.expression.context.IBinding;
import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.INamespace;

public class GenericEvaluationContext implements IEvaluationContext {

	private IBinding binding;
	private final INamespace namespace;

	public GenericEvaluationContext(final IBinding binding, final INamespace namespace) {
		this.binding = binding;
		this.namespace = namespace;
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
}
