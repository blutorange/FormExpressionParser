package de.xima.fc.form.expression.exception.parse;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.ISourceResolvable;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class VariableDeclaredTwiceException extends SemanticsException {
	private static final long serialVersionUID = 1L;
	public <T extends ISourceResolvable & Node> VariableDeclaredTwiceException(@Nonnull final T node) {
		super(NullUtil.messageFormat(CmnCnst.Error.VARIABLE_ALREADY_DECLARED_IN_NESTING_LEVEL, node.getVariableName()), node);
		name = node.getVariableName();
	}
	public final String name;
}
