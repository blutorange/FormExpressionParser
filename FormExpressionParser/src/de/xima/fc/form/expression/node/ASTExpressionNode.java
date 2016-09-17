package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.util.EMethod;

public class ASTExpressionNode extends MySimpleNode {
	private EMethod method;
	private EMethod unaryMethod;
	private MySimpleNode child;
	private ASTExpressionNode[] childArray;

	public ASTExpressionNode(final int id) {
		super(id);
	}

	public ASTExpressionNode(final FormExpressionParser p, final int id) {
		super(p, id);
	}

	public final void init(final EMethod methodName) throws ParseException {
		method = methodName;
		if (children.length == 1)
			child = getSingleChild();
		else childArray = getChildArray(ASTExpressionNode.class);
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

	@Override
	public ALangObject evaluate(final IEvaluationContext ec) throws EvaluationException {
		// Unary expression
		if (child != null) {
			final ALangObject res = child.evaluate(ec);
			return unaryMethod == null ? res : res.evaluateInstanceMethod(unaryMethod.name, ec);
		}

		// Binary expression.
		ALangObject res = childArray[0].evaluate(ec);
		for (int i = 1 ; i != childArray.length; ++i) {
			final ASTExpressionNode op = childArray[i];
			res = res.evaluateInstanceMethod(op.method.name, ec, op.evaluate(ec));
		}
		return res;
	}

	public EMethod getMethod() {
		return method;
	}
}
