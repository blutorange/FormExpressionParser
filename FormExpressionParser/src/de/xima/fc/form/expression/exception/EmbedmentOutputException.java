package de.xima.fc.form.expression.exception;

import java.io.IOException;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IExternalContext;
import de.xima.fc.form.expression.util.CmnCnst;

public class EmbedmentOutputException extends CatchableEvaluationException {
	private static final long serialVersionUID = 1L;
	public EmbedmentOutputException(final IOException ioException, final IEvaluationContext ec) {
		super(ec, CmnCnst.Error.EMBEDMENT_OUPTUT, ioException);
		this.ioException = ioException;
	}
	public EmbedmentOutputException(final IOException ioException, final IExternalContext externalContext) {
		super(externalContext, CmnCnst.Error.EMBEDMENT_OUPTUT, ioException);
		this.ioException = ioException;
	}
	public final IOException ioException;
}