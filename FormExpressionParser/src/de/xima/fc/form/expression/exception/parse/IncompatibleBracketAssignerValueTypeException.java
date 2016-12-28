package de.xima.fc.form.expression.exception.parse;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
public class IncompatibleBracketAssignerValueTypeException extends IncompatibleVariableTypeException {
	private static final long serialVersionUID = 1L;

	public IncompatibleBracketAssignerValueTypeException(final IVariableType thisContext, final IVariableType shouldValue,
			final IVariableType isValue, final Node node) {
		super(NullUtil.messageFormat(CmnCnst.Error.INCOMPATIBLE_BRACKET_ASSIGNER_VALUE_TYPES, thisContext), shouldValue,
				isValue, node);
		this.thisContext = thisContext;
	}

	public final IVariableType thisContext;
}
