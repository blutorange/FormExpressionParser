package de.xima.fc.form.expression.exception.parse;

import javax.annotation.Nullable;
import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
public class IncompatibleVariableTypeException extends SemanticsException {
	private static final long serialVersionUID = 1L;

	public IncompatibleVariableTypeException(final String message, @Nullable final IVariableType shouldType,
			final IVariableType isType, final Node node) {
		super(message(message, shouldType, isType), node);
		this.shouldType = shouldType;
		this.isType = isType;
	}

	private static String message(final String message, @Nullable final IVariableType shouldType,
			final IVariableType isType) {
		if (shouldType != null)
			return NullUtil.messageFormat(CmnCnst.Error.INCOMPATIBLE_VARIABLE_TYPES_KNOWN_SHOULD, isType, shouldType,
					message);
		return NullUtil.messageFormat(CmnCnst.Error.INCOMPATIBLE_VARIABLE_TYPES_UNKNOWN_SHOULD, isType, message);
	}

	public final IVariableType isType;
	@Nullable
	public final IVariableType shouldType;
}