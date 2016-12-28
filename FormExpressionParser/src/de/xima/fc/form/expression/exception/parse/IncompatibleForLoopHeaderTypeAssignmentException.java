package de.xima.fc.form.expression.exception.parse;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
public class IncompatibleForLoopHeaderTypeAssignmentException extends IncompatibleVariableTypeException {
	private static final long serialVersionUID = 1L;

	public IncompatibleForLoopHeaderTypeAssignmentException(final IVariableType isTypeVariable,
			final IVariableType shouldTypeIterator, final Node nodeIterator) {
		super(NullUtil.messageFormat(CmnCnst.Error.INCOMPATIBLE_TYPE_IN_FOR_HEADER), shouldTypeIterator, isTypeVariable,
				nodeIterator);
	}
}