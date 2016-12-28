package de.xima.fc.form.expression.impl.embedment.handler;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.impl.embedment.IEmbedmentHandlerNamed;
import de.xima.fc.form.expression.util.CmnCnst;

@NonNullByDefault
public enum EmbedmentHandlerBundleGeneral implements IEmbedmentHandlerNamed {
	SILENT(CmnCnst.CustomScope.GENERAL_NO_OUTPUT, false),
	VERBOSE(CmnCnst.CustomScope.GENERAL_YES_OUTPUT, true),
	;
	private final String name;
	private final boolean doOutput;
	private EmbedmentHandlerBundleGeneral(final String name, final boolean doOutput) {
		this.name = name;
		this.doOutput = doOutput;
	}

	@Override
	public String[] getScopeList() {
		return CmnCnst.NonnullConstant.EMPTY_STRING_ARRAY;
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