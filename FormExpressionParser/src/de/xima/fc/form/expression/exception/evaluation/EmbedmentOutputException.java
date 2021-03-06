package de.xima.fc.form.expression.exception.evaluation;

import java.io.IOException;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IExternalContext;
import de.xima.fc.form.expression.util.CmnCnst;

public class EmbedmentOutputException extends CatchableEvaluationException {
	private static final long serialVersionUID = 1L;
	public EmbedmentOutputException(@Nonnull final IOException ioException, @Nonnull final IEvaluationContext ec) {
		super(ec, CmnCnst.Error.EMBEDMENT_OUPTUT, ioException);
		this.ioException = ioException;
	}
	public EmbedmentOutputException(@Nonnull final IOException ioException, @Nonnull final IExternalContext externalContext) {
		super(externalContext, CmnCnst.Error.EMBEDMENT_OUPTUT, ioException);
		this.ioException = ioException;
	}
	public final IOException ioException;
}