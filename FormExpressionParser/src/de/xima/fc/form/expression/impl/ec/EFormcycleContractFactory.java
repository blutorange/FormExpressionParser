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
import de.xima.fc.form.expression.impl.embedment.EEmbedmentFactory;
import de.xima.fc.form.expression.impl.externalcontext.Formcycle;
import de.xima.fc.form.expression.impl.externalcontext.FormcycleExternalContextContractFactory;

@ParametersAreNonnullByDefault
public enum EFormcycleContractFactory implements IEvaluationContextContractFactory<Formcycle> {
	GENERAL(EEmbedmentFactory.FORMCYCLE, FormcycleExternalContextContractFactory.INSTANCE),
	;
	private final IEvaluationContextContractFactory<Formcycle> impl;
	private EFormcycleContractFactory(final IEmbedmentContractFactory embedmentFactory, final IExternalContextContractFactory<Formcycle> ex) {
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
	public IExternalContextContractFactory<Formcycle> getExternalFactory() {
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