package de.xima.fc.form.expression.exception.variabletype;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.type.IVariableType;
import de.xima.fc.form.expression.util.CmnCnst;

public abstract class InhomogenousCompoundTypeException extends IllegalVariableTypeException {
	private static final long serialVersionUID = 1L;
	public final int index;
	public final String compoundName, elementName;
	public final Node compoundNode;
	public final IVariableType previousType, currentType;

	public InhomogenousCompoundTypeException(final int index, final IVariableType previousType,
			final IVariableType currentType, final String compoundName, final String elementName, final Node compoundNode) {
		super(index >= 0 && index < compoundNode.jjtGetNumChildren() ? compoundNode.jjtGetChild(index) : compoundNode,
				String.format(
						CmnCnst.Error.INHOMOGENOUS_COMPOUND_TYPE,
						compoundName, new Integer(compoundNode.getStartLine()), new Integer(compoundNode.getStartColumn()), elementName, currentType,
						new Integer(index), elementName, previousType));
		this.index = index;
		this.previousType = previousType;
		this.currentType = currentType;
		this.compoundNode = compoundNode;
		this.compoundName = compoundName;
		this.elementName = elementName;
	}
}
