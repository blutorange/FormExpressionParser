package de.xima.fc.form.expression.impl.ec;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.evaluate.IEmbedmentContractFactory;
import de.xima.fc.form.expression.iface.evaluate.IExternalContextContractFactory;
import de.xima.fc.form.expression.iface.evaluate.ILibraryContractFactory;
import de.xima.fc.form.expression.iface.evaluate.ILogger;
import de.xima.fc.form.expression.iface.evaluate.INamespaceContractFactory;
import de.xima.fc.form.expression.iface.evaluate.ITracer;
import de.xima.fc.form.expression.iface.parse.IEvaluationContextContractFactory;
import de.xima.fc.form.expression.impl.embedment.EEmbedmentFactory;
import de.xima.fc.form.expression.impl.externalcontext.DummyExternalContextFactory;
import de.xima.fc.form.expression.impl.library.GenericLibraryContractFactory;
import de.xima.fc.form.expression.impl.library.LibraryScopeSystem;
import de.xima.fc.form.expression.impl.logger.SystemLogger;
import de.xima.fc.form.expression.impl.namespace.DummyNamespaceContractFactory;
import de.xima.fc.form.expression.impl.tracer.DummyTracer;
import de.xima.fc.form.expression.impl.tracer.GenericTracer;

@ParametersAreNonnullByDefault
public class GenericContractFactory<T> implements IEvaluationContextContractFactory<T> {
	private static final long serialVersionUID = 1L;

	private final ILogger logger;
	private final IEmbedmentContractFactory embedmentFactory;
	private final ILibraryContractFactory libraryFactory;
	private final IExternalContextContractFactory<T> externalFactory;
	private final INamespaceContractFactory namespaceFactory;
	private final boolean tracingEnabled;

	public final static class Builder<S> {
		@Nullable
		private ILogger logger;
		@Nullable
		private IEmbedmentContractFactory embedmentFactory;
		@Nullable
		private ILibraryContractFactory libraryFactory;
		@Nullable
		private INamespaceContractFactory namespaceFactory;
		@Nullable
		private IExternalContextContractFactory<S> externalFactory;
		private boolean tracingEnabled = true;

		public Builder() {
		}

		public Builder<S> setLibrary(final ILibraryContractFactory libraryFactory) {
			this.libraryFactory = libraryFactory;
			return this;
		}

		public Builder<S> setEmbedment(final IEmbedmentContractFactory embedmentFactory) {
			this.embedmentFactory = embedmentFactory;
			return this;
		}

		public Builder<S> setLogger(final ILogger logger) {
			this.logger = logger;
			return this;
		}

		public Builder<S> setExternal(final IExternalContextContractFactory<S> externalFactory) {
			this.externalFactory = externalFactory;
			return this;
		}

		public Builder<S> setTracing(final boolean tracingEnabled) {
			this.tracingEnabled = tracingEnabled;
			return this;
		}

		public IEvaluationContextContractFactory<S> build() {
			// default library
			final ILibraryContractFactory lib = libraryFactory != null ? libraryFactory
					: GenericLibraryContractFactory.createWith(LibraryScopeSystem.values());
			// default logger
			final ILogger log = logger != null ? logger : SystemLogger.getInfoLogger();
			// default embedment
			final IEmbedmentContractFactory emb = embedmentFactory != null ? embedmentFactory
					: EEmbedmentFactory.GENERAL;
			// default external context
			final IExternalContextContractFactory<S> exf = externalFactory != null ? externalFactory
					: DummyExternalContextFactory.<S> getFactory();
			// default namespace
			final INamespaceContractFactory nam = namespaceFactory != null ? namespaceFactory
					: DummyNamespaceContractFactory.INSTANCE;
			// creating evaluation context factory
			final IEvaluationContextContractFactory<S> res = new GenericContractFactory<>(lib, emb, exf, nam, log,
					tracingEnabled);
			// reset builder
			logger = null;
			libraryFactory = null;
			embedmentFactory = null;
			externalFactory = null;
			namespaceFactory = null;
			return res;
		}
	}

	protected GenericContractFactory(final ILibraryContractFactory libraryFactory,
			final IEmbedmentContractFactory embedmentFactory, final IExternalContextContractFactory<T> externalFactory,
			final INamespaceContractFactory namespaceFactory, final ILogger logger, final boolean tracingEnabled) {
		this.embedmentFactory = embedmentFactory;
		this.libraryFactory = libraryFactory;
		this.externalFactory = externalFactory;
		this.tracingEnabled = tracingEnabled;
		this.namespaceFactory = namespaceFactory;
		this.logger = logger;
	}

	@Override
	public ILibraryContractFactory getLibraryFactory() {
		return libraryFactory;
	}

	@Override
	public ITracer<Node> makeTracer() {
		return tracingEnabled ? new GenericTracer() : DummyTracer.INSTANCE;
	}

	@Override
	public IExternalContextContractFactory<T> getExternalFactory() {
		return externalFactory;
	}

	@Override
	public IEmbedmentContractFactory getEmbedmentFactory() {
		return embedmentFactory;
	}

	@Override
	public ILogger makeLogger() {
		return logger;
	}

	@Override
	public INamespaceContractFactory getNamespaceFactory() {
		return namespaceFactory;
	}
}