package de.xima.fc.form.expression.visitor;

import java.util.ArrayList;
import java.util.List;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.INamedFunction;
import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.exception.NoSuchFunctionException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.node.AFunctionCallNode;
import de.xima.fc.form.expression.node.ASTArrayNode;
import de.xima.fc.form.expression.node.ASTBooleanNode;
import de.xima.fc.form.expression.node.ASTDotExpressionNode;
import de.xima.fc.form.expression.node.ASTExpressionNode;
import de.xima.fc.form.expression.node.ASTForLoopNode;
import de.xima.fc.form.expression.node.ASTHashNode;
import de.xima.fc.form.expression.node.ASTIfClauseNode;
import de.xima.fc.form.expression.node.ASTNullNode;
import de.xima.fc.form.expression.node.ASTNumberNode;
import de.xima.fc.form.expression.node.ASTParenthesesFunction;
import de.xima.fc.form.expression.node.ASTPlainFunction;
import de.xima.fc.form.expression.node.ASTProgramNode;
import de.xima.fc.form.expression.node.ASTStatementListNode;
import de.xima.fc.form.expression.node.ASTStringNode;
import de.xima.fc.form.expression.node.ASTSwitchClauseNode;
import de.xima.fc.form.expression.node.ASTTryClauseNode;
import de.xima.fc.form.expression.node.ASTWhileLoopNode;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ArrayLangObject;
import de.xima.fc.form.expression.object.BooleanLangObject;
import de.xima.fc.form.expression.object.HashLangObject;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.object.StringLangObject;

public class EvaluateVisitor implements IFormExpressionParserVisitor<ALangObject, IEvaluationContext>{

	public EvaluateVisitor() {
	}

	private ALangObject[] getEvaluatedArgsArray(final Node[] args, final IEvaluationContext ec) throws EvaluationException {
		final ALangObject[] evaluatedArgs = new ALangObject[args.length];
		for (int i = 0; i != args.length; ++i)
			evaluatedArgs[i] = args[i].jjtAccept(this, ec);
		return evaluatedArgs;
	}

	@Override
	public ALangObject visit(final ASTExpressionNode node, final IEvaluationContext ec) throws EvaluationException {
		final int count = node.jjtGetNumChildren();

		// Empty expression node.
		if (count == 0) return NullLangObject.getInstance();

		final Node[] childrenArray = node.getChildArray();
		ALangObject res = childrenArray[0].jjtAccept(this, ec);

		// Unary expression
		if (count == 1) {
			final EMethod unaryMethod = node.getUnaryMethod();
			return unaryMethod == null ? res : res.evaluateInstanceMethod(unaryMethod.name, ec);
		}

		// Binary expression.
		for (int i = 1 ; i != childrenArray.length; ++i) {
			final Node arg = childrenArray[i];
			res = res.evaluateInstanceMethod(arg.getSiblingMethod().name, ec, arg.jjtAccept(this, ec));
		}
		return res;
	}

	@Override
	public ALangObject visit(final ASTDotExpressionNode node, final IEvaluationContext ec) throws EvaluationException {
		final Node child = node.getStartNode();
		final AFunctionCallNode[] functionArray = node.getFunctionArray();

		// Unary expression
		ALangObject res = child.jjtAccept(this, ec);
		if (functionArray.length == 0) return res;

		// Binary expression.
		for (int i = 0 ; i != functionArray.length; ++i) {
			final AFunctionCallNode sibling = functionArray[i];
			switch (sibling.getChainType()) {
			case ATTR_ACCESSOR:
				res = res.evaluateAttrAccessor(sibling.getMethodName(), ec);
				break;
			case INSTANCE_METHOD:
				final ALangObject[] eval = getEvaluatedArgsArray(sibling.getChildArray(), ec);
				res = res.evaluateInstanceMethod(sibling.getMethodName(), ec, eval);
				break;
			default:
				throw new EvaluationException(ec, "TODO: Add enum case to EvaluationVisitor, ASTDotExpressionNode.");
			}
		}
		return res;
	}

	@Override
	public ALangObject visit(final ASTParenthesesFunction node, final IEvaluationContext ec) throws EvaluationException {
		final String name = node.getMethodName();
		final INamedFunction<NullLangObject> function = ec.getNamespace().globalMethod(name);
		if (function == null) throw new NoSuchFunctionException("global function", name, ec);
		final ALangObject[] eval = getEvaluatedArgsArray(node.getChildArray(), ec);
		return ALangObject.create(function.evaluate(ec, NullLangObject.getInstance(), eval));
	}

	@Override
	public ALangObject visit(final ASTNumberNode node, final IEvaluationContext ec) throws EvaluationException {
		return NumberLangObject.create(node.getBigDecimalValue());
	}

	@Override
	public ALangObject visit(final ASTStringNode node, final IEvaluationContext ec) throws EvaluationException {
		return StringLangObject.create(node.getStringValue());
	}

	@Override
	public ALangObject visit(final ASTArrayNode node, final IEvaluationContext ec) throws EvaluationException {
		final Node[] childArray = node.getChildArray();
		final List<ALangObject> list = new ArrayList<ALangObject>(node.jjtGetNumChildren());
		for (final Node n : childArray) list.add(n.jjtAccept(this, ec));
		return ArrayLangObject.create(list);
	}

	@Override
	public ALangObject visit(final ASTHashNode node, final IEvaluationContext ec) throws EvaluationException {
		final Node[] childArray = node.getChildArray();
		final List<ALangObject> list = new ArrayList<ALangObject>(childArray.length);
		for (final Node n : childArray) list.add(n.jjtAccept(this, ec));
		return HashLangObject.create(list);
	}

	@Override
	public ALangObject visit(final ASTNullNode node, final IEvaluationContext ec) throws EvaluationException {
		return NullLangObject.getInstance();
	}

	@Override
	public ALangObject visit(final ASTBooleanNode node, final IEvaluationContext ec) throws EvaluationException {
		return BooleanLangObject.create(node.getBooleanValue());
	}

	@Override
	public ALangObject visit(final ASTPlainFunction node, final IEvaluationContext ec) throws EvaluationException {
		return ALangObject.create(ec.getBinding().getVariable(node.getMethodName()));
	}

	@Override
	public ALangObject visit(final ASTStatementListNode node, final IEvaluationContext ec)
			throws EvaluationException {
		ALangObject res = NullLangObject.getInstance();
		for (final Node n : node.getChildArray()) res = n.jjtAccept(this, ec);
		return res;
	}

	@Override
	public ALangObject visit(final ASTIfClauseNode node, final IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ALangObject visit(final ASTForLoopNode node, final IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ALangObject visit(final ASTWhileLoopNode node, final IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ALangObject visit(final ASTTryClauseNode node, final IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ALangObject visit(final ASTSwitchClauseNode node, final IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ALangObject visit(final ASTProgramNode node, final IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}
}
