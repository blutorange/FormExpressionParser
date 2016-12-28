package de.xima.fc.form.expression.exception.parse;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
public class IncompatibleSwitchCaseTypeException extends IncompatibleVariableTypeException {
	private static final long serialVersionUID = 1L;

	public IncompatibleSwitchCaseTypeException(final IVariableType typeSwitch, final IVariableType typeCase,
			final Node nodeCase) {
		super(NullUtil.messageFormat(CmnCnst.Error.INCOMPATIBLE_SWITCH_CASE_TYPE), typeSwitch, typeCase, nodeCase);
	}
}