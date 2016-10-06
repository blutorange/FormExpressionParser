package de.xima.fc.form.expression.exception;

import java.io.IOException;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IExternalContext;

public class EmbedmentOutputException extends CatchableEvaluationException {
	public EmbedmentOutputException(final IOException ioException, final IEvaluationContext ec) {
		super(ec, "Failed to write output due to an I/O exception.", ioException);
		this.ioException = ioException;
	}
	public EmbedmentOutputException(final IOException ioException, IExternalContext externalContext) {
		super(externalContext, "Failed to write output due to an I/O exception.", ioException);
		this.ioException = ioException;
	}
	public final IOException ioException;
}