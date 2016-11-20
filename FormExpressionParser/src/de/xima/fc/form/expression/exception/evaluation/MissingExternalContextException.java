package de.xima.fc.form.expression.exception.evaluation;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.util.CmnCnst;

public class MissingExternalContextException extends UncatchableEvaluationException {
	private static final long serialVersionUID = 1L;
	public MissingExternalContextException(@Nonnull final IEvaluationContext ec) {
		super(ec, CmnCnst.Error.MISSING_EXTERNAL_CONTEXT);
	}
}