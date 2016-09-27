package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTFunctionNode extends SimpleNode {

	private String[] argNameArray;
	private Node evaluationNode;

	public ASTFunctionNode(final int nodeId) {
		super(nodeId);
	}

	public void init(final EMethod method) throws ParseException {
		assertChildrenAtLeast(1);
		siblingMethod = method;
		final ASTVariableNode[] childrenArray = getChildArrayAs(ASTVariableNode.class, 0, children.length-2);
		argNameArray = new String[childrenArray.length-1];
		for (int i = 0; i != argNameArray.length; ++i) {
			argNameArray[i] = childrenArray[i].getName();
		}
		evaluationNode = getLastChild();
	}

	@Override
	public <R, T> R jjtAccept(final IFormExpressionParserVisitor<R, T> visitor, final T data) throws EvaluationException {
		return visitor.visit(this, data);
	}

	public String[] getArgNameArray() {
		return argNameArray;
	}

	public Node getEvaluationNode() {
		return evaluationNode;
	}
}
