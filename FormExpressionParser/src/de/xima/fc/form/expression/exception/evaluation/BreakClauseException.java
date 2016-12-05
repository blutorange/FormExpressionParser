package de.xima.fc.form.expression.exception.evaluation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class BreakClauseException extends UncatchableEvaluationException {
	private static final long serialVersionUID = 1L;
	@Nullable public final String label;
	public BreakClauseException(@Nullable final String label, @Nonnull final IEvaluationContext ec) {
		super(ec, NullUtil.format(CmnCnst.Error.BREAK_CLAUSE, label));
		this.label = label;
	}
}
