package de.xima.fc.form.expression.exception.parse;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.ISourceResolvable;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public class IncompatibleVariableAssignmentTypeException extends IncompatibleVariableTypeException {
	private static final long serialVersionUID = 1L;

	public <T extends ISourceResolvable & Node> IncompatibleVariableAssignmentTypeException(final IVariableType lhs,
			final IVariableType rhs, final T node) {
		this(lhs, rhs, node, node);
	}

	public IncompatibleVariableAssignmentTypeException(final IVariableType lhs, final IVariableType rhs,
			final ISourceResolvable resolvable, final Node node) {
		super(NullUtil.messageFormat(CmnCnst.Error.INCOMPATIBLE_VARIABLE_ASSIGNMENT_TYPE, resolvable.getVariableName()),
				lhs, rhs, node);
		this.variableName = resolvable.getVariableName();
	}

	public final String variableName;
}