package de.xima.fc.form.expression.exception.parse;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
public class NoSuchBracketAccessorException extends SemanticsException {
	private static final long serialVersionUID = 1L;
	public NoSuchBracketAccessorException(final IVariableType thisContext, final Node node) {
		super(NullUtil.messageFormat(CmnCnst.Error.NO_SUCH_BRACKET_ACCESSOR, thisContext), node);
	}
}