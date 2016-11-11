package de.xima.fc.form.expression.impl.embedment.handler;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.impl.embedment.IEmbedmentHandlerNamed;
import de.xima.fc.form.expression.util.CmnCnst;

public enum EmbedmentHandlerBundleGeneral implements IEmbedmentHandlerNamed {
	SILENT("[%%", false), //$NON-NLS-1$
	VERBOSE("[%%=", true), //$NON-NLS-1$
	;

	@Nonnull private final String name;
	private final boolean doOutput;
	private EmbedmentHandlerBundleGeneral(@Nonnull final String name, final boolean doOutput) {
		this.name = name;
		this.doOutput = doOutput;
	}

	@Override
	public String[] getScopeList() {
		return CmnCnst.EMPTY_STRING_ARRAY;
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
