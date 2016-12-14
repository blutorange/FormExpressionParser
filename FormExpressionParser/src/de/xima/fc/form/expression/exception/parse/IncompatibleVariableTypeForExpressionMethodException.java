package de.xima.fc.form.expression.exception.parse;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public class IncompatibleVariableTypeForExpressionMethodException extends SemanticsException {
	private static final long serialVersionUID = 1L;
	public IncompatibleVariableTypeForExpressionMethodException(final IVariableType lhs, final EMethod method, final IVariableType rhs, final Node node) {
		super(NullUtil.messageFormat(CmnCnst.Error.INCOMPATIBLE_EXPRESSION_METHOD_TYPES, method, lhs, rhs), node);
		this.lhs = lhs;
		this.rhs = rhs;
		this.method = method;
	}
	public final IVariableType lhs, rhs;
	public final EMethod method;
}