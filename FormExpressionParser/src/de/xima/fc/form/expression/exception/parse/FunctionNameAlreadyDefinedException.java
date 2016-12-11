package de.xima.fc.form.expression.exception.parse;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.node.ASTFunctionClauseNode;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public class FunctionNameAlreadyDefinedException extends SemanticsException {
	private static final long serialVersionUID = 1L;
	public FunctionNameAlreadyDefinedException(final ASTFunctionClauseNode node) {
		super(NullUtil.messageFormat(CmnCnst.Error.FUNCTION_NAME_ALREADY_DEFINED, node.getCanonicalName()), node);
		this.functionName = node.getCanonicalName();
	}
	public final String functionName;
}