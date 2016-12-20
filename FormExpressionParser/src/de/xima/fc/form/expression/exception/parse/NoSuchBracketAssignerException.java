package de.xima.fc.form.expression.exception.parse;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public class NoSuchBracketAssignerException extends SemanticsException {
	private static final long serialVersionUID = 1L;
	public NoSuchBracketAssignerException(final IVariableType thisContext, final Node node) {
		super(NullUtil.messageFormat(CmnCnst.Error.NO_SUCH_BRACKET_ACCESSOR, thisContext), node);
	}
}