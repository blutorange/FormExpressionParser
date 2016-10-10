package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTSwitchClauseNode extends SimpleNode {

	private String label;

	public ASTSwitchClauseNode(final int nodeId) {
		super(nodeId);
	}

	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionParserVisitor<R, T, E> visitor, final T data) throws E {
		return visitor.visit(this, data);
	}

	@Override
	protected void additionalToStringFields(final StringBuilder sb) {
		sb.append(label).append(',');
	}

	public void init(final EMethod method, final String label) throws ParseException {
		assertChildrenAtLeast(1);
		siblingMethod = method;
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
