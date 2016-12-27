package de.xima.fc.form.expression.exception.evaluation;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.enums.EVariableSource;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public class IllegalVariableAssignmentException extends CatchableEvaluationException {
	private static final long serialVersionUID = 1L;

	public IllegalVariableAssignmentException(final EVariableSource sourceType, final String name, final IEvaluationContext ec) {
		super(ec, NullUtil.messageFormat(CmnCnst.Error.ASSIGNMENT_OF_UNASSIGNABLE_VARIABLE, name, sourceType));
		this.name = name;
		this.sourceType = sourceType;
	}

	public final String name;
	public final EVariableSource sourceType;
}