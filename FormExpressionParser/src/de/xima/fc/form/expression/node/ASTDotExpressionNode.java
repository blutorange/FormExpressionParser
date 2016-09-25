package de.xima.fc.form.expression.node;


import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public final class ASTDotExpressionNode extends ASTExpressionNode {
	private ASTVariableNode[] functionArray;

	public ASTDotExpressionNode(final int id) {
		super(id);
	}

	public Node getStartNode() {
		return children[0];
	}

	public ASTVariableNode[] getFunctionArray() {
		return functionArray;
	}

	@Override
	public void init(final EMethod method) throws ParseException {
		assertChildrenAtLeast(1);
		siblingMethod = method;
		functionArray = getChildArrayAs(ASTVariableNode.class, 1);
	}

	@Override
	public String toString() {
		return "DotExpressionNode(" + siblingMethod + ")";
	}

	@Override
	public <R, T> R jjtAccept(final IFormExpressionParserVisitor<R, T> visitor, final T data) throws EvaluationException {
		return visitor.visit(this, data);
	}

}
