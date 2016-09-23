package de.xima.fc.form.expression.visitor;

import java.util.ArrayList;
import java.util.List;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.INamedFunction;
import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.BreakClauseException;
import de.xima.fc.form.expression.exception.CatchableEvaluationException;
import de.xima.fc.form.expression.exception.ContinueClauseException;
import de.xima.fc.form.expression.exception.CustomRuntimeException;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.exception.NoSuchFunctionException;
import de.xima.fc.form.expression.exception.UncatchableEvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.node.AFunctionCallNode;
import de.xima.fc.form.expression.node.ASTArrayNode;
import de.xima.fc.form.expression.node.ASTAssignmentExpressionNode;
import de.xima.fc.form.expression.node.ASTBooleanNode;
import de.xima.fc.form.expression.node.ASTBreakClauseNode;
import de.xima.fc.form.expression.node.ASTContinueClauseNode;
import de.xima.fc.form.expression.node.ASTDoWhileLoopNode;
import de.xima.fc.form.expression.node.ASTDotExpressionNode;
import de.xima.fc.form.expression.node.ASTExceptionNode;
import de.xima.fc.form.expression.node.ASTExpressionNode;
import de.xima.fc.form.expression.node.ASTForLoopNode;
import de.xima.fc.form.expression.node.ASTHashNode;
import de.xima.fc.form.expression.node.ASTIfClauseNode;
import de.xima.fc.form.expression.node.ASTNullNode;
import de.xima.fc.form.expression.node.ASTNumberNode;
import de.xima.fc.form.expression.node.ASTParenthesesFunction;
import de.xima.fc.form.expression.node.ASTPlainFunction;
import de.xima.fc.form.expression.node.ASTStatementListNode;
import de.xima.fc.form.expression.node.ASTStringNode;
import de.xima.fc.form.expression.node.ASTSwitchClauseNode;
import de.xima.fc.form.expression.node.ASTThrowClauseNode;
import de.xima.fc.form.expression.node.ASTTryClauseNode;
import de.xima.fc.form.expression.node.ASTWhileLoopNode;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ArrayLangObject;
import de.xima.fc.form.expression.object.BooleanLangObject;
import de.xima.fc.form.expression.object.ExceptionLangObject;
import de.xima.fc.form.expression.object.HashLangObject;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.object.StringLangObject;

public class EvaluateVisitor implements IFormExpressionParserVisitor<ALangObject, IEvaluationContext> {

	public EvaluateVisitor() {
		reinit();
	}

	public void reinit() {
	}

	private ALangObject[] getEvaluatedArgsArray(final Node[] args, final IEvaluationContext ec)
			throws EvaluationException {
		final ALangObject[] evaluatedArgs = new ALangObject[args.length];
		for (int i = 0; i != args.length; ++i)
			evaluatedArgs[i] = args[i].jjtAccept(this, ec);
		return evaluatedArgs;
	}

	private void nest(final IEvaluationContext ec) {
		ec.setBinding(ec.getBinding().nest());
	}

	private void unnest(final IEvaluationContext ec) {
		ec.setBinding(ec.getBinding().unnest());
	}

	@Override
	public ALangObject visit(final ASTExpressionNode node, final IEvaluationContext ec) throws EvaluationException {
		final int count = node.jjtGetNumChildren();

		// Empty expression node.
		if (count == 0)
			return NullLangObject.getInstance();

		final Node[] childrenArray = node.getChildArray();
		ALangObject res = childrenArray[0].jjtAccept(this, ec);

		// Unary expression
		if (count == 1) {
			final EMethod unaryMethod = node.getUnaryMethod();
			return unaryMethod == null ? res : res.evaluateInstanceMethod(unaryMethod.name, ec);
		}

		// Binary expression.
		for (int i = 1; i != childrenArray.length; ++i) {
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
		if (functionArray.length == 0)
			return res;

		// Binary expression.
		for (int i = 0; i != functionArray.length; ++i) {
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
	public ALangObject visit(final ASTParenthesesFunction node, final IEvaluationContext ec)
			throws EvaluationException {
		final String name = node.getMethodName();
		final INamedFunction<NullLangObject> function = ec.getNamespace().globalMethod(name);
		if (function == null)
			throw new NoSuchFunctionException("global function", name, ec);
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
		for (final Node n : childArray)
			list.add(n.jjtAccept(this, ec));
		return ArrayLangObject.create(list);
	}

	@Override
	public ALangObject visit(final ASTHashNode node, final IEvaluationContext ec) throws EvaluationException {
		final Node[] childArray = node.getChildArray();
		final List<ALangObject> list = new ArrayList<ALangObject>(childArray.length);
		for (final Node n : childArray)
			list.add(n.jjtAccept(this, ec));
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
	public ALangObject visit(final ASTStatementListNode node, final IEvaluationContext ec) throws EvaluationException {
		ALangObject res = NullLangObject.getInstance();
		for (final Node n : node.getChildArray())
			res = n.jjtAccept(this, ec);
		return res;
	}

	@Override
	public ALangObject visit(final ASTIfClauseNode node, final IEvaluationContext ec) throws EvaluationException {
		final Node[] children = node.getChildArray();
		nest(ec);
		try {
			// If branch
			if (children[0].jjtAccept(this, ec).coerceBoolean(ec).booleanValue())
				return children[1].jjtAccept(this, ec);
			// Else branch
			else if (children.length == 3)
				return children[2].jjtAccept(this, ec);
			// No else
			else
				return NullLangObject.getInstance();
		} finally {
			unnest(ec);
		}
	}

	@Override
	public ALangObject visit(final ASTForLoopNode node, final IEvaluationContext ec) throws EvaluationException {
		final String variableName = node.getIteratingLoopVariable();
		final Node[] children = node.getChildArray();
		ALangObject res = NullLangObject.getInstance();
		nest(ec);
		try {
			if (variableName == null) {
				// Plain for loop
				children[0].jjtAccept(this, ec);
				while (children[1].jjtAccept(this, ec).coerceBoolean(ec).booleanValue()) {
					try {
						res = children[3].jjtAccept(this, ec);
						children[2].jjtAccept(this, ec);
					} catch (ContinueClauseException continueException) {
						if (continueException.label != null && !continueException.label.equals(node.getLabel()))
							// Pass on continue to some parent loop/switch.
							throw continueException;
					} catch (BreakClauseException breakException) {
						if (breakException.label != null && !breakException.label.equals(node.getLabel()))
							// Pass on break to some parent loop/switch.
							throw breakException;
						break;
					}
				}
			} else
				// Iterating for loop
				for (final ALangObject value : children[0].jjtAccept(this, ec)) {
				ec.getBinding().setVariable(variableName, value);
				try {
				children[1].jjtAccept(this, ec);
				} catch (ContinueClauseException continueException) {
				if (continueException.label != null && !continueException.label.equals(node.getLabel()))
					// Pass on continue to some parent loop/switch.
					throw continueException;
				}
				}
		} finally {
			unnest(ec);
		}
		return res;
	}

	@Override
	public ALangObject visit(final ASTWhileLoopNode node, final IEvaluationContext ec) throws EvaluationException {
		final Node[] children = node.getChildArray();
		ALangObject res = NullLangObject.getInstance();
		nest(ec);
		try {
			while (children[0].jjtAccept(this, ec).coerceBoolean(ec).booleanValue())
				try {
					res = children[1].jjtAccept(this, ec);
				} catch (final ContinueClauseException continueException) {
					if (continueException.label != null && !continueException.label.equals(node.getLabel()))
						// Pass on continue to some parent loop/switch.
						throw continueException;
				} catch (final BreakClauseException breakException) {
					if (breakException.label != null && !breakException.label.equals(node.getLabel()))
						// Pass on continue to some parent loop/switch.
						throw breakException;
					break;
				}
		} finally {
			unnest(ec);
		}
		return res;
	}

	@Override
	public ALangObject visit(final ASTDoWhileLoopNode node, final IEvaluationContext ec) throws EvaluationException {
		final Node[] children = node.getChildArray();
		ALangObject res = NullLangObject.getInstance();
		nest(ec);
		try {
			do
				try {
					res = children[1].jjtAccept(this, ec);
				} catch (ContinueClauseException continueException) {
					if (continueException.label != null && !continueException.label.equals(node.getLabel()))
						// Pass on continue to some parent loop/switch.
						throw continueException;
				} catch (BreakClauseException breakException) {
					if (breakException.label != null && breakException.label.equals(node.getLabel()))
						// Pass on break to some parent loop/switch.
						throw breakException;
					break;
				}
			while (children[0].jjtAccept(this, ec).coerceBoolean(ec).booleanValue());
		} finally {
			unnest(ec);
		}
		return res;
	}

	@Override
	public ALangObject visit(final ASTTryClauseNode node, final IEvaluationContext ec) throws EvaluationException {
		final Node[] children = node.getChildArray();
		nest(ec);
		try {
			return children[0].jjtAccept(this, ec);
		} catch (final CatchableEvaluationException e) {
			final ALangObject exception = ExceptionLangObject.create(e);
			ec.getBinding().setVariable(node.getErrorVariableName(), exception);
			return children[1].jjtAccept(this, ec);
		} finally {
			unnest(ec);
		}
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public ALangObject visit(final ASTSwitchClauseNode node, final IEvaluationContext ec) throws EvaluationException {
		final Node[] children = node.getChildArray();
		ALangObject res = NullLangObject.getInstance();
		boolean matchingCase = false;
		nest(ec);
		try {
			final ALangObject switchValue = children[0].jjtAccept(this, ec);
			forloop: for (int i = 1; i < children.length; ++i)
				switch (children[i].getSiblingMethod()) {
				case SWITCHCASE:
					if (!matchingCase && switchValue.equals(children[i - 1].jjtAccept(this, ec)))
						matchingCase = true;
					break;
				case SWITCHCLAUSE:
					if (matchingCase) {
						final ALangObject tmp;
						try {
							tmp = children[i].jjtAccept(this, ec);
						} catch (final BreakClauseException breakException) {
							if (breakException.label != null)
								// Pass on break to some parent loop/switch
								throw breakException;
							break forloop;
						}
						res = tmp;
					}
					break;
				case SWITCHDEFAULT:
					final ALangObject tmp;
					try {
						tmp = children[i].jjtAccept(this, ec);
					} catch (final BreakClauseException breakException) {
						if (breakException.label != null)
							// Pass on break to some parent loop/switch
							throw breakException;
						break forloop;
					}
					res = tmp;
				default:
					throw new UncatchableEvaluationException(ec, "Invalid switch syntax.");
				}
		} finally {
			unnest(ec);
		}
		return res;
	}

	@Override
	public ALangObject visit(final ASTAssignmentExpressionNode node, final IEvaluationContext ec)
			throws EvaluationException {
		// TODO
		return null;
	}

	@Override
	public ALangObject visit(final ASTExceptionNode node, final IEvaluationContext ec) throws EvaluationException {
		final StringLangObject message = node.jjtGetChild(0).jjtAccept(this, ec).coerceString(ec);
		return ExceptionLangObject.create(message.stringValue());
	}

	@Override
	public ALangObject visit(final ASTThrowClauseNode node, final IEvaluationContext ec) throws EvaluationException {
		final String message = node.jjtGetChild(0).jjtAccept(this, ec).coerceString(ec).stringValue();
		throw new CustomRuntimeException(message);
	}

	@Override
	public ALangObject visit(final ASTBreakClauseNode node, final IEvaluationContext ec) throws EvaluationException {
		throw new BreakClauseException(node.getLabel(), ec);
	}

	@Override
	public ALangObject visit(final ASTContinueClauseNode node, final IEvaluationContext ec) throws EvaluationException {
		throw new ContinueClauseException(node.getLabel(), ec);
	}
}
