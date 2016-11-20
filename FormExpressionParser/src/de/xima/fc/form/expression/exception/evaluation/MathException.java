package de.xima.fc.form.expression.exception.evaluation;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class MathException extends CatchableEvaluationException {
	private static final long serialVersionUID = 1L;

	public MathException(@Nonnull final String details, @Nonnull final IEvaluationContext ec)  {
		super(ec, NullUtil.format(CmnCnst.Error.MATH, details));
		this.details = details;
	}

	public final String details;
}
