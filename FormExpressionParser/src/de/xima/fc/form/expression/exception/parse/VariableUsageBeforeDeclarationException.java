package de.xima.fc.form.expression.exception.parse;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.node.ASTVariableNode;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public class VariableUsageBeforeDeclarationException extends SemanticsException {
	private static final long serialVersionUID = 1L;
	public VariableUsageBeforeDeclarationException(final ASTVariableNode node) {
		super(NullUtil.messageFormat(CmnCnst.Error.VARIABLE_USED_BEFORE_DECLARED, node.getVariableName()), node);
		this.variableName = node.getVariableName();
	}
	public final String variableName;
}