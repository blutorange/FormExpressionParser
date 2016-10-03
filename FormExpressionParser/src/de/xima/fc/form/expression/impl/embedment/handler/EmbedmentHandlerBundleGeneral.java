package de.xima.fc.form.expression.impl.embedment.handler;

import java.io.IOException;
import java.io.Writer;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.exception.EmbedmentOutputException;
import de.xima.fc.form.expression.impl.embedment.IEmbedmentHandlerNamed;

public enum EmbedmentHandlerBundleGeneral implements IEmbedmentHandlerNamed<Writer> {
	SILENT("[%%"),
	VERBOSE("[%%="),
	;

	private String name;
	private EmbedmentHandlerBundleGeneral(final String name) {
		this.name = name;
	}

	@Override
	public void beginEmbedment() {
	}

	@Override
	public void endEmbedment() {
	}

	@Override
	public void output(final String data, final Writer writer, final IEvaluationContext ec) throws EmbedmentOutputException {
		try {
			writer.write(data);
		}
		catch (final IOException e) {
			throw new EmbedmentOutputException(e, ec);
		}
	}

	@Override
	public String getEmbedmentName() {
		return name;
	}
}
