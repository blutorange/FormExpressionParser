package de.xima.fc.form.expression.impl.embedment.handler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

import de.xima.fc.form.expression.impl.embedment.IEmbedmentHandlerNamed;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.CmnCnst.CustomScope;

public enum EmbedmentHandlerBundleFormcycle implements IEmbedmentHandlerNamed {
	FORM_FIELD_SILENT(CustomScope.PREFIX_FORM_FIELD_SILENT, false, CustomScope.FORM_FIELD),
	SYSTEM_SILENT(CustomScope.PREFIX_FC_SYSTEM_SILENT, false, CustomScope.FC_SYSTEM),
	TEMPLATE_SILENT(CustomScope.PREFIX_TEMPLATE_SILENT, false, CustomScope.TEMPLATE),
	FORM_FIELD_VERBOSE(CustomScope.PREFIX_FORM_FIELD_VERBOSE, true, CustomScope.FORM_FIELD),
	SYSTEM_VERBOSE(CustomScope.PREFIX_FC_SYSTEM_VERBOSE, true, CustomScope.FC_SYSTEM),
	TEMPLATE_VERBOSE(CustomScope.PREFIX_TEMPLATE_VERBOSE, true, CustomScope.TEMPLATE),
	;

	@Nonnull private final String name;
	@Nonnull private final String[] scopeList;
	private final boolean doOutput;
	private EmbedmentHandlerBundleFormcycle(@Nonnull final String name, final boolean doOutput, @Nullable final String... scopeList) {
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
