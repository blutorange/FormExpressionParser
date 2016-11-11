package de.xima.fc.form.expression.exception;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.util.CmnCnst;

public class ReturnClauseException extends UncatchableEvaluationException {
	private static final long serialVersionUID = 1L;
	public ReturnClauseException(@Nonnull final IEvaluationContext ec) {
		super(ec, CmnCnst.Error.RETURN_CLAUSE);
	}
}
