package de.xima.fc.form.expression.exception.parse;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public class MissingReturnException extends SemanticsException {
	private static final long serialVersionUID = 1L;

	public MissingReturnException(final IVariableType declaredReturnType, final Node node) {
		super(NullUtil.messageFormat(CmnCnst.Error.MISSING_EXPLICIT_RETURN, declaredReturnType), node);
		this.declaredReturnType = declaredReturnType;
	}

	public final IVariableType declaredReturnType;
}