package de.xima.fc.form.expression.exception.parse;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class IncompatibleVariableTypeException extends SemanticsException {
	private static final long serialVersionUID = 1L;

	public IncompatibleVariableTypeException(@Nonnull final String message, @Nonnull final IVariableType shouldType,
			@Nonnull final IVariableType isType, @Nonnull final Node node) {
		super(NullUtil.messageFormat(CmnCnst.Error.INCOMPATIBLE_VARIABLE_TYPES, isType, shouldType, message), node);
		this.shouldType = shouldType;
		this.isType = isType;
	}

	@Nonnull
	public final IVariableType shouldType, isType;
}