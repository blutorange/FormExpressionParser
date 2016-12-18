package de.xima.fc.form.expression.exception.parse;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public class IncompatibleDotAssignerTypeException extends IncompatibleVariableTypeException {
	private static final long serialVersionUID = 1L;

	public IncompatibleDotAssignerTypeException(final IVariableType thisContext, final String property,
			final IVariableType value, final Node node) {
		super(NullUtil.messageFormat(CmnCnst.Error.NO_SUCH_DOT_ASSIGNER, property, value), null , thisContext, node);
		this.thisContext = thisContext;
		this.property = property;
		this.value = value;
	}
	public final IVariableType thisContext;
	public final String property;
	public final IVariableType value;
}