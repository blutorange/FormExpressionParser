package de.xima.fc.form.expression.node;


import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.util.EMethod;

public final class ASTDotExpressionNode extends ASTExpressionNode {
	private AFunctionCallNode[] functionArray;

	public ASTDotExpressionNode(final int id) {
		super(id);
	}

	public ASTDotExpressionNode(final FormExpressionParser p, final int id) {
		super(p, id);
	}

	@Override
	public ALangObject evaluate(final IEvaluationContext ec) throws EvaluationException {
		// Unary expression
		ALangObject res = child.evaluate(ec);
		if (functionArray.length == 0) return res;

		// Binary expression.
		for (int i = 0 ; i != functionArray.length; ++i) {
			final AFunctionCallNode node = functionArray[i];
			res = node.chain(res, ec);
		}
		return res;
	}

	@Override
	public void init(final EMethod methodName) throws ParseException {
		if (jjtGetNumChildren() == 0) throw new ParseException("Does not contain any children.");
		method = methodName;
		child = getFirstChild();
		functionArray = new AFunctionCallNode[children.length-1];
		for (int i = 1 ; i!= children.length ; ++i) {
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
