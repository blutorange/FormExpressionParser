package de.xima.fc.form.expression.exception.parse;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.node.ASTFunctionClauseNode;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class ScopedFunctionOutsideHeaderException extends SemanticsException {
	private static final long serialVersionUID = 1L;
	public ScopedFunctionOutsideHeaderException(@Nonnull final ASTFunctionClauseNode node) {
		super(NullUtil.format(CmnCnst.Error.SCOPED_FUNCTION_OUTSIDE_HEADER, node.getScope(), node.getVariableName()), node);
		scope = NullUtil.orEmpty(node.getScope());
		variableName = node.getVariableName();
	}
	@Nonnull public final String scope;
	@Nonnull public final String variableName;
}
