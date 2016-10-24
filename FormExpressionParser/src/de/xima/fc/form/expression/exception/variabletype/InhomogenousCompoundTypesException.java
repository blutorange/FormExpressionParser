package de.xima.fc.form.expression.exception.variabletype;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.type.IVariableType;

public abstract class InhomogenousCompoundTypesException extends IllegalVariableTypeException {
	public final int index;
	public final String compoundName, elementName;
	public final Node compoundNode;
	public final IVariableType previousType, currentType;

	public InhomogenousCompoundTypesException(final int index, final IVariableType previousType,
			final IVariableType currentType, final String compoundName, final String elementName, final Node compoundNode) {
		super(index >= 0 && index < compoundNode.jjtGetNumChildren() ? compoundNode.jjtGetChild(index) : compoundNode,
				String.format(
						"Inhomogenous %s at line %d, column %d contains %s of type %s at index %d that is incompatible with a previous %s of type %s.",
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
