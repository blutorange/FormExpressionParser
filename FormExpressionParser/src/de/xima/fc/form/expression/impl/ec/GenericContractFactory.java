package de.xima.fc.form.expression.impl.ec;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.evaluate.IEmbedmentContractFactory;
import de.xima.fc.form.expression.iface.evaluate.IExternalContextContractFactory;
import de.xima.fc.form.expression.iface.evaluate.ILibraryContractFactory;
import de.xima.fc.form.expression.iface.evaluate.ILogger;
import de.xima.fc.form.expression.iface.evaluate.INamespaceContractFactory;
import de.xima.fc.form.expression.iface.evaluate.ITracer;
import de.xima.fc.form.expression.iface.parse.IEvaluationContextContractFactory;
import de.xima.fc.form.expression.impl.library.GenericLibraryContractFactory;
import de.xima.fc.form.expression.impl.library.SystemLibrary;
import de.xima.fc.form.expression.impl.logger.SystemLogger;
import de.xima.fc.form.expression.impl.namespace.DummyNamespaceContractFactory;
import de.xima.fc.form.expression.impl.tracer.DummyTracer;
import de.xima.fc.form.expression.impl.tracer.GenericTracer;

@ParametersAreNonnullByDefault
public class GenericContractFactory<T> implements IEvaluationContextContractFactory<T> {
	private static final long serialVersionUID = 1L;

	private final IEmbedmentContractFactory embedmentFactory;
	private final ILibraryContractFactory libraryFactory;
	private final IExternalContextContractFactory<T> externalFactory;
	private final boolean tracingEnabled;

	public GenericContractFactory(final IEmbedmentContractFactory embedmentFactory,
			final IExternalContextContractFactory<T> externalFactory) {
		this(embedmentFactory, externalFactory, true);
	}

	private GenericContractFactory(final IEmbedmentContractFactory embedmentFactory,
			final IExternalContextContractFactory<T> externalFactory, final boolean tracingEnabled) {
		this.embedmentFactory = embedmentFactory;
		this.libraryFactory = GenericLibraryContractFactory.createWith(SystemLibrary.MATH);
		this.externalFactory = externalFactory;
		this.tracingEnabled = tracingEnabled;
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
		return SystemLogger.getInfoLogger();
	}

	@Override
	public INamespaceContractFactory getNamespaceFactory() {
		return DummyNamespaceContractFactory.INSTANCE;
	}
}