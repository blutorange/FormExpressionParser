package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.util.CmnCnst;

public class OperationNotYetImplementedException extends UncatchableEvaluationException {
	private static final long serialVersionUID = 1L;

	public OperationNotYetImplementedException(final String name, final IEvaluationContext ec) {
		super(ec, String.format(CmnCnst.Error.OPERATION_NOT_YET_IMPLEMENTED, name));
		this.operationName = name;
	}

	public final String operationName;

}
