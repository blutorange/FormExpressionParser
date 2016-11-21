package de.xima.fc.form.expression.exception.parse;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.node.ASTScopeExternalNode;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class DuplicateRequireScopeDeclarationException extends SemanticsException {
	private static final long serialVersionUID = 1L;
	public DuplicateRequireScopeDeclarationException(@Nonnull final ASTScopeExternalNode node) {
		super(NullUtil.format(CmnCnst.Error.DUPLICATE_REQUIRE_SCOPE, node.getScopeName()),
				node.getStartLine(), node.getStartColumn());
	}
}