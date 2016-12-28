package de.xima.fc.form.expression.exception.parse;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
public class NoSuchExpressionMethodException extends SemanticsException {
	private static final long serialVersionUID = 1L;
	public NoSuchExpressionMethodException(final IVariableType thisContext, final EMethod method, final Node node) {
		super(NullUtil.messageFormat(CmnCnst.Error.NO_SUCH_EXPRESSION_METHOD, method, thisContext), node);
		this.method = method;
	}
	public final EMethod method;
}