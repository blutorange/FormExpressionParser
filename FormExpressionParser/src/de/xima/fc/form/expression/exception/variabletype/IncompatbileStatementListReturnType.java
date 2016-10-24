package de.xima.fc.form.expression.exception.variabletype;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.type.IVariableType;

public class IncompatbileStatementListReturnType extends IllegalVariableTypeException {
	public final IVariableType previousType, currentType;
	public final int index;
	public IncompatbileStatementListReturnType(final int index, final IVariableType previousType, final IVariableType currentType, final Node statementListNode) {
		super(index >= 0 && index < statementListNode.jjtGetNumChildren() ? statementListNode.jjtGetChild(index) : statementListNode,
				String.format("Statement at index %d can return type %s, but a previous statement can return type %s.",
						new Integer(index), previousType, currentType));
		this.previousType = previousType;
		this.currentType = currentType;
		this.index = index;
	}
}
