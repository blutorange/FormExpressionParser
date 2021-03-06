package de.xima.fc.form.expression.exception.parse;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
public class IncompatibleBracketAssignerPropertyTypeException extends IncompatibleVariableTypeException {
	private static final long serialVersionUID = 1L;

	public IncompatibleBracketAssignerPropertyTypeException(final IVariableType thisContext, final IVariableType shouldProperty,
			final IVariableType isProperty, final Node node) {
		super(NullUtil.messageFormat(CmnCnst.Error.INCOMPATIBLE_BRACKET_ASSIGNER_PROPERTY_TYPES, thisContext), shouldProperty,
				isProperty, node);
		this.thisContext = thisContext;
	}

	public final IVariableType thisContext;
}
