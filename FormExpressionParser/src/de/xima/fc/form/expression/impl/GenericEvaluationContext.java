package de.xima.fc.form.expression.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.evaluate.IEmbedment;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IExternalContext;
import de.xima.fc.form.expression.iface.evaluate.IExternalScope;
import de.xima.fc.form.expression.iface.evaluate.ILogger;
import de.xima.fc.form.expression.iface.evaluate.INamespace;
import de.xima.fc.form.expression.iface.evaluate.ITracer;
import de.xima.fc.form.expression.iface.parse.IVariableReference;
import de.xima.fc.form.expression.impl.logger.SystemLogger;
import de.xima.fc.form.expression.impl.tracer.DummyTracer;
import de.xima.fc.form.expression.util.CmnCnst;

public final class GenericEvaluationContext implements IEvaluationContext {

	@Nonnull private final INamespace namespace;
	@Nonnull private final ILogger logger;
	@Nonnull private final ITracer<Node> tracer;
	@Nonnull private final IExternalScope scope;
	@Nonnull private final IEmbedment embedment;
	@Nullable private IExternalContext externalContext;
	@Nonnull private IVariableReference[] symbolTable;

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
	public GenericEvaluationContext(@Nonnull final IExternalScope scope,
			@Nonnull final INamespace namespace, @Nonnull final ITracer<Node> tracer,
			@Nonnull final IEmbedment embedment, @Nonnull final ILogger logger) {
		this.namespace = namespace;
		this.scope = scope;
		this.tracer = tracer;
		this.logger = logger;
		this.embedment = embedment;
		this.symbolTable = CmnCnst.NonnullConstant.EMPTY_SYMBOL_TABLE;
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
	public IExternalScope getScope() {
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
		externalContext = null;
	}

	@Override
	public void setExternalContext(final IExternalContext externalContext) {
		this.externalContext = externalContext;
	}

	@Override
	public IExternalContext getExternalContext() {
		return externalContext;
	}

	/**
	 * For testing / debugging only.
	 * @return Some evaluation context.
	 */
	@Nonnull
	public static IEvaluationContext getNewBasicEvaluationContext() {
		final Builder b = new Builder();
		b.setScope(GenericExternalScope.getEmptyScopeInstance());
		return b.build();
	}

	public final static class Builder {
		private ILogger logger;
		private ITracer<Node> tracer;
		private INamespace namespace;
		private IExternalScope scope;
		private IEmbedment embedment;

		public Builder() {
			reinit();
		}

		private void reinit() {
			logger = null;
			tracer = null;
			namespace = null;
			scope = null;
			embedment = null;
		}

		@Nonnull
		public Builder setNamespace(@Nullable final INamespace namespace) {
			if (namespace != null)
				this.namespace = namespace;
			return this;
		}


		@Nonnull
		public Builder setScope(@Nullable final IExternalScope scope) {
			if (scope != null)
				this.scope = scope;
			return this;
		}

		@Nonnull
		public Builder setLogger(@Nullable final ILogger logger) {
			if (logger != null)
				this.logger = logger;
			return this;
		}

		@Nonnull
		public Builder setTracer(@Nullable final ITracer<Node> tracer) {
			if (tracer != null)
				this.tracer = tracer;
			return this;
		}

		@Nonnull
		public Builder setEmbedment(@Nullable final IEmbedment embedment) {
			if (embedment != null)
				this.embedment = embedment;
			return this;
		}

		@Nonnull
		public IEvaluationContext build() throws IllegalStateException {
			final IExternalScope scope = this.scope;
			ILogger logger = this.logger;
			IEmbedment embedment = this.embedment;
			ITracer<Node> tracer = this.tracer;
			INamespace namespace = this.namespace;
			if (scope == null) throw new IllegalStateException(CmnCnst.Error.ILLEGAL_STATE_EC_BUILDER_SCOPE);
			if (logger == null)	logger = SystemLogger.getInfoLogger();
			if (embedment == null) embedment = GenericEmbedment.getNewGeneralEmbedment();
			if (tracer == null) tracer = DummyTracer.INSTANCE;
			if (namespace == null) namespace = GenericNamespace.getGenericNamespaceInstance();
			final IEvaluationContext retVal = new GenericEvaluationContext(scope, namespace, tracer,	embedment, logger);
			reinit();
			return retVal;
		}
	}

	@Override
	public void createSymbolTable(int symbolTableSize) {
		symbolTableSize = symbolTableSize < 0 ? 0 : symbolTableSize;
		if (symbolTable.length != symbolTableSize)
			symbolTable = new IVariableReference[symbolTableSize];
		for (int i = symbolTableSize; i-- > 0;)
			symbolTable[i] = new GenericVariableReference();
	}

	@Override
	public IVariableReference[] getSymbolTable() {
		return symbolTable;
	}
}