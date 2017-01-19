package de.xima.fc.form.expression.exception.parse;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
public class VariableReadBeforeAssignmentException extends SemanticsException {
	private static final long serialVersionUID = 1L;

	public VariableReadBeforeAssignmentException(final String variableName, final Node node) {
		super(NullUtil.messageFormat(CmnCnst.Error.VARIABLE_USED_BEFORE_ASSIGNMENT, variableName), node);
		this.variableName = variableName;
	}

	public final String variableName;
}