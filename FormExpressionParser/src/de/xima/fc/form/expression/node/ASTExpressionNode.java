package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.util.EMethod;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTExpressionNode extends MySimpleNode {
	protected EMethod method;
	protected EMethod unaryMethod;
	protected Node child;
	private ASTExpressionNode[] expressionArray;

	public ASTExpressionNode(final int id) {
		super(id);
	}

	public ASTExpressionNode(final FormExpressionParser p, final int id) {
		super(p, id);
	}

	public void init(final EMethod methodName) throws ParseException {
		method = methodName;
		if (children.length == 1)
			child = getSingleChild();
		else expressionArray = getChildArray(ASTExpressionNode.class);
	}

	public final void init(final EMethod methodName, final EMethod unaryMethod) throws ParseException {
		method = methodName;
		this.unaryMethod = unaryMethod;
		child = getSingleChild();
	}

	@Override
	public String toString() {
		if (child != null)
			return "UnaryExpressionNode(" + String.valueOf(method) + "," + String.valueOf(unaryMethod) + ")";
		return "BinaryExpressionNode(" + (method == null ? "null" : method.name()) + ")";
	}

	public EMethod getUnaryMethod() {
		return unaryMethod;
	}
	public EMethod getBinaryMethod() {
		return method;
	}
	public Node getUnaryChild() {
		return child;
	}
	public ASTExpressionNode[] getExpressionArray() {
		return expressionArray;
	}

	//TODO remove this
	//	@Override
	//	public ALangObject evaluate(final IEvaluationContext ec) throws EvaluationException {
	//		// Unary expression
	//		if (child != null) {
	//			final ALangObject res = child.evaluate(ec);
	//			return unaryMethod == null ? res : res.evaluateInstanceMethod(unaryMethod.name, ec);
	//		}
	//
	//		// Binary expression.
	//		ALangObject res = expressionArray[0].evaluate(ec);
	//		for (int i = 1 ; i != expressionArray.length; ++i) {
	//			final ASTExpressionNode op = expressionArray[i];
	//			res = res.evaluateInstanceMethod(op.method.name, ec, op.evaluate(ec));
	//		}
	//		return res;
	//	}

	@Override
	public <R, T> R jjtAccept(final IFormExpressionParserVisitor<R, T> visitor, final T data) throws EvaluationException {
		return visitor.visit(this, data);
	}
}
