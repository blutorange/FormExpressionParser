package de.xima.fc.form.expression.exception.evaluation;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
public class MathException extends CatchableEvaluationException {
	private static final long serialVersionUID = 1L;

	public MathException(final String details, final IEvaluationContext ec) {
		super(ec, NullUtil.messageFormat(CmnCnst.Error.MATH, details));
		this.details = details;
	}

	public final String details;
}