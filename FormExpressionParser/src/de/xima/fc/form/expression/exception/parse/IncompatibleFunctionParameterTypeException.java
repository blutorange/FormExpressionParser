package de.xima.fc.form.expression.exception.parse;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
public class IncompatibleFunctionParameterTypeException extends IncompatibleVariableTypeException {
	private static final long serialVersionUID = 1L;

	public IncompatibleFunctionParameterTypeException(final IVariableType shouldType, final IVariableType isType,
			final Node node) {
		super(NullUtil.messageFormat(CmnCnst.Error.INCOMPATIBLE_FUNCTION_PARAMETER_TYPE), shouldType, isType, node);
	}
}