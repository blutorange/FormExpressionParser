package de.xima.fc.form.expression.impl.ec;

import javax.annotation.Nullable;
import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.evaluate.ITracer;
import de.xima.fc.form.expression.iface.factory.IEmbedmentContractFactory;
import de.xima.fc.form.expression.iface.factory.IExternalContextContractFactory;
import de.xima.fc.form.expression.iface.factory.ILibraryContractFactory;
import de.xima.fc.form.expression.iface.factory.ILoggerContractFactory;
import de.xima.fc.form.expression.iface.factory.INamespaceContractFactory;
import de.xima.fc.form.expression.iface.parse.IEvaluationContextContract;
import de.xima.fc.form.expression.impl.embedment.EEmbedmentContractFactory;
import de.xima.fc.form.expression.impl.externalcontext.ExternalContextContractFactoryDummy;
import de.xima.fc.form.expression.impl.library.ELibraryContractFactory;
import de.xima.fc.form.expression.impl.logger.ELoggerContractFactory;
import de.xima.fc.form.expression.impl.namespace.ENamespaceContractFactory;
import de.xima.fc.form.expression.impl.tracer.DummyTracer;
import de.xima.fc.form.expression.impl.tracer.GenericTracer;

@NonNullByDefault
public class EvaluationContextContract<T> implements IEvaluationContextContract<T> {
	private static final long serialVersionUID = 1L;

	private final ILoggerContractFactory loggerFactory;
	private final IEmbedmentContractFactory embedmentFactory;
	private final ILibraryContractFactory libraryFactory;
	private final IExternalContextContractFactory<T> externalFactory;
	private final INamespaceContractFactory namespaceFactory;
	private final boolean tracingEnabled;

	public final static class Builder<S> {
		@Nullable
		private ILoggerContractFactory loggerFactory;
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

		public Builder<S> setLogger(final ILoggerContractFactory loggerFactory) {
			this.loggerFactory = loggerFactory;
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

		public IEvaluationContextContract<S> build() {
			// default library
			final ILibraryContractFactory lib = libraryFactory != null ? libraryFactory	: ELibraryContractFactory.GENERIC;
			// default logger
			final ILoggerContractFactory log = loggerFactory != null ? loggerFactory : ELoggerContractFactory.SYSTEM;
			// default embedment
			final IEmbedmentContractFactory emb = embedmentFactory != null ? embedmentFactory
					: EEmbedmentContractFactory.GENERAL;
			// default external context
			final IExternalContextContractFactory<S> exf = externalFactory != null ? externalFactory
					: ExternalContextContractFactoryDummy.<S> getInstance();
			// default namespace
			final INamespaceContractFactory nam = namespaceFactory != null ? namespaceFactory
					: ENamespaceContractFactory.GENERIC;
			// creating evaluation context factory
			final IEvaluationContextContract<S> res = new EvaluationContextContract<>(lib, emb, exf, nam, log,
					tracingEnabled);
			// reset builder
			loggerFactory = null;
			libraryFactory = null;
			embedmentFactory = null;
			externalFactory = null;
			namespaceFactory = null;
			return res;
		}
	}

	protected EvaluationContextContract(final ILibraryContractFactory libraryFactory,
			final IEmbedmentContractFactory embedmentFactory, final IExternalContextContractFactory<T> externalFactory,
			final INamespaceContractFactory namespaceFactory, final ILoggerContractFactory loggerFactory, final boolean tracingEnabled) {
		this.embedmentFactory = embedmentFactory;
		this.libraryFactory = libraryFactory;
		this.externalFactory = externalFactory;
		this.tracingEnabled = tracingEnabled;
		this.namespaceFactory = namespaceFactory;
		this.loggerFactory = loggerFactory;
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
	public ILoggerContractFactory getLoggerFactory() {
		return loggerFactory;
	}

	@Override
	public INamespaceContractFactory getNamespaceFactory() {
		return namespaceFactory;
	}
}