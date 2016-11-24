package de.xima.fc.form.expression.exception.parse;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.node.ASTScopeManualNode;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class ManualScopeAlreadyRequiredException extends SemanticsException {
	private static final long serialVersionUID = 1L;

	public ManualScopeAlreadyRequiredException(@Nonnull final ASTScopeManualNode node) {
		super(NullUtil.format(CmnCnst.Error.MANUAL_SCOPE_ALREADY_REQUIRED, node.getScopeName()),
				node.getStartLine(), node.getStartColumn());
	}
}