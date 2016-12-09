package de.xima.fc.form.expression.exception.parse;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.impl.variable.SimpleVariableType;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class IncompatibleBranchConditionTypeException extends IncompatibleVariableTypeException {
	private static final long serialVersionUID = 1L;

	public IncompatibleBranchConditionTypeException(@Nonnull final IVariableType typeCondition,
			@Nonnull final Node node) {
		super(NullUtil.messageFormat(CmnCnst.Error.BRANCH_CONDITION_MUST_BE_BOOLEAN),
				SimpleVariableType.BOOLEAN, typeCondition, node);
	}
}