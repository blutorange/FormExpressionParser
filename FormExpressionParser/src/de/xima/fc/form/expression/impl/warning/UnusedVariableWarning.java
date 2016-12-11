package de.xima.fc.form.expression.impl.warning;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.ISourceResolvable;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public class UnusedVariableWarning extends GenericWarning {
	public UnusedVariableWarning(final ISourceResolvable resolvable, final Node node) {
		super(NullUtil.messageFormat(CmnCnst.Warning.UNUSED_VARIABLE, resolvable.getVariableName()), node);
		variableName = resolvable.getVariableName();
	}

	public final String variableName;
}