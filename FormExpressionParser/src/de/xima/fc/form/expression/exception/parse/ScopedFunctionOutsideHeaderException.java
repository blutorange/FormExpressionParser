package de.xima.fc.form.expression.exception.parse;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.node.ASTFunctionClauseNode;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
public class ScopedFunctionOutsideHeaderException extends SemanticsException {
	private static final long serialVersionUID = 1L;
	public ScopedFunctionOutsideHeaderException(final ASTFunctionClauseNode node) {
		super(NullUtil.messageFormat(CmnCnst.Error.SCOPED_FUNCTION_OUTSIDE_HEADER, node.getScope(), node.getVariableName()), node);
		scope = NullUtil.orEmpty(node.getScope());
		variableName = node.getVariableName();
	}
	public final String scope;
	public final String variableName;
}