package de.xima.fc.form.expression.exception.parse;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.node.ASTVariableNode;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public class IncompatibleVariableAssignmentTypeException extends IncompatibleVariableTypeException {
	private static final long serialVersionUID = 1L;
	public IncompatibleVariableAssignmentTypeException(final IVariableType lhs, final IVariableType rhs, final ASTVariableNode node) {
		super(NullUtil.messageFormat(CmnCnst.Error.INCOMPATIBLE_VARIABLE_ASSIGNMENT_TYPE, node.getVariableName()), lhs, rhs, node);
		this.variableName = node.getVariableName();
	}
	public final String variableName;
}