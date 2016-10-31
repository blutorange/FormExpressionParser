package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.util.CmnCnst;

public class ContinueClauseException extends UncatchableEvaluationException {
	private static final long serialVersionUID = 1L;
	public final String label;
	public ContinueClauseException(final String label, final IEvaluationContext ec) {
		super(ec, CmnCnst.Error.CONTINUE_CLAUSE);
		this.label = label;
	}
}
