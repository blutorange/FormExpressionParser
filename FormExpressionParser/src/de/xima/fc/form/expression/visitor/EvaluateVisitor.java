package de.xima.fc.form.expression.visitor;
import static de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants.JJTPROPERTYEXPRESSIONNODE;
import static de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants.JJTVARIABLENODE;

import java.util.ArrayList;
import java.util.List;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IFunction;
import de.xima.fc.form.expression.context.ILogger;
import de.xima.fc.form.expression.enums.EJump;
import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.BreakClauseException;
import de.xima.fc.form.expression.exception.CatchableEvaluationException;
import de.xima.fc.form.expression.exception.ContinueClauseException;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.exception.IllegalThisContextException;
import de.xima.fc.form.expression.exception.ReturnClauseException;
import de.xima.fc.form.expression.exception.UncatchableEvaluationException;
import de.xima.fc.form.expression.exception.VariableNotDefinedException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.node.ASTArrayNode;
import de.xima.fc.form.expression.node.ASTAssignmentExpressionNode;
import de.xima.fc.form.expression.node.ASTBooleanNode;
import de.xima.fc.form.expression.node.ASTBreakClauseNode;
import de.xima.fc.form.expression.node.ASTComparisonExpressionNode;
import de.xima.fc.form.expression.node.ASTContinueClauseNode;
import de.xima.fc.form.expression.node.ASTDoWhileLoopNode;
import de.xima.fc.form.expression.node.ASTEmptyNode;
import de.xima.fc.form.expression.node.ASTEqualExpressionNode;
import de.xima.fc.form.expression.node.ASTExceptionNode;
import de.xima.fc.form.expression.node.ASTExpressionNode;
import de.xima.fc.form.expression.node.ASTForLoopNode;
import de.xima.fc.form.expression.node.ASTFunctionClauseNode;
import de.xima.fc.form.expression.node.ASTFunctionNode;
import de.xima.fc.form.expression.node.ASTHashNode;
import de.xima.fc.form.expression.node.ASTIdentifierNameNode;
import de.xima.fc.form.expression.node.ASTIfClauseNode;
import de.xima.fc.form.expression.node.ASTLogNode;
import de.xima.fc.form.expression.node.ASTLosNode;
import de.xima.fc.form.expression.node.ASTNullNode;
import de.xima.fc.form.expression.node.ASTNumberNode;
import de.xima.fc.form.expression.node.ASTParenthesisExpressionNode;
import de.xima.fc.form.expression.node.ASTPostUnaryExpressionNode;
import de.xima.fc.form.expression.node.ASTPropertyExpressionNode;
import de.xima.fc.form.expression.node.ASTRegexNode;
import de.xima.fc.form.expression.node.ASTReturnClauseNode;
import de.xima.fc.form.expression.node.ASTStatementListNode;
import de.xima.fc.form.expression.node.ASTStringNode;
import de.xima.fc.form.expression.node.ASTSwitchClauseNode;
import de.xima.fc.form.expression.node.ASTTernaryExpressionNode;
import de.xima.fc.form.expression.node.ASTThrowClauseNode;
import de.xima.fc.form.expression.node.ASTTryClauseNode;
import de.xima.fc.form.expression.node.ASTUnaryExpressionNode;
import de.xima.fc.form.expression.node.ASTVariableNode;
import de.xima.fc.form.expression.node.ASTVariableTypeDeclarationNode;
import de.xima.fc.form.expression.node.ASTWhileLoopNode;
import de.xima.fc.form.expression.node.ASTWithClauseNode;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ALangObject.Type;
import de.xima.fc.form.expression.object.ArrayLangObject;
import de.xima.fc.form.expression.object.BooleanLangObject;
import de.xima.fc.form.expression.object.ExceptionLangObject;
import de.xima.fc.form.expression.object.FunctionLangObject;
import de.xima.fc.form.expression.object.HashLangObject;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.object.RegexLangObject;
import de.xima.fc.form.expression.object.StringLangObject;

public class EvaluateVisitor
implements IFormExpressionParserVisitor<ALangObject, IEvaluationContext, EvaluationException> {

	/**
	 * Evaluates the given node as a complete program with the given context.
	 * This method may be used by multiple threads, however, the node and
	 * evaluation context passed require external synchronization. This is
	 * because usually you would create a new evaluation context for each
	 * evaluation; and {@link Node} (should) be immutable after parsing.
	 *
	 * @param node
	 *            Node to evaluate.
	 * @param ec
	 *            Evaluation context to use.
	 * @return The evaluated result.
	 * @throws EvaluationException
	 *             When the code cannot be evaluated.
	 */
	public static ALangObject evaluateCode(final Node node, final IEvaluationContext ec) throws EvaluationException {
		final EvaluateVisitor v = new EvaluateVisitor();
		final ALangObject res;
		if (ec.getExternalContext() != null) ec.getExternalContext().beginWriting();
		try {
			res = node.jjtAccept(v, ec);
		}
		finally {
			if (ec.getExternalContext() != null) ec.getExternalContext().finishWriting();
		}
		v.assertNoJumps(ec);
		return res;
	}

	private ALangObject currentResult;
	private boolean mustJump;
	private EJump jumpType;
	private String jumpLabel;

	private EvaluateVisitor() {
		reinit();
	}

	public void reinit() throws EvaluationException {
		currentResult = NullLangObject.getInstance();
		mustJump = false;
		jumpType = null;
		jumpLabel = null;
	}

	private void assertNoJumps(final IEvaluationContext ec) throws UncatchableEvaluationException {
		if (mustJump) {
			switch (jumpType) {
			case BREAK:
				throw new BreakClauseException(jumpLabel, ec);
			case CONTINUE:
				throw new ContinueClauseException(jumpLabel, ec);
			case RETURN:
				throw new ReturnClauseException(ec);
			default:
				throw new UncatchableEvaluationException(ec, String.format(
						"Invalid jump type %s. This is likely a bug with the paser. Contact support.", jumpType));
			}
		}
	}

	private ALangObject jjtAccept(final Node parentNode, final Node node, final IEvaluationContext ec) {
		ec.getTracer().setCurrentlyProcessed(node);
		ec.getEmbedment().setCurrentEmbedment(node.getEmbedment());
		try {
			return currentResult = node.jjtAccept(this, ec);
		}
		finally {
			ec.getTracer().setCurrentlyProcessed(parentNode);
		}
	}

	private ALangObject setVariable(final ASTVariableNode var, final ALangObject val, final IEvaluationContext ec) {
		if (var.getScope() != null)
			ec.getScope().setVariable(var.getScope(), var.getName(), val);
		else
			ec.setUnqualifiedVariable(var.getName(), val);
		return val;
	}

	private ALangObject[] evaluateChildren(final Node node, final IEvaluationContext ec) {
		final Node[] children = node.getChildArray();
		final ALangObject[] res = new ALangObject[children.length];
		for (int i = 0; i != children.length; ++i)
			res[i] = jjtAccept(node, children[i], ec);
		return res;
	}

	private ALangObject evaluatePropertyExpression(final Node parentNode, final int indexOneAfterEnd,
			final IEvaluationContext ec) {
		final Node[] children = parentNode.getChildArray();
		// Child is an expressions and cannot contain break/clause/return
		// clauses.
		ALangObject res = jjtAccept(parentNode, children[0], ec);
		ALangObject thisContext = NullLangObject.getInstance();
		for (int i = 1; i < indexOneAfterEnd; ++i) {
			final Node n = children[i];
			switch (n.getSiblingMethod()) {
			case DOT:
				thisContext = res;
				final StringLangObject attrDot = jjtAccept(parentNode, n, ec).coerceString(ec);
				res = res.evaluateAttrAccessor(attrDot, true, ec);
				break;
			case BRACKET:
				thisContext = res;
				final ALangObject attrBracket = jjtAccept(parentNode, n, ec);
				res = res.evaluateAttrAccessor(attrBracket, false, ec);
				break;
			case PARENTHESIS:
				// Get a function object.
				final IFunction<ALangObject> func = res.coerceFunction(ec).functionValue();

				// Evaluate function arguments
				final ALangObject[] args = evaluateChildren(n, ec);

				// Check thisContext of the function.
				if (func.getThisContextType() != Type.NULL && func.getThisContextType() != thisContext.getType())
					throw new IllegalThisContextException(thisContext, func.getThisContextType(), func, ec);

				ec.getBinding().nestLocal(ec);
				ec.getTracer().descend(parentNode);
				try {
					// Evaluate function
					thisContext = res = func.evaluate(ec,
							func.getThisContextType() == Type.NULL ? NullLangObject.getInstance() : thisContext, args);
				}
				finally {
					ec.getTracer().ascend();
					ec.getBinding().unnest(ec);
				}
				// Check for disallowed break / continue clauses.
				if (mustJump) {
					switch (jumpType) {
					case RETURN:
						mustJump = false;
						break;
					case BREAK:
						throw new BreakClauseException(jumpLabel, ec);
					case CONTINUE:
						throw new ContinueClauseException(jumpLabel, ec);
					default:
						throw new EvaluationException(ec,
								String.format(
										"Unknown jump type %s after function evaluation. This is most likely a bug with the parser. Contact support.",
										jumpType));
					}
				}
				break;
				// $CASES-OMITTED$
			default:
				throw new UncatchableEvaluationException(ec,
						String.format(
								"Illegal enum constant %s at ASTPropertyExpressionNode. This is likely a bug with the parser. Contact support.",
								n.getSiblingMethod()));
			}
		}
		return res;
	}

	private ALangObject performAssignment(final Node node, final Node child, final EMethod method, ALangObject assignee, final IEvaluationContext ec) {
		switch (child.jjtGetNodeId()) {
		case JJTVARIABLENODE:
			final ASTVariableNode var = (ASTVariableNode)child;
			// Compound assignments (a+=b, etc.) are the same as a=a+b etc.
			// First we need to evaluate the variable (a),
			// then the expression method (+).
			if (method != EMethod.EQUAL)
				assignee = jjtAccept(node, var, ec).evaluateExpressionMethod(method.equalMethod(), ec,
						assignee);
			// Now we can set the variable to its new value.
			setVariable(var, assignee, ec);
			break;
		case JJTPROPERTYEXPRESSIONNODE:
			final ASTPropertyExpressionNode prop = (ASTPropertyExpressionNode)child;
			final ALangObject res = evaluatePropertyExpression(prop, prop.jjtGetNumChildren() - 1, ec);
			final Node last = prop.getLastChild();
			switch (last.getSiblingMethod()) {
			case DOT:
				final StringLangObject attrDot = jjtAccept(prop, last, ec).coerceString(ec);
				// Compound assignments (a.b+=c etc.) are the same as a.b=a.b+c etc.
				// First we need to evaluate the last attribute accessor (a.b),
				// then the expression method (+).
				if (method != EMethod.EQUAL)
					assignee = res.evaluateAttrAccessor(attrDot, true, ec)
					.evaluateExpressionMethod(method.equalMethod(), ec, assignee);
				// Now we can call the attribute assigner and assign the
				// value.
				res.executeAttrAssigner(attrDot, true, assignee, ec);
				break;
			case BRACKET:
				final ALangObject attrBracket = jjtAccept(prop, last, ec);
				// Compound assignments (a[b]+=c etc.) are the same as a[b]=a[b]+c etc.
				// First we need to evaluate the last attribute accessor (a[b]),
				// then the expression method (+).
				if (method != EMethod.EQUAL)
					assignee = res.evaluateAttrAccessor(attrBracket, false, ec)
					.evaluateExpressionMethod(method.equalMethod(), ec, assignee);
				res.executeAttrAssigner(attrBracket, false, assignee, ec);
				break;
				// $CASES-OMITTED$
			default:
				throw new UncatchableEvaluationException(ec,
						String.format(
								"Illegal enum constant %s at %s. This is likely a bug with the parser. Contact support.",
								last.getSiblingMethod(), node.getClass().getSimpleName()));
			}
			break;
			// $CASES-OMITTED$
		default:
			throw new UncatchableEvaluationException(ec,
					String.format(
							"Unknown assignment type %s at %s. This is likely a bug with the parser. Contact support.",
							child.getSiblingMethod(), node.getClass().getSimpleName()));
		}
		return assignee;
	}

	private ALangObject performPostUnaryAssignment(final Node node, final Node child, final EMethod method, final IEvaluationContext ec) {
		final ALangObject res, tmp;
		switch (child.jjtGetNodeId()) {
		case JJTVARIABLENODE:
			final ASTVariableNode var = (ASTVariableNode)child;
			// Compound assignments (a+=b, etc.) are the same as a=a+b etc.
			// First we need to evaluate the variable (a),
			// then the expression method (+).
			res = jjtAccept(node, var, ec);
			// Now we can set the variable to its new value.
			setVariable(var, res.evaluateExpressionMethod(method.equalMethod(), ec), ec);
			break;
		case JJTPROPERTYEXPRESSIONNODE:
			final ASTPropertyExpressionNode prop = (ASTPropertyExpressionNode)child;
			tmp = evaluatePropertyExpression(prop, prop.jjtGetNumChildren() - 1, ec);
			final Node last = prop.getLastChild();
			switch (last.getSiblingMethod()) {
			case DOT:
				final StringLangObject attrDot = jjtAccept(prop, last, ec).coerceString(ec);
				// Compound assignments (a.b+=c etc.) are the same as a.b=a.b+c etc.
				// First we need to evaluate the last attribute accessor (a.b),
				// then the expression method (+).
				res = tmp.evaluateAttrAccessor(attrDot, true, ec);
				// Now we can call the attribute assigner and assign the
				// value.
				res.executeAttrAssigner(attrDot, true, res.evaluateExpressionMethod(method.equalMethod(), ec),
						ec);
				break;
			case BRACKET:
				final ALangObject attrBracket = jjtAccept(prop, last, ec);
				// Compound assignments (a[b]+=c etc.) are the same as a[b]=a[b]+c etc.
				// First we need to evaluate the last attribute accessor (a[b]),
				// then the expression method (+).
				res = tmp.evaluateAttrAccessor(attrBracket, false, ec);
				tmp.executeAttrAssigner(attrBracket, false,
						res.evaluateExpressionMethod(method.equalMethod(), ec), ec);
				break;
				// $CASES-OMITTED$
			default:
				throw new UncatchableEvaluationException(ec,
						String.format(
								"Illegal enum constant %s at %s. This is likely a bug with the parser. Contact support.",
								last.getSiblingMethod(), node.getClass().getSimpleName()));
			}
			break;
			// $CASES-OMITTED$
		default:
			throw new UncatchableEvaluationException(ec,
					String.format(
							"Unknown assignment type %s at %s. This is likely a bug with the parser. Contact support.",
							child.getSiblingMethod(), node.getClass().getSimpleName()));
		}
		return res;
	}

	@Override
	public ALangObject visit(final ASTUnaryExpressionNode node, final IEvaluationContext ec)
			throws EvaluationException {
		// Child must be an expression and cannot be a break/continue/return node.
		if (node.getUnaryMethod().isAssigning())
			return performAssignment(node, node.getFirstChild(), node.getUnaryMethod(), null, ec);
		if (node.getUnaryMethod() == EMethod.EXCLAMATION)
			return jjtAccept(node, node.getFirstChild(), ec).coerceBoolean(ec).not();
		return jjtAccept(node, node.getFirstChild(), ec).evaluateExpressionMethod(node.getUnaryMethod(), ec);
	}

	@Override
	public ALangObject visit(final ASTPostUnaryExpressionNode node, final IEvaluationContext ec)
			throws EvaluationException {
		// Child must be an expression and cannot be a break/continue/return node.
		if (node.getUnaryMethod().isAssigning())
			return performPostUnaryAssignment(node, node.getFirstChild(), node.getUnaryMethod(), ec);
		return jjtAccept(node, node.getFirstChild(), ec).evaluateExpressionMethod(node.getUnaryMethod(), ec);
	}

	@Override
	public ALangObject visit(final ASTExpressionNode node, final IEvaluationContext ec) throws EvaluationException {
		// Arguments are expressions which cannot be clause/continue/return
		// clauses
		final Node[] childrenArray = node.getChildArray();

		// Empty expression node.
		if (childrenArray.length == 0)
			return NullLangObject.getInstance();

		// Binary expression node.
		// Children are expressions and cannot contain break/clause/return
		// clauses.
		ALangObject res = jjtAccept(node, childrenArray[0], ec);
		for (int i = 1; i != childrenArray.length; ++i) {
			final Node arg = childrenArray[i];
			// Children are expressions and cannot contain break/clause/return
			// clauses.
			res = res.evaluateExpressionMethod(arg.getSiblingMethod(), ec, jjtAccept(node, arg, ec));
		}

		return res;
	}

	@Override
	public ALangObject visit(final ASTPropertyExpressionNode node, final IEvaluationContext ec)
			throws EvaluationException {
		return evaluatePropertyExpression(node, node.jjtGetNumChildren(), ec);
	}

	@Override
	public ALangObject visit(final ASTNumberNode node, final IEvaluationContext ec) throws EvaluationException {
		return NumberLangObject.create(node.getDoubleValue());
	}

	@Override
	public ALangObject visit(final ASTStringNode node, final IEvaluationContext ec) throws EvaluationException {
		return StringLangObject.create(node.getStringValue());
	}

	@Override
	public ALangObject visit(final ASTArrayNode node, final IEvaluationContext ec) throws EvaluationException {
		final Node[] childArray = node.getChildArray();
		final List<ALangObject> list = new ArrayList<ALangObject>(node.jjtGetNumChildren());
		for (final Node n : childArray) {
			// Children are expression and cannot be break/clause/return
			// clauses.
			list.add(jjtAccept(node, n, ec));
		}
		return ArrayLangObject.create(list);
	}

	@Override
	public ALangObject visit(final ASTHashNode node, final IEvaluationContext ec) throws EvaluationException {
		final Node[] childArray = node.getChildArray();
		final List<ALangObject> list = new ArrayList<ALangObject>(childArray.length);
		for (final Node n : childArray) {
			// Children are expressions and cannot contain break/clause/return
			// clauses.
			list.add(jjtAccept(node, n, ec));
		}
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
	public ALangObject visit(final ASTVariableNode node, final IEvaluationContext ec) throws EvaluationException {
		final String scope = node.getScope();
		if (scope != null) {
			final ALangObject value = ec.getScope().getVariable(scope, node.getName(), ec);
			if (value == null)
				throw new VariableNotDefinedException(scope, node.getName(), ec);
			return value;
		}
		return ec.getUnqualifiedVariable(node.getName());
	}

	@Override
	public ALangObject visit(final ASTStatementListNode node, final IEvaluationContext ec) throws EvaluationException {
		ALangObject res = NullLangObject.getInstance();
		for (final Node n : node.getChildArray()) {
			res = jjtAccept(node, n, ec);
			if (mustJump)
				break;
		}
		return res;
	}

	@Override
	public ALangObject visit(final ASTIfClauseNode node, final IEvaluationContext ec) throws EvaluationException {
		final Node[] children = node.getChildArray();
		ec.getBinding().nest(ec);
		try {
			// If branch, no explicit check for mustJump as it returns
			// immediately anyway
			if (jjtAccept(node, children[0], ec).coerceBoolean(ec).booleanValue())
				return jjtAccept(node, children[1], ec);
			// Else branch, no explicit check for mustJump as it returns
			// immediately anyway
			else if (children.length == 3)
				return jjtAccept(node, children[2], ec);
			// No else
			else
				return NullLangObject.getInstance();
		}
		finally {
			ec.getBinding().unnest(ec);
		}
	}

	@Override
	public ALangObject visit(final ASTForLoopNode node, final IEvaluationContext ec) throws EvaluationException {
		final String variableName = node.getIteratingLoopVariable();
		final Node[] children = node.getChildArray();
		ALangObject res = NullLangObject.getInstance();
		ec.getBinding().nest(ec);
		try {
			if (variableName == null) {
				// Plain for loop
				jjtAccept(node, children[0], ec);
				whileloop: while (jjtAccept(node, children[1], ec).coerceBoolean(ec).booleanValue()) {
					res = jjtAccept(node, children[3], ec);
					// Handle break, continue, return.
					if (mustJump) {
						if (jumpType == EJump.RETURN || (jumpLabel != null && !jumpLabel.equals(node.getLabel())))
							return res;
						mustJump = false;
						if (jumpType == EJump.BREAK)
							break whileloop;
					}
					jjtAccept(node, children[2], ec);
				}
			}
			else {
				// Iterating for loop
				forloop: for (final ALangObject value : jjtAccept(node, children[0], ec).getIterable(ec)) {
					ec.getBinding().setVariable(variableName, value);
					res = jjtAccept(node, children[1], ec);
					// Handle break, continue, return.
					if (mustJump) {
						if (jumpType == EJump.RETURN || (jumpLabel != null && !jumpLabel.equals(node.getLabel())))
							return res;
						mustJump = false;
						if (jumpType == EJump.BREAK)
							break forloop;
					}
				}
			}
		}
		finally {
			ec.getBinding().unnest(ec);
		}
		return res;
	}

	@Override
	public ALangObject visit(final ASTWhileLoopNode node, final IEvaluationContext ec) throws EvaluationException {
		final Node[] children = node.getChildArray();
		ALangObject res = NullLangObject.getInstance();
		ec.getBinding().nest(ec);
		try {
			whileloop: while (jjtAccept(node, children[0], ec).coerceBoolean(ec).booleanValue()) {
				res = jjtAccept(node, children[1], ec);
				// Handle break, continue, return.
				if (mustJump) {
					if (jumpType == EJump.RETURN || (jumpLabel != null && !jumpLabel.equals(node.getLabel())))
						return res;
					mustJump = false;
					if (jumpType == EJump.BREAK)
						break whileloop;
				}
			}
		}
		finally {
			ec.getBinding().unnest(ec);
		}
		return res;
	}

	@Override
	public ALangObject visit(final ASTDoWhileLoopNode node, final IEvaluationContext ec) throws EvaluationException {
		final Node[] children = node.getChildArray();
		ALangObject res = NullLangObject.getInstance();
		ec.getBinding().nest(ec);
		try {
			doloop: do {
				res = jjtAccept(node, children[0], ec);
				// Handle break, continue, return.
				if (mustJump) {
					if (jumpType == EJump.RETURN || (jumpLabel != null && !jumpLabel.equals(node.getLabel())))
						return res;
					mustJump = false;
					if (jumpType == EJump.BREAK)
						break doloop;
				}
			}
			while (jjtAccept(node, children[1], ec).coerceBoolean(ec).booleanValue());
		}
		finally {
			ec.getBinding().unnest(ec);
		}
		return res;
	}

	@Override
	public ALangObject visit(final ASTTryClauseNode node, final IEvaluationContext ec) throws EvaluationException {
		final Node[] children = node.getChildArray();
		ALangObject res = NullLangObject.getInstance();
		CatchableEvaluationException exception = null;
		ec.getBinding().nest(ec);
		try {
			// Try branch, no explicit check for mustJump as it returns
			// immediately anyway
			res = jjtAccept(node, children[0], ec);
		}
		catch (final CatchableEvaluationException e) {
			exception = e;
		}
		finally {
			ec.getBinding().unnest(ec);
		}
		// If mustJump is true, break/continue/return was the last statement
		// evaluated and no exception could have been thrown.
		if (exception != null) {
			ec.getBinding().nest(ec);
			try {
				final ALangObject e = ExceptionLangObject.create(exception, ec);
				ec.getBinding().setVariable(node.getErrorVariableName(), e);
				// Catch branch, no explicit check for mustJump as it returns
				// immediately anyway
				res = jjtAccept(node, children[1], ec);
			}
			finally {
				ec.getBinding().unnest(ec);
			}
		}
		return res;
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public ALangObject visit(final ASTSwitchClauseNode node, final IEvaluationContext ec) throws EvaluationException {
		final Node[] children = node.getChildArray();
		ALangObject res = NullLangObject.getInstance();
		boolean matchingCase = false;
		ec.getBinding().nest(ec);
		try {
			final ALangObject switchValue = jjtAccept(node, children[0], ec);
			forloop: for (int i = 1; i < children.length; ++i) {
				switch (children[i].getSiblingMethod()) {
				case SWITCHCASE:
					// Case contains an expression and may not contain break,
					// continue, or throw clauses.
					if (!matchingCase && switchValue.equals(jjtAccept(node, children[i], ec))) {
						matchingCase = true;
					}
					break;
				case SWITCHCLAUSE:
					if (matchingCase) {
						res = jjtAccept(node, children[i], ec);
						// Handle continue, break, return.
						if (mustJump) {
							if (jumpType == EJump.RETURN || (jumpLabel != null && !jumpLabel.equals(node.getLabel())))
								return res;
							mustJump = false;
							if (jumpType == EJump.BREAK)
								break forloop;
						}
					}
					break;
				case SWITCHDEFAULT:
					res = jjtAccept(node, children[i], ec);
					// Handle continue, break, return.
					if (mustJump) {
						if (jumpType == EJump.RETURN || (jumpLabel != null && !jumpLabel.equals(node.getLabel())))
							return res;
						mustJump = false;
						if (jumpType == EJump.BREAK)
							break forloop;
					}
					break;
				default:
					throw new UncatchableEvaluationException(ec,
							"Invalid switch syntax. This is most likely an error with the parser. Contact support.");
				}
			}
		}
		finally {
			ec.getBinding().unnest(ec);
		}
		return res;
	}

	@Override
	public ALangObject visit(final ASTExceptionNode node, final IEvaluationContext ec) throws EvaluationException {
		// Child is an expression and cannot contain any break, continue, or
		// return clause.
		final StringLangObject message = jjtAccept(node, node.jjtGetChild(0), ec).coerceString(ec);
		return ExceptionLangObject.create(message.stringValue(), ec);
	}

	@Override
	public ALangObject visit(final ASTThrowClauseNode node, final IEvaluationContext ec) throws EvaluationException {
		// Child is an expression and cannot contain any break, continue, or
		// return clause.
		throw jjtAccept(node, node.jjtGetChild(0), ec).coerceException(ec).exceptionValue();
	}

	@Override
	public ALangObject visit(final ASTBreakClauseNode node, final IEvaluationContext ec) throws EvaluationException {
		jumpLabel = node.getLabel();
		jumpType = EJump.BREAK;
		mustJump = true;
		return NullLangObject.getInstance();
	}

	@Override
	public ALangObject visit(final ASTContinueClauseNode node, final IEvaluationContext ec) throws EvaluationException {
		jumpLabel = node.getLabel();
		jumpType = EJump.CONTINUE;
		mustJump = true;
		return NullLangObject.getInstance();
	}

	@Override
	public ALangObject visit(final ASTReturnClauseNode node, final IEvaluationContext ec) throws EvaluationException {
		jumpLabel = null;
		jumpType = EJump.RETURN;
		mustJump = true;
		return node.jjtGetNumChildren() == 0 ? NullLangObject.getInstance() : jjtAccept(node, node.jjtGetChild(0), ec);
	}

	@Override
	public ALangObject visit(final ASTLogNode node, final IEvaluationContext ec) throws EvaluationException {
		// Child must be an expression and cannot contain any break, continue,
		// or return clause.
		final StringLangObject message = jjtAccept(node, node.jjtGetChild(0), ec).coerceString(ec);
		final ILogger logger = ec.getLogger();
		switch (node.getLogLevel()) {
		case DEBUG:
			logger.debug(message.stringValue());
			break;
		case ERROR:
			logger.error(message.stringValue());
			break;
		case INFO:
			logger.info(message.stringValue());
			break;
		case WARN:
			logger.warn(message.stringValue());
			break;
		default:
			throw new UncatchableEvaluationException(ec,
					String.format("No such log level %s. This is most likely a bug with the parser. Contact support.",
							node.getLogLevel()));
		}
		return message;
	}

	@Override
	public ALangObject visit(final ASTFunctionNode node, final IEvaluationContext ec) throws EvaluationException {
		return FunctionLangObject.create(new EvaluateVisitorAnonymousFunction(this, node, ec));
	}

	@Override
	public ALangObject visit(final ASTFunctionClauseNode node, final IEvaluationContext ec) throws EvaluationException {
		final ALangObject func = FunctionLangObject.create(new EvaluateVisitorNamedFunction(this, node, ec));
		return setVariable((ASTVariableNode) node.getFirstChild(), func, ec);
	}

	@Override
	public ALangObject visit(final ASTIdentifierNameNode node, final IEvaluationContext ec) throws EvaluationException {
		return StringLangObject.create(node.getName());
	}

	@Override
	public ALangObject visit(final ASTWithClauseNode node, final IEvaluationContext ec) throws EvaluationException {
		final Node[] children = node.getChildArray();
		final int len = children.length - 1;
		// Set scopes.
		for (int i = 0; i != len; ++i) {
			// Children can only be identifiers, not break/continue/return.
			ec.beginDefaultScope(jjtAccept(node, children[i], ec).coerceString(ec).stringValue());
		}
		// Evaluate block.
		try {
			// Need not check for must jump as we return immediately anyway.
			return jjtAccept(node, children[children.length - 1], ec);
		}
		finally {
			// Remove scopes.
			for (int i = 0; i != len; ++i)
				ec.endDefaultScope();
		}
	}

	@Override
	public ALangObject visit(final ASTAssignmentExpressionNode node, final IEvaluationContext ec)
			throws EvaluationException {
		final Node[] children = node.getChildArray();
		// Iterate from the end of each assignment pair and assign the rvalue to
		// the lvalue.
		// Child must be an expression and cannot contain break/continue/return.
		ALangObject assignee = jjtAccept(node, children[children.length - 1], ec);
		for (int i = children.length - 2; i >= 0; --i) {
			final EMethod method = children[i + 1].getSiblingMethod();
			assignee = performAssignment(node, children[i], method, assignee, ec);
		}
		return assignee;
	}

	@Override
	public ALangObject visit(final ASTEmptyNode node, final IEvaluationContext ec) throws EvaluationException {
		return this.currentResult;
	}

	@Override
	public ALangObject visit(final ASTLosNode node, final IEvaluationContext ec) throws EvaluationException {
		if (node.isHasClose())
			ec.getEmbedment().outputCode(currentResult.coerceString(ec).stringValue(), ec);
		if (node.isHasText())
			ec.getEmbedment().outputText(node.getText(), ec);
		// if (node.isHasOpen()) {
		// }
		return NullLangObject.getInstance();
	}

	@Override
	public ALangObject visit(final ASTRegexNode node, final IEvaluationContext ec) throws EvaluationException {
		return RegexLangObject.create(node.getPattern());
	}

	@Override
	public ALangObject visit(final ASTTernaryExpressionNode node, final IEvaluationContext ec)
			throws EvaluationException {
		if (jjtAccept(node, node.jjtGetChild(0), ec).coerceBoolean(ec).booleanValue()) {
			return jjtAccept(node, node.jjtGetChild(1), ec);
		}
		return jjtAccept(node, node.jjtGetChild(2), ec);
	}

	@Override
	public ALangObject visit(final ASTParenthesisExpressionNode node, final IEvaluationContext ec)
			throws EvaluationException {
		return jjtAccept(node, node.getFirstChild(), ec);
	}

	@Override
	public ALangObject visit(final ASTComparisonExpressionNode node, final IEvaluationContext ec) throws EvaluationException {
		// Arguments are expressions which cannot be clause/continue/return
		// clauses
		final Node[] childrenArray = node.getChildArray();

		// Empty expression node.
		if (childrenArray.length == 0)
			return NullLangObject.getInstance();

		// Binary expression node.
		// Children are expressions and cannot contain break/clause/return
		// clauses.
		ALangObject res = jjtAccept(node, childrenArray[0], ec);
		for (int i = 1; i != childrenArray.length; ++i) {
			// Children are expressions and cannot contain break/clause/return
			// clauses.
			res = BooleanLangObject.create(childrenArray[i].getSiblingMethod()
					.checkComparison(res.compareTo(jjtAccept(node, childrenArray[i], ec))));
		}
		return res;
	}

	@Override
	public ALangObject visit(final ASTEqualExpressionNode node, final IEvaluationContext ec) throws EvaluationException {
		// Arguments are expressions which cannot be clause/continue/return
		// clauses
		final Node[] childrenArray = node.getChildArray();

		// Empty expression node.
		if (childrenArray.length == 0)
			return NullLangObject.getInstance();

		// Binary expression node.
		// Children are expressions and cannot contain break/clause/return
		// clauses.
		ALangObject res = jjtAccept(node, childrenArray[0], ec);
		for (int i = 1; i != childrenArray.length; ++i) {
			// Children are expressions and cannot contain break/clause/return
			// clauses.
			//!~ =~ == === !== !=
			switch (childrenArray[i].getSiblingMethod()) {
			case DOUBLE_EQUAL:
				res = BooleanLangObject.create(res.equals(jjtAccept(node, childrenArray[i], ec)));
				break;
			case EXCLAMATION_EQUAL:
				res = BooleanLangObject.create(!res.equals(jjtAccept(node, childrenArray[i], ec)));
				break;
			case TRIPLE_EQUAL:
				res = BooleanLangObject.create(res.equalsSameObject(jjtAccept(node, childrenArray[i], ec)));
				break;
			case EXCLAMATION_DOUBLE_EQUAL:
				res = BooleanLangObject.create(!res.equalsSameObject(jjtAccept(node, childrenArray[i], ec)));
				break;
			case EQUAL_TILDE:
				res = res.evaluateExpressionMethod(EMethod.EQUAL_TILDE,	ec, jjtAccept(node, childrenArray[i], ec)).coerceBoolean(ec);
				break;
			case EXCLAMATION_TILDE:
				res = res.evaluateExpressionMethod(EMethod.EQUAL_TILDE,	ec, jjtAccept(node, childrenArray[i], ec)).coerceBoolean(ec).not();
				break;
				//$CASES-OMITTED$
			default:
				throw new UncatchableEvaluationException(ec,
						String.format("Unexptected enum %s. This is likely a bug with the parser. Contact support.",
								childrenArray[i].getSiblingMethod()));
			}
		}

		return res;
	}

	@Override
	public ALangObject visit(final ASTVariableTypeDeclarationNode node, final IEvaluationContext ec) throws EvaluationException {
		// Type information is discarded at runtime.
		throw new UncatchableEvaluationException(ec, "ASTVariableTypeDeclarationNode cannot be evaluated. This is most likely a bug with the parser. Contact support.");
	}
}
