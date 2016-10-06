package de.xima.fc.form.expression.impl.embedment.handler;

import de.xima.fc.form.expression.impl.embedment.IEmbedmentHandlerNamed;
import de.xima.fc.form.expression.util.CmnCnst;

public enum EmbedmentHandlerBundleFormcycle implements IEmbedmentHandlerNamed {
	FORM_FIELD_SILENT(CmnCnst.CUSTOM_SCOPE_PREFIX_FORM_FIELD_SILENT, false, CmnCnst.CUSTOM_SCOPE_FORM_FIELD),
	SYSTEM_SILENT(CmnCnst.CUSTOM_SCOPE_PREFIX_FC_SYSTEM_SILENT, false, CmnCnst.CUSTOM_SCOPE_FC_SYSTEM),
	TEMPLATE_SILENT(CmnCnst.CUSTOM_SCOPE_PREFIX_TEMPLATE_SILENT, false, CmnCnst.CUSTOM_SCOPE_TEMPLATE),
	FORM_FIELD_VERBOSE(CmnCnst.CUSTOM_SCOPE_PREFIX_FORM_FIELD_VERBOSE, true, CmnCnst.CUSTOM_SCOPE_FORM_FIELD),
	SYSTEM_VERBOSE(CmnCnst.CUSTOM_SCOPE_PREFIX_FC_SYSTEM_VERBOSE, true, CmnCnst.CUSTOM_SCOPE_FC_SYSTEM),
	TEMPLATE_VERBOSE(CmnCnst.CUSTOM_SCOPE_PREFIX_TEMPLATE_VERBOSE, true, CmnCnst.CUSTOM_SCOPE_TEMPLATE),
	;

	private final String name;
	private final String[] scopeList;
	private final boolean doOutput;
	private EmbedmentHandlerBundleFormcycle(final String name, final boolean doOutput, final String... scopeList) {
		this.name = name;
		this.doOutput = doOutput;
		this.scopeList = scopeList;
	}

	@Override
	public String[] getScopeList() {
		return scopeList;
	}

	@Override
	public String getEmbedmentName() {
		return name;
	}

	@Override
	public boolean isDoOutput() {
		return doOutput;
	}
}
