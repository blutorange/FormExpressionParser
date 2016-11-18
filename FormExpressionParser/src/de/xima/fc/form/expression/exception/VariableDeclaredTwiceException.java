package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.node.ASTVariableDeclarationClauseNode;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class VariableDeclaredTwiceException extends SemanticsException {
	private static final long serialVersionUID = 1L;
	public VariableDeclaredTwiceException(final ASTVariableDeclarationClauseNode node) {
		super(NullUtil.format(CmnCnst.Error.VARIABLE_ALREADY_DECLARED_IN_NESTING_LEVEL, node.getVariableName()), node);
		name = node.getVariableName();
	}
	public final String name;
}
