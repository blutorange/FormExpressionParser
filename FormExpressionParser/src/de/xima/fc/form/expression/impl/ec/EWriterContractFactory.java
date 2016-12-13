package de.xima.fc.form.expression.impl.ec;

import java.io.Writer;

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
import de.xima.fc.form.expression.impl.externalcontext.WriterOnlyExternalContextContractFactory;

@ParametersAreNonnullByDefault
public enum EWriterContractFactory implements IEvaluationContextContractFactory<Writer> {
	INSTANCE(EEmbedmentFactory.GENERAL, WriterOnlyExternalContextContractFactory.getInstance());
	private final IEvaluationContextContractFactory<Writer> impl;

	private EWriterContractFactory(final IEmbedmentContractFactory embedmentFactory,
			final IExternalContextContractFactory<Writer> ex) {
		this.impl = new GenericContractFactory<>(embedmentFactory, ex);
	}

	@Override
	public IEmbedmentContractFactory getEmbedmentFactory() {
		return impl.getEmbedmentFactory();
	}

	@Override
	public ITracer<Node> makeTracer() {
		return impl.makeTracer();
	}

	@Override
	public ILibraryContractFactory getLibraryFactory() {
		return impl.getLibraryFactory();
	}

	@Override
	public IExternalContextContractFactory<Writer> getExternalFactory() {
		return impl.getExternalFactory();
	}

	@Override
	public ILogger makeLogger() {
		return impl.makeLogger();
	}

	@Override
	public INamespaceContractFactory getNamespaceFactory() {
		return impl.getNamespaceFactory();
	}
}