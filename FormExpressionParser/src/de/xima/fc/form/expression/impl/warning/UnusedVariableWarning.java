package de.xima.fc.form.expression.impl.warning;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.context.IEvaluationWarning;
import de.xima.fc.form.expression.iface.parse.ISourceResolvable;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class UnusedVariableWarning extends GenericWarning implements IEvaluationWarning {
	public UnusedVariableWarning(@Nonnull final ISourceResolvable resolvable, @Nonnull final Node node) {
		super(NullUtil.format(CmnCnst.Warning.UNUSED_VARIABLE, resolvable.getVariableName()), node);
		variableName = resolvable.getVariableName();
	}
	@Nonnull public final String variableName;
}