package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.util.CmnCnst;

public class BreakClauseException extends UncatchableEvaluationException {
	private static final long serialVersionUID = 1L;
	public final String label;
	public BreakClauseException(final String label, final IEvaluationContext ec) {
		super(ec, CmnCnst.Error.BREAK_CLAUSE);
		this.label = label;
	}
}
