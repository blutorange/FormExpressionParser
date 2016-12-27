package de.xima.fc.form.expression.exception.parse;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.iface.parse.IFunctionNode;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public class FunctionIdNotResolvedException extends SemanticsException {
	private static final long serialVersionUID = 1L;
	public FunctionIdNotResolvedException(final IFunctionNode node) {
		super(NullUtil.messageFormat(CmnCnst.Error.CALL_ID_NOT_RESOLVED, node), node);
	}
}