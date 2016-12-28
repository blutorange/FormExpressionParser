package de.xima.fc.form.expression.exception.parse;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.node.ASTScopeManualNode;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
public class ManualScopeAlreadyRequiredException extends SemanticsException {
	private static final long serialVersionUID = 1L;

	public ManualScopeAlreadyRequiredException(final ASTScopeManualNode node) {
		super(NullUtil.messageFormat(CmnCnst.Error.MANUAL_SCOPE_ALREADY_REQUIRED, node.getScopeName()),	node);
	}
}