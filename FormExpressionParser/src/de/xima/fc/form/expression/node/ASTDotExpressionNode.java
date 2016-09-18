package de.xima.fc.form.expression.node;


import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;

public final class ASTDotExpressionNode extends ASTExpressionNode {
	private Node child;
	private AFunctionCallNode[] functionArray;

	public ASTDotExpressionNode(final int id) {
		super(id);
	}

	public ASTDotExpressionNode(final FormExpressionParser p, final int id) {
		super(p, id);
	}

	public Node getStartNode() {
		return child;
	}
	public AFunctionCallNode[] getFunctionArray() {
		return functionArray;
	}

	@Override
	public void init(final EMethod methodName) throws ParseException {
		if (jjtGetNumChildren() == 0) throw new ParseException("Does not contain any children.");
		method = methodName;
		child = getFirstChild();
		functionArray = new AFunctionCallNode[children.length-1];
		for (int i = 1 ; i < children.length ; ++i) {
			final Node n = children[i];
			if (!(n instanceof AFunctionCallNode)) throw new ParseException("Node not the correct type: " + n.getClass());
			functionArray[i-1] = (AFunctionCallNode)children[i];
		}
	}

	@Override
	public String toString() {
		return "DotExpressionNode(" + method + ")";
	}

}
