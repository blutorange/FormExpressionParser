package de.xima.fc.form.expression.impl.embedment.handler;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.google.common.base.Preconditions.checkNotNull;

import de.xima.fc.form.expression.impl.embedment.IEmbedmentHandlerNamed;
import de.xima.fc.form.expression.util.CmnCnst;

@ParametersAreNonnullByDefault
public enum EmbedmentHandlerBundleFormcycle implements IEmbedmentHandlerNamed {
	FORM_FIELD_SILENT(CmnCnst.CustomScope.PREFIX_FORM_FIELD_SILENT, false, CmnCnst.CustomScope.FORM_FIELD),
	SYSTEM_SILENT(CmnCnst.CustomScope.PREFIX_FC_SYSTEM_SILENT, false, CmnCnst.CustomScope.FC_SYSTEM),
	TEMPLATE_SILENT(CmnCnst.CustomScope.PREFIX_TEMPLATE_SILENT, false, CmnCnst.CustomScope.TEMPLATE),
	FORM_FIELD_VERBOSE(CmnCnst.CustomScope.PREFIX_FORM_FIELD_VERBOSE, true, CmnCnst.CustomScope.FORM_FIELD),
	SYSTEM_VERBOSE(CmnCnst.CustomScope.PREFIX_FC_SYSTEM_VERBOSE, true, CmnCnst.CustomScope.FC_SYSTEM),
	TEMPLATE_VERBOSE(CmnCnst.CustomScope.PREFIX_TEMPLATE_VERBOSE, true, CmnCnst.CustomScope.TEMPLATE),
	;

	private final String name;
	private final String[] scopeList;
	private final boolean doOutput;
	private EmbedmentHandlerBundleFormcycle(final String name, final boolean doOutput, @Nullable final String... scopeList) {
		checkNotNull(name);
		this.name = name;
		this.doOutput = doOutput;
		this.scopeList = scopeList == null ? CmnCnst.NonnullConstant.EMPTY_STRING_ARRAY : scopeList;
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