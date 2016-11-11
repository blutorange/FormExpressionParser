package de.xima.fc.form.expression.exception;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class OperationNotYetImplementedException extends UncatchableEvaluationException {
	private static final long serialVersionUID = 1L;

	public OperationNotYetImplementedException(@Nonnull final String name, @Nonnull final IEvaluationContext ec) {
		super(ec, NullUtil.format(CmnCnst.Error.OPERATION_NOT_YET_IMPLEMENTED, name));
		this.operationName = name;
	}

	public final String operationName;

}
