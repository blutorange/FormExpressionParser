package de.xima.fc.form.expression.impl.embedment.handler;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.impl.embedment.IEmbedmentHandlerNamed;

public enum EmbedmentHandlerBundleFormcycle implements IEmbedmentHandlerNamed {
	FORM_FIELD_SILENT("[%@", false, "field"),
	SYSTEM_SILENT("[%$@", false, "fcsystem"),
	TEMPLATE_SILENT("[%$$@", false, "template"),
	FORM_FIELD_VERBOSE("[%", true, "field"),
	SYSTEM_VERBOSE("[%$", true, "fcsystem"),
	TEMPLATE_VERBOSE("[%$$", true, "template"),
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
	public void beginEmbedment(final IEvaluationContext ec) {
		// TODO Auto-generated method stub
		for (String scope : scopeList) ec.beginDefaultScope(scope);
	}

	@Override
	public void endEmbedment(final IEvaluationContext ec) {
		// TODO Auto-generated method stub
		for (int i = scopeList.length; i-->0;) ec.endDefaultScope();
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
