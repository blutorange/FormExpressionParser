package de.xima.fc.form.expression.exception;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.node.ASTVariableDeclarationClauseNode;
import de.xima.fc.form.expression.util.NullUtil;

public class MultipleOccurencesOfScopedVariableException extends SemanticsException {
	private static final long serialVersionUID = 1L;

	public MultipleOccurencesOfScopedVariableException(@Nonnull final ASTVariableDeclarationClauseNode node,
			@Nullable final String scopeName) {
		super(NullUtil.format("Variable %s under scope %s was already declared previously.",
				scopeName == null ? "<GLOBAL>" : scopeName, node.getVariableName()), node.getStartLine(),
				node.getStartColumn());
		this.scopeName = scopeName;
	}

	public final String scopeName;
}
