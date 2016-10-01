package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTBreakClauseNode extends SimpleNode {
	private String label;
	
	public ASTBreakClauseNode(final int id) {
		super(id);
	}

	@Override
	public <R, T> R jjtAccept(final IFormExpressionParserVisitor<R, T> visitor, final T data) throws EvaluationException {
		return visitor.visit(this, data);
	}

	public void init(final EMethod method, final String label) throws ParseException {
		assertChildrenExactly(0);
		siblingMethod = method;
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
}
