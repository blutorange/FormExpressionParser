package de.xima.fc.form.expression.impl.embedment;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.iface.evaluate.IEmbedment;
import de.xima.fc.form.expression.iface.factory.IEmbedmentContractFactory;
import de.xima.fc.form.expression.impl.embedment.handler.EmbedmentHandlerBundleFormcycle;
import de.xima.fc.form.expression.impl.embedment.handler.EmbedmentHandlerBundleGeneral;

public enum EEmbedmentContractFactory implements IEmbedmentContractFactory {
	/** Contains no scopes. */
	EMPTY(new GenericEmbedmentFactory.Builder().build()),
	GENERAL(new GenericEmbedmentFactory.Builder()
			.addHandler(EmbedmentHandlerBundleGeneral.values())
			.build()),
	FORMCYCLE_ALL(
			new GenericEmbedmentFactory.Builder()
			.addHandler(EmbedmentHandlerBundleGeneral.values())
			.addHandler(EmbedmentHandlerBundleFormcycle.values())
			.build()),
	FORMCYCLE_FORM_FIELD(
			new GenericEmbedmentFactory.Builder()
			.addHandler(EmbedmentHandlerBundleGeneral.values())
			.addHandler(EmbedmentHandlerBundleFormcycle.FORM_FIELD_SILENT)
			.addHandler(EmbedmentHandlerBundleFormcycle.FORM_FIELD_VERBOSE)
			.build()),
	FORMCYCLE_SYSTEM(
			new GenericEmbedmentFactory.Builder()
			.addHandler(EmbedmentHandlerBundleGeneral.values())
			.addHandler(EmbedmentHandlerBundleFormcycle.SYSTEM_SILENT)
			.addHandler(EmbedmentHandlerBundleFormcycle.SYSTEM_VERBOSE)
			.build()),
	FORMCYCLE_TEMPLATE(
			new GenericEmbedmentFactory.Builder()
			.addHandler(EmbedmentHandlerBundleGeneral.values())
			.addHandler(EmbedmentHandlerBundleFormcycle.TEMPLATE_SILENT)
			.addHandler(EmbedmentHandlerBundleFormcycle.TEMPLATE_VERBOSE)
			.build()),
	;
	@Nonnull
	private final IEmbedmentContractFactory impl;

	private EEmbedmentContractFactory(@Nonnull final IEmbedmentContractFactory impl) {
		this.impl = impl;
	}

	@Override
	public IEmbedment make() {
		return impl.make();
	}

	@Override
	public String[] getScopesForEmbedment(final String embedment) {
		return impl.getScopesForEmbedment(embedment);
	}
}