package de.xima.fc.form.expression.exception.evaluation;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public class MathException extends CatchableEvaluationException {
	private static final long serialVersionUID = 1L;

	public MathException(final String details, final IEvaluationContext ec) {
		super(ec, NullUtil.messageFormat(CmnCnst.Error.MATH, details));
		this.details = details;
	}

	public final String details;
}