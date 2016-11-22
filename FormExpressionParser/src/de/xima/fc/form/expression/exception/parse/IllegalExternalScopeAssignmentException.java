package de.xima.fc.form.expression.exception.parse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.node.ASTVariableNode;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class IllegalExternalScopeAssignmentException extends SemanticsException {
	private static final long serialVersionUID = 1L;
	public IllegalExternalScopeAssignmentException(@Nonnull final ASTVariableNode node) {
		super(NullUtil.format(CmnCnst.Error.ASSIGNMENT_OF_EXTERNALLY_SCOPED_VARIABLE, node.getScope(), node.getVariableName()), node);
		this.scope = node.getScope();
		this.name = node.getVariableName();
	}

	@Nullable
	public final String scope;
	@Nonnull
	public final String name;
}
