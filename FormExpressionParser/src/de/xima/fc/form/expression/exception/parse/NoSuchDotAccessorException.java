package de.xima.fc.form.expression.exception.parse;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public class NoSuchDotAccessorException extends SemanticsException {
	private static final long serialVersionUID = 1L;
	public NoSuchDotAccessorException(final IVariableType thisContext, final String property, final Node node) {
		super(NullUtil.messageFormat(CmnCnst.Error.NO_SUCH_DOT_ACCESSOR, property, thisContext), node);
		this.property = property;
	}
	public final String property;
}