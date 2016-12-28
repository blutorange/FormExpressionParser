package de.xima.fc.form.expression.impl.ec;

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
import de.xima.fc.form.expression.util.Void;

@NonNullByDefault
public enum EEvaluationContextContractVoid implements IEvaluationContextContract<Void> {
	DUMMY(new EvaluationContextContract.Builder<Void>()
			.setEmbedment(EEmbedmentContractFactory.EMPTY)
			.setExternal(ExternalContextContractFactoryDummy.<Void> getInstance())
			.setLibrary(ELibraryContractFactory.GENERIC)
			.setLogger(ELoggerContractFactory.DUMMY)
			.setTracing(false)
			.build()
			),
	GENERIC(new EvaluationContextContract.Builder<Void>()
			.setEmbedment(EEmbedmentContractFactory.GENERAL)
			.setExternal(ExternalContextContractFactoryDummy.<Void> getInstance())
			.setLibrary(ELibraryContractFactory.GENERIC)
			.setLogger(ELoggerContractFactory.SYSTEM)
			.setTracing(true)
			.build()
	)
	;
	private final IEvaluationContextContract<Void> impl;
	private EEvaluationContextContractVoid(final IEvaluationContextContract<Void> impl) {
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
	public IExternalContextContractFactory<Void> getExternalFactory() {
		return impl.getExternalFactory();
	}
}