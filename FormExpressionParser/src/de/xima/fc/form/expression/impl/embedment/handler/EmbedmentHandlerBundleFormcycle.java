package de.xima.fc.form.expression.impl.embedment.handler;

import java.io.Writer;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.exception.EmbedmentOutputException;
import de.xima.fc.form.expression.impl.embedment.IEmbedmentHandlerNamed;

public enum EmbedmentHandlerBundleFormcycle implements IEmbedmentHandlerNamed<Writer> {
	FORM_FIELD_SILENT("[%@"),
	SYSTEM_SILENT("[%$@"),
	TEMPLATE_SILENT("[%$$@"),
	FORM_FIELD_VERBOSE("[%"),
	SYSTEM_VERBOSE("[%$"),
	TEMPLATE_VERBOSE("[%$$"),
	;

	private String name;
	private EmbedmentHandlerBundleFormcycle(final String name) {
		this.name = name;
	}

	@Override
	public void beginEmbedment() {
		// TODO Auto-generated method stub
		throw new RuntimeException("TODO - not yet implemented");
	}

	@Override
	public void endEmbedment() {
		// TODO Auto-generated method stub
		throw new RuntimeException("TODO - not yet implemented");
	}

	@Override
	public void output(final String data, final Writer writer, final IEvaluationContext ec) throws EmbedmentOutputException {
		// TODO Auto-generated method stub
		throw new RuntimeException("TODO - not yet implemented");
	}

	@Override
	public String getEmbedmentName() {
		return name;
	}

}
