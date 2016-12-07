package de.xima.fc.form.expression.exception.parse;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.exception.IllegalVariableTypeException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class IncompatibleBranchSplitTypeException extends IncompatibleVariableTypeException {
	private static final long serialVersionUID = 1L;

	public IncompatibleBranchSplitTypeException(final IllegalVariableTypeException e, @Nonnull final IVariableType typeIf, @Nonnull final IVariableType typeElse, 
			@Nonnull final Node node) {
		super(NullUtil.messageFormat(CmnCnst.Error.BRANCH_SPLIT_TYPE_INCOMPATIBLE, e.getMessage()),
				typeIf, typeElse, node);
	}
}