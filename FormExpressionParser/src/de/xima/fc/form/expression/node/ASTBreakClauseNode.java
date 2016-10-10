package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTBreakClauseNode extends SimpleNode {
	private String label;

	public ASTBreakClauseNode(final int id) {
		super(id);
	}

	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionParserVisitor<R, T, E> visitor, final T data) throws E {
		return visitor.visit(this, data);
	}

	public void init(final EMethod method, final String label) throws ParseException {
		assertChildrenExactly(0);
		siblingMethod = method;
		this.label = label;
	}

	@Override
	protected void additionalToStringFields(final StringBuilder sb) {
		sb.append(label).append(',');
	}


	public String getLabel() {
		return label;
	}
}
