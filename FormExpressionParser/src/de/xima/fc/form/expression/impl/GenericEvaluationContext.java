package de.xima.fc.form.expression.impl;

import de.xima.fc.form.expression.context.IBinding;
import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.ILogger;
import de.xima.fc.form.expression.context.INamespace;
import de.xima.fc.form.expression.impl.logger.SystemLogger;
import de.xima.fc.form.expression.impl.logger.SystemLogger.Level;

public class GenericEvaluationContext implements IEvaluationContext {

	private IBinding binding;
	private final INamespace namespace;
	private final ILogger logger;
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
	public GenericEvaluationContext(final IBinding binding, final INamespace namespace, final ILogger logger,
			final int recursionLimit) {
		this.binding = binding;
		this.namespace = namespace;
		this.logger = logger;
		this.recursionLimit = recursionLimit;
	}

	/**
	 * A new evaluation context with the given binding and namespace, a
	 * {@link SystemLogger} at {@link Level#INFO} and a recursion limit of 10.
	 * 
	 * @param binding
	 *            Binding to use.
	 * @param namespace
	 *            Namespace to use.
	 */
	public GenericEvaluationContext(final IBinding binding, final INamespace namespace) {
		this(binding, namespace, SystemLogger.getInfoLogger(), 10);
	}

	/**
	 * A new evaluation context with the given binding and namespace, and a
	 * recursion limit of 10.
	 * 
	 * @param binding
	 *            Binding to use.
	 * @param namespace
	 *            Namespace to use.
	 * @param logger
	 *            The logger used for logging.
	 */
	public GenericEvaluationContext(final IBinding binding, final INamespace namespace, ILogger logger) {
		this(binding, namespace, logger, 10);
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
}
