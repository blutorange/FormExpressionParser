package de.xima.fc.form.expression.exception.parse;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.node.ASTPropertyExpressionNode;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
public class NotAFunctionException extends IncompatibleVariableTypeException {
	private static final long serialVersionUID = 1L;
	public NotAFunctionException(final IVariableType typeThis, final ASTPropertyExpressionNode node) {
		super(NullUtil.messageFormat(CmnCnst.Error.NOT_A_FUNCTION), null, typeThis, node);
	}
}