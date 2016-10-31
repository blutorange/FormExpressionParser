package de.xima.fc.form.expression.exception.variabletype;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.type.IVariableType;
import de.xima.fc.form.expression.util.CmnCnst;

public class IncompatbileStatementListReturnTypeException extends IllegalVariableTypeException {
	private static final long serialVersionUID = 1L;
	public final IVariableType previousType, currentType;
	public final int index;
	public IncompatbileStatementListReturnTypeException(final int index, final IVariableType previousType, final IVariableType currentType, final Node statementListNode) {
		super(index >= 0 && index < statementListNode.jjtGetNumChildren() ? statementListNode.jjtGetChild(index) : statementListNode,
				String.format(CmnCnst.Error.INCOMPATIBLE_STATEMENT_LIST_RETURN_TYPE,
						new Integer(index), previousType, currentType));
		this.previousType = previousType;
		this.currentType = currentType;
		this.index = index;
	}
}
