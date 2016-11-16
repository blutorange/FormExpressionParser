package de.xima.fc.form.expression.impl.externalcontext;

import java.io.IOException;
import java.io.Writer;

import de.xima.fc.form.expression.exception.EmbedmentOutputException;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.iface.context.IExternalContext;
import de.xima.fc.form.expression.iface.context.IExternalContextCommand;
import de.xima.fc.form.expression.impl.writer.StringBuilderWriter;
import de.xima.fc.form.expression.object.ALangObject;

public class WriterOnlyExternalContext implements IExternalContext {
	private Writer writer;

	public WriterOnlyExternalContext(final Writer writer) {
		this.writer = writer;
	}

	@Override
	public void write(String data) throws EmbedmentOutputException {
		try {
			writer.write(data);
		}
		catch (IOException e) {
			throw new EmbedmentOutputException(e, this);
		}
	}

	@Override
	public void finishWriting() throws EmbedmentOutputException {
		try {
			writer.flush();
		}
		catch (IOException e) {
			throw new EmbedmentOutputException(e, this);
		}
	}

	@Override
	public ALangObject fetchScopedVariable(String scope, String name, IEvaluationContext ec) {
		return null;
	}	
	
	/** @return The return value of the toString method of the writer. Useful for {@link StringBuilderWriter}. */
	@Override
	public String toString() {
		return writer.toString();
	}

	@Override
	public void process(IExternalContextCommand command, IEvaluationContext ec) {
	}

	@Override
	public void beginWriting() {
	}
}