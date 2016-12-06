package de.xima.fc.form.expression.exception.parse;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class IncompatibleArrayItemException extends IncompatibleVariableTypeException {
	private static final long serialVersionUID = 1L;

	public IncompatibleArrayItemException(final String details, final int index,
			@Nonnull final IVariableType shouldType, @Nonnull final IVariableType isType,
			@Nonnull final Node elementNode) {
		super(NullUtil.messageFormat(CmnCnst.Error.INCOMPATIBLE_ARRAY_ITEM_TYPE, index, details), shouldType, isType,
				elementNode);
		this.index = index;
	}

	public final int index;
}