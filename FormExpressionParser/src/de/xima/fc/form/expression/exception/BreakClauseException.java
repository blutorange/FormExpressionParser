package de.xima.fc.form.expression.exception;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.util.CmnCnst;

public class BreakClauseException extends UncatchableEvaluationException {
	private static final long serialVersionUID = 1L;
	@Nullable public final String label;
	public BreakClauseException(@Nullable final String label, @Nonnull final IEvaluationContext ec) {
		super(ec, CmnCnst.Error.BREAK_CLAUSE);
		this.label = label;
	}
}
