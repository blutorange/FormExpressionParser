package de.xima.fc.form.expression.exception.parse;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.node.ASTVariableDeclarationClauseNode;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public class DuplicateScopedVariableDeclarationException extends SemanticsException {
	private static final long serialVersionUID = 1L;

	public DuplicateScopedVariableDeclarationException(final ASTVariableDeclarationClauseNode node,
			@Nullable final String scopeName) {
		this(scopeName, node.getVariableName(), node);
	}

	public DuplicateScopedVariableDeclarationException(@Nullable final String scopeName, final String variableName,
			final Node node) {
		super(NullUtil.messageFormat(CmnCnst.Error.DUPLICATE_SCOPED_VARIABLE, variableName,
				scopeName == null ? CmnCnst.Syntax.GLOBAL : scopeName), node);
		this.scopeName = scopeName;
	}

	@Nullable
	public final String scopeName;
}