package de.xima.fc.form.expression.exception.parse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.node.ASTVariableDeclarationClauseNode;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class DuplicateScopedVariableDeclarationException extends SemanticsException {
	private static final long serialVersionUID = 1L;

	public DuplicateScopedVariableDeclarationException(@Nonnull final ASTVariableDeclarationClauseNode node,
			@Nullable final String scopeName) {
		super(NullUtil.format(CmnCnst.Error.DUPLICATE_SCOPED_VARIABLE,
				node.getVariableName(), scopeName == null ? CmnCnst.Syntax.GLOBAL : scopeName), node.getStartLine(),
				node.getStartColumn());
		this.scopeName = scopeName;
	}

	public final String scopeName;
}
