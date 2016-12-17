package de.xima.fc.form.expression.exception.parse;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public class IncompatibleDotAccessorTypeException extends IncompatibleVariableTypeException {
	private static final long serialVersionUID = 1L;
	public IncompatibleDotAccessorTypeException(final IVariableType thisContext, final String property, final Node node) {
		super(NullUtil.messageFormat(CmnCnst.Error.NO_SUCH_DOT_ACCESSOR, property), null , thisContext, node);
		this.thisContext = thisContext;
		this.property = property;
	}
	public final IVariableType thisContext;
	public final String property;
}