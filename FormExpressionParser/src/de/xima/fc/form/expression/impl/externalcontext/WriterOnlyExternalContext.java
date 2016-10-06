package de.xima.fc.form.expression.impl.externalcontext;

import java.io.IOException;
import java.io.Writer;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IExternalContext;
import de.xima.fc.form.expression.exception.EmbedmentOutputException;
import de.xima.fc.form.expression.object.ALangObject;

public class WriterOnlyExternalContext implements IExternalContext {
	private Writer writer;

	public WriterOnlyExternalContext(final Writer writer) {
		this.writer = writer;
	}

	@Override
	public Writer getWriter() {
		return writer;
	}

	@Override
	public void flushWriter() throws EmbedmentOutputException {
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
}