package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.context.IEvaluationContext;

public class OperationNotYetImplementedException extends UncatchableEvaluationException {

	public OperationNotYetImplementedException(final String name, final IEvaluationContext ec) {
		super(ec, String.format("Operation %s has not yet been implemented yet.", name));
		this.operationName = name;
	}

	public final String operationName;

}
