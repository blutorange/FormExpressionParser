package de.xima.fc.form.expression.impl.ec;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.evaluate.ITracer;
import de.xima.fc.form.expression.iface.factory.IEmbedmentContractFactory;
import de.xima.fc.form.expression.iface.factory.IExternalContextContractFactory;
import de.xima.fc.form.expression.iface.factory.ILibraryContractFactory;
import de.xima.fc.form.expression.iface.factory.ILoggerContractFactory;
import de.xima.fc.form.expression.iface.factory.INamespaceContractFactory;
import de.xima.fc.form.expression.iface.parse.IEvaluationContextContract;
import de.xima.fc.form.expression.impl.embedment.EEmbedmentContractFactory;
import de.xima.fc.form.expression.impl.externalcontext.EExternalContextContractFactoryFormcycle;
import de.xima.fc.form.expression.impl.externalcontext.Formcycle;
import de.xima.fc.form.expression.impl.library.ELibraryContractFactory;

@ParametersAreNonnullByDefault
public enum EEvaluationContextContractFormcycle implements IEvaluationContextContract<Formcycle> {
	INSTANCE(new EvaluationContextContract.Builder<Formcycle>()
			.setEmbedment(EEmbedmentContractFactory.FORMCYCLE)
			.setLibrary(ELibraryContractFactory.GENERIC)
			.setExternal(EExternalContextContractFactoryFormcycle.INSTANCE)
			.setTracing(true)
			.build()
			);
	private final IEvaluationContextContract<Formcycle> impl;
	private EEvaluationContextContractFormcycle(final IEvaluationContextContract<Formcycle> impl) {
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
	public IExternalContextContractFactory<Formcycle> getExternalFactory() {
		return impl.getExternalFactory();
	}
}