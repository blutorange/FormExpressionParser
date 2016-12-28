package de.xima.fc.form.expression.exception.parse;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public class IllegalNumberOfFunctionParametersException extends SemanticsException {
	private static final long serialVersionUID = 1L;

	public IllegalNumberOfFunctionParametersException(final int shouldCount, final int isCount, final Node node) {
		super(NullUtil.messageFormat(CmnCnst.Error.ILLEGAL_ARGUMENT_COUNT, Integer.valueOf(shouldCount),
				Integer.valueOf(isCount)), node);
		this.shouldCount = shouldCount;
		this.isCount = isCount;
	}

	public final int shouldCount, isCount;
}