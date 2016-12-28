package de.xima.fc.form.expression.exception.parse;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
public class IncompatibleFunctionReturnTypeException extends IncompatibleVariableTypeException {
	private static final long serialVersionUID = 1L;

	public IncompatibleFunctionReturnTypeException(final IVariableType declaredReturnType, final IVariableType actualReturnType,
			final Node node) {
		super(NullUtil.messageFormat(CmnCnst.Error.INCOMPATIBLE_FUNCTION_RETURN_TYPE), declaredReturnType,
				actualReturnType, node);
	}
}