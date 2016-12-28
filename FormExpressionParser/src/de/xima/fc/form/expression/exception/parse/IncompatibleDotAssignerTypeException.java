package de.xima.fc.form.expression.exception.parse;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
public class IncompatibleDotAssignerTypeException extends IncompatibleVariableTypeException {
	private static final long serialVersionUID = 1L;

	public IncompatibleDotAssignerTypeException(final IVariableType thisContext, final String property,
			final IVariableType shouldType, final IVariableType isType, final Node node) {
		super(NullUtil.messageFormat(CmnCnst.Error.INCOMPATIBLE_DOT_ASSIGNER_TYPES, property, thisContext), shouldType,
				isType, node);
		this.thisContext = thisContext;
		this.property = property;
	}

	public final IVariableType thisContext;
	public final String property;
}