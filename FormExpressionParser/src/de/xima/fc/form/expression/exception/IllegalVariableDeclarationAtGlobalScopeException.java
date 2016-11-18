package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.node.ASTVariableDeclarationClauseNode;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class IllegalVariableDeclarationAtGlobalScopeException extends SemanticsException {
	private static final long serialVersionUID = 1L;
	public IllegalVariableDeclarationAtGlobalScopeException(final ASTVariableDeclarationClauseNode node) {
		super(NullUtil.format(CmnCnst.Error.VARIABLE_DECLARATION_AT_GLOBAL_SCOPE, node.getVariableName()), node);
		name = node.getVariableName();
	}
	public final String name;
}
