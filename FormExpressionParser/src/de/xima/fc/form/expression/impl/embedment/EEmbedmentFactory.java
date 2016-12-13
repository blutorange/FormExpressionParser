package de.xima.fc.form.expression.impl.embedment;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.iface.evaluate.IEmbedment;
import de.xima.fc.form.expression.iface.evaluate.IEmbedmentContractFactory;
import de.xima.fc.form.expression.impl.embedment.handler.EmbedmentHandlerBundleFormcycle;
import de.xima.fc.form.expression.impl.embedment.handler.EmbedmentHandlerBundleGeneral;

public enum EEmbedmentFactory implements IEmbedmentContractFactory {
	GENERAL(new GenericEmbedmentFactory.Builder()
			.addHandler(EmbedmentHandlerBundleGeneral.values())
			.build()),
	FORMCYCLE(
			new GenericEmbedmentFactory.Builder()
			.addHandler(EmbedmentHandlerBundleGeneral.values())
			.addHandler(EmbedmentHandlerBundleFormcycle.values())
			.build()),
	;
	@Nonnull
	private final IEmbedmentContractFactory impl;

	private EEmbedmentFactory(@Nonnull final IEmbedmentContractFactory impl) {
		this.impl = impl;
	}

	@Override
	public IEmbedment makeEmbedment() {
		return impl.makeEmbedment();
	}

	@Override
	public String[] getScopesForEmbedment(final String embedment) {
		return impl.getScopesForEmbedment(embedment);
	}
}