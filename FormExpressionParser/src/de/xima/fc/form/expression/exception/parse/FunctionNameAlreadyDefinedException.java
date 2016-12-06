package de.xima.fc.form.expression.exception.parse;

import de.xima.fc.form.expression.node.ASTFunctionClauseNode;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class FunctionNameAlreadyDefinedException extends SemanticsException {
	private static final long serialVersionUID = 1L;
	public FunctionNameAlreadyDefinedException(final ASTFunctionClauseNode node) {
		super(NullUtil.stringFormat(CmnCnst.Error.FUNCTION_NAME_ALREADY_DEFINED, node.getCanonicalName()), node);
		this.functionName = node.getCanonicalName();
	}
	public final String functionName;
}