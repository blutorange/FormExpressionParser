package de.xima.fc.form.expression.context;

import java.io.Writer;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.exception.EmbedmentOutputException;
import de.xima.fc.form.expression.impl.externalcontext.DummyWriter;
import de.xima.fc.form.expression.object.ALangObject;

public interface IExternalContext {
	/**
	 * @return A writer for writing output from template files. May not be null,
	 * but you may use a {@link DummyWriter}.
	 */
	@Nonnull
	public Writer getWriter();
		
	/**
	 * Flushes the writer, making sure all pending data is written.
	 * @throws EmbedmentOutputException When pending data cannot be written.
	 */
	public void flushWriter() throws EmbedmentOutputException;

	public ALangObject fetchScopedVariable(String scope, String name, IEvaluationContext ec);
}
