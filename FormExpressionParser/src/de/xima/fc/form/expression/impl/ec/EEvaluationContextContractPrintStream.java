package de.xima.fc.form.expression.impl.ec;

import java.io.PrintStream;

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
import de.xima.fc.form.expression.impl.externalcontext.ExternalContextContractFactoryPrintStream;
import de.xima.fc.form.expression.impl.logger.ELoggerContractFactory;

@NonNullByDefault
public enum EEvaluationContextContractPrintStream implements IEvaluationContextContract<PrintStream> {
	INSTANCE(new EvaluationContextContract.Builder<PrintStream>()
			.setEmbedment(EEmbedmentContractFactory.GENERAL)
			.setExternal(ExternalContextContractFactoryPrintStream.getInstance())
			.setLogger(ELoggerContractFactory.SYSTEM)
			.setTracing(true)
			.build()
			);
	private final IEvaluationContextContract<PrintStream> impl;
	private EEvaluationContextContractPrintStream(final IEvaluationContextContract<PrintStream> impl) {
		this.impl = impl;
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
	public ILoggerContractFactory getLoggerFactory() {
		return impl.getLoggerFactory();
	}
	@Override
	public INamespaceContractFactory getNamespaceFactory() {
		return impl.getNamespaceFactory();
	}
	@Override
	public ILibraryContractFactory getLibraryFactory() {
		return impl.getLibraryFactory();
	}
	@Override
	public IExternalContextContractFactory<PrintStream> getExternalFactory() {
		return impl.getExternalFactory();
	}
}