package de.xima.fc.form.expression.exception.parse;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public class IterationNotSupportedException extends SemanticsException {
	private static final long serialVersionUID = 1L;

	public IterationNotSupportedException(final IVariableType type, final Node node) {
		super(NullUtil.messageFormat(CmnCnst.Error.TYPE_NOT_ITERABLE, type), node);
		this.type = type;
	}

	public final IVariableType type;
}