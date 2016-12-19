package de.xima.fc.form.expression.exception.parse;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public class IllegalNumberOfVarArgFunctionParametersException extends SemanticsException {
	private static final long serialVersionUID = 1L;

	public IllegalNumberOfVarArgFunctionParametersException(final int shouldCount, final int isCount, final Node node) {
		super(NullUtil.messageFormat(CmnCnst.Error.ILLEGAL_VARARG_ARGUMENT_COUNT, shouldCount-1, isCount), node);
		this.shouldCount = shouldCount;
		this.isCount = isCount;
	}

	public final int shouldCount, isCount;
}