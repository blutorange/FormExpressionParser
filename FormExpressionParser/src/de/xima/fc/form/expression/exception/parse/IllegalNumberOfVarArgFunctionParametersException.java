package de.xima.fc.form.expression.exception.parse;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
public class IllegalNumberOfVarArgFunctionParametersException extends SemanticsException {
	private static final long serialVersionUID = 1L;

	public IllegalNumberOfVarArgFunctionParametersException(final int shouldCount, final int isCount, final Node node) {
		super(NullUtil.messageFormat(CmnCnst.Error.ILLEGAL_VARARG_ARGUMENT_COUNT, Integer.valueOf(shouldCount - 1),
				Integer.valueOf(isCount)), node);
		this.shouldCount = shouldCount;
		this.isCount = isCount;
	}

	public final int shouldCount, isCount;
}