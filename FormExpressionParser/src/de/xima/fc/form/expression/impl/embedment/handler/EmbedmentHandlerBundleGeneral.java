package de.xima.fc.form.expression.impl.embedment.handler;

import org.apache.commons.lang3.ArrayUtils;

import de.xima.fc.form.expression.impl.embedment.IEmbedmentHandlerNamed;

public enum EmbedmentHandlerBundleGeneral implements IEmbedmentHandlerNamed {
	SILENT("[%%", false),
	VERBOSE("[%%=", true),
	;

	private final String name;
	private final boolean doOutput;
	private EmbedmentHandlerBundleGeneral(final String name, final boolean doOutput) {
		this.name = name;
		this.doOutput = doOutput;
	}

	@Override
	public String[] getScopeList() {
		return ArrayUtils.EMPTY_STRING_ARRAY;
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
