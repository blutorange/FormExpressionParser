package de.xima.fc.form.expression.exception.evaluation;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
public class OperationNotYetImplementedException extends UncatchableEvaluationException {
	private static final long serialVersionUID = 1L;

	public OperationNotYetImplementedException(final String name, final IEvaluationContext ec) {
		super(ec, NullUtil.messageFormat(CmnCnst.Error.OPERATION_NOT_YET_IMPLEMENTED, name));
		this.operationName = name;
	}

	public final String operationName;
}