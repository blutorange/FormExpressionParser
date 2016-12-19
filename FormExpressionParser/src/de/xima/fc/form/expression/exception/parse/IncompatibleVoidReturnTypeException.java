package de.xima.fc.form.expression.exception.parse;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public class IncompatibleVoidReturnTypeException extends IncompatibleVariableTypeException {
	private static final long serialVersionUID = 1L;
	public IncompatibleVoidReturnTypeException(final IVariableType actualReturnType, final Node node) {
		super(NullUtil.messageFormat(CmnCnst.Error.INCOMPATIBLE_VOID_RETURN_TYPE), null,
				actualReturnType, node);
	}
}