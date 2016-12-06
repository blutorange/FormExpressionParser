package de.xima.fc.form.expression.exception.parse;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class IncompatibleHashEntryException extends IncompatibleVariableTypeException {
	private static final long serialVersionUID = 1L;
	public IncompatibleHashEntryException(final String details, final boolean isKey, @Nonnull final IVariableType shouldType, @Nonnull final IVariableType isType, @Nonnull final Node elementNode) {
		super(NullUtil.messageFormat(
				isKey ? CmnCnst.Error.INCOMPATIBLE_HASH_KEY_TYPE : CmnCnst.Error.INCOMPATIBLE_HASH_VALUE_TYPE, details),
				shouldType, isType, elementNode);
		this.isKey = isKey;
	}	
	public final boolean isKey;
}