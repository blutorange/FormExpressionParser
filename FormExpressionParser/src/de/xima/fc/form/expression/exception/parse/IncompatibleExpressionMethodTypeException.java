package de.xima.fc.form.expression.exception.parse;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public class IncompatibleExpressionMethodTypeException extends IncompatibleVariableTypeException {
	private static final long serialVersionUID = 1L;

	public IncompatibleExpressionMethodTypeException(final IVariableType thisContext, final EMethod method,
			final IVariableType shouldType, final IVariableType isType, final Node node) {
		super(NullUtil.messageFormat(CmnCnst.Error.INCOMPATIBLE_EXPRESSION_METHOD_TYPES, method, thisContext), shouldType, isType, node);
		this.thisContext = thisContext;
		this.method = method;
	}

	public final IVariableType thisContext;
	public final EMethod method;
}