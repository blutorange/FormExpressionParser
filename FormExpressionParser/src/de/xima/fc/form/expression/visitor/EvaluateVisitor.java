package de.xima.fc.form.expression.visitor;

import static de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants.JJTPROPERTYEXPRESSIONNODE;
import static de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants.JJTVARIABLENODE;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.enums.EJump;
import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.evaluation.BreakClauseException;
import de.xima.fc.form.expression.exception.evaluation.CatchableEvaluationException;
import de.xima.fc.form.expression.exception.evaluation.ContinueClauseException;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.exception.evaluation.IllegalVariableAssignmentException;
import de.xima.fc.form.expression.exception.evaluation.MissingExternalContextException;
import de.xima.fc.form.expression.exception.evaluation.ReturnClauseException;
import de.xima.fc.form.expression.exception.evaluation.UncatchableEvaluationException;
import de.xima.fc.form.expression.exception.evaluation.UnhandledEnumException;
import de.xima.fc.form.expression.exception.evaluation.UnhandledNodeTypeException;
import de.xima.fc.form.expression.exception.evaluation.UnresolvedVariableSourceException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.evaluate.IClosure;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IExternalContext;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionReturnVoidVisitor;
import de.xima.fc.form.expression.iface.parse.IHeaderNode;
import de.xima.fc.form.expression.iface.parse.IScopeDefinitions;
import de.xima.fc.form.expression.iface.parse.ISourceResolvable;
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
import de.xima.fc.form.expression.node.ASTFunctionArgumentNode;
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
import de.xima.fc.form.expression.node.ASTScopeExternalNode;
import de.xima.fc.form.expression.node.ASTScopeGlobalNode;
import de.xima.fc.form.expression.node.ASTScopeManualNode;
import de.xima.fc.form.expression.node.ASTStatementListNode;
import de.xima.fc.form.expression.node.ASTStringCharactersNode;
import de.xima.fc.form.expression.node.ASTStringNode;
import de.xima.fc.form.expression.node.ASTSwitchClauseNode;
import de.xima.fc.form.expression.node.ASTTernaryExpressionNode;
import de.xima.fc.form.expression.node.ASTThrowClauseNode;
import de.xima.fc.form.expression.node.ASTTryClauseNode;
import de.xima.fc.form.expression.node.ASTUnaryExpressionNode;
import de.xima.fc.form.expression.node.ASTVariableDeclarationClauseNode;
import de.xima.fc.form.expression.node.ASTVariableNode;
import de.xima.fc.form.expression.node.ASTVariableTypeNode;
import de.xima.fc.form.expression.node.ASTWhileLoopNode;
import de.xima.fc.form.expression.node.ASTWithClauseNode;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ArrayLangObject;
import de.xima.fc.form.expression.object.BooleanLangObject;
import de.xima.fc.form.expression.object.ExceptionLangObject;
import de.xima.fc.form.expression.object.FunctionLangObject;
import de.xima.fc.form.expression.object.HashLangObject;
import de.xima.fc.form.expression.object.INonNullIterator;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.object.RegexLangObject;
import de.xima.fc.form.expression.object.StringLangObject;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
public class EvaluateVisitor implements IFormExpressionReturnVoidVisitor<ALangObject, EvaluationException> {

	private final IEvaluationContext ec;
	private ALangObject currentResult = NullLangObject.getInstance();

	private final ALangObject[] symbolTable;

	private EvaluateVisitor(final IEvaluationContext ec, final int symbolTableSize) {
		symbolTable = new ALangObject[symbolTableSize < 0 ? 0 : symbolTableSize];
		this.ec = ec;
		reinit();
	}

	private void reinit() {
		currentResult = NullLangObject.getInstance();
		for (int i = symbolTable.length; i --> 0;)
			symbolTable[i] = NullLangObject.getInstance();
	}

	private void assertNoJumps() throws UncatchableEvaluationException {
		switch (ec.getJumpType()) {
		case NONE:
			return;
		case BREAK:
			throw new BreakClauseException(ec.getJumpLabel(), ec);
		case CONTINUE:
			throw new ContinueClauseException(ec.getJumpLabel(), ec);
		case RETURN:
			throw new ReturnClauseException(ec);
		default:
			throw new UncatchableEvaluationException(ec,
					NullUtil.messageFormat(CmnCnst.Error.INVALID_JUMP_TYPE, ec.getJumpType()));
		}
	}

	private ALangObject jjtAccept(final Node parentNode, @Nullable final Node node) throws EvaluationException {
		if (node == null)
			throw new UncatchableEvaluationException(ec, CmnCnst.Error.NULL_CHILD_NODE);
		ec.getTracer().setCurrentlyProcessed(node);
		ec.getEmbedment().setCurrentEmbedment(node.getEmbedment());
		try {
			return currentResult = node.jjtAccept(this);
		}
		finally {
			ec.getTracer().setCurrentlyProcessed(parentNode);
		}
	}

	ALangObject setVariable(@Nullable final ISourceResolvable node, final ALangObject val) throws EvaluationException {
		if (node == null)
			throw new UncatchableEvaluationException(ec, CmnCnst.Error.NULL_NODE_INTERNAL);
		switch (node.getSourceType()) {
		case ENVIRONMENTAL:
			symbolTable[node.getClosureSource()] = val;
			break;
		case CLOSURE:
			final int source = node.getClosureSource();
			getClosure(source).setObject(source&0x0000FFFF, val);
			break;
		case EXTERNAL_CONTEXT:
		case LIBRARY:
			throw new IllegalVariableAssignmentException(node.getSourceType(), node.getVariableName(), ec);
		case UNRESOLVED:
			throw new UnresolvedVariableSourceException(null, node.getVariableName(), ec);
		default:
			throw new UnhandledEnumException(node.getSourceType(), ec);
		}
		return val;
	}

	private ALangObject[] evaluateChildren(final Node node)
			throws EvaluationException {
		final int len = node.jjtGetNumChildren();
		final ALangObject[] res = new ALangObject[len];
		for (int i = 0; i != len; ++i)
			res[i] = jjtAccept(node, node.jjtGetChild(i));
		return res;
	}

	private IClosure getClosure(final int source) throws EvaluationException {
		IClosure closure = ec.closureStackPeek();
		if (closure == null)
			throw new UncatchableEvaluationException(ec, NullUtil.messageFormat(
					CmnCnst.Error.VARIABLE_WITH_ILLEGAL_SOURCE, Integer.valueOf(source)));
		for (int parent = (source >>> 16); parent --> 0;)
			if ((closure = closure.getParent()) == null)
				throw new UncatchableEvaluationException(ec, NullUtil.messageFormat(
						CmnCnst.Error.VARIABLE_WITH_ILLEGAL_SOURCE, Integer.valueOf(source)));
		return closure;
	}

	private ALangObject evaluatePropertyExpression(final Node parentNode, final int indexOneAfterEnd) throws EvaluationException {
		// Child is an expressions and cannot contain break/clause/return
		// clauses.
		ALangObject res = jjtAccept(parentNode, parentNode.jjtGetChild(0));
		for (int i = 1; i < indexOneAfterEnd; ++i) {
			final Node n = parentNode.jjtGetChild(i);
			switch (n.getSiblingMethod()) {
			case DOT: {
				final String attrDot = jjtAccept(parentNode, n).coerceString(ec).stringValue();
				res = res.evaluateDotAccessor(attrDot, ec);
				break;
			}
			case BRACKET: {
				final ALangObject attrBracket = jjtAccept(parentNode, n);
				res = res.evaluateBracketAccessor(attrBracket, ec);
				break;
			}
			case PARENTHESIS: {
				// Get a function object.
				final FunctionLangObject func = res.coerceFunction(ec);

				// Evaluate function arguments
				final ALangObject[] args = evaluateChildren(n);

				// Evaluate function
				res = func.evaluate(ec, args);
				break;
			}
			// $CASES-OMITTED$
			default:
				throw new UnhandledEnumException(n.getSiblingMethod(), ec);
			}
		}
		return res;
	}

	private ALangObject performAssignment(final Node node, @Nullable final Node child,
			@Nullable final EMethod method, @Nullable ALangObject assignee)
					throws EvaluationException {
		if (child == null)
			throw new UncatchableEvaluationException(ec, CmnCnst.Error.NULL_CHILD_NODE);
		if (method == null)
			throw new UncatchableEvaluationException(ec, CmnCnst.Error.NULL_METHOD);
		switch (child.jjtGetNodeId()) {
		case JJTVARIABLENODE:
			final ASTVariableNode var = (ASTVariableNode) child;
			// Compound assignments (a+=b, etc.) are the same as a=a+b etc.
			// First we need to evaluate the variable (a),
			// then the expression method (+).
			if (method != EMethod.EQUAL)
				assignee = jjtAccept(node, var).evaluateExpressionMethod(method.equalMethod(ec), ec, assignee);
			// Now we can set the variable to its new value.
			if (assignee == null)
				assignee = NullLangObject.getInstance();
			setVariable(var, assignee);
			break;
		case JJTPROPERTYEXPRESSIONNODE:
			final ASTPropertyExpressionNode prop = (ASTPropertyExpressionNode) child;
			final ALangObject res = evaluatePropertyExpression(prop, prop.jjtGetNumChildren() - 1);
			final Node last = prop.jjtGetChild(prop.jjtGetNumChildren()-1);
			switch (last.getSiblingMethod()) {
			case DOT:
				final String attrDot = jjtAccept(prop, last).coerceString(ec).stringValue();
				// Compound assignments (a.b+=c etc.) are the same as a.b=a.b+c
				// etc.
				// First we need to evaluate the last attribute accessor (a.b),
				// then the expression method (+).
				if (method != EMethod.EQUAL)
					assignee = res.evaluateDotAccessor(attrDot, ec)
					.evaluateExpressionMethod(method.equalMethod(ec), ec, assignee);
				// Now we can call the attribute assigner and assign the
				// value.
				if (assignee == null)
					assignee = NullLangObject.getInstance();
				res.executeDotAssigner(attrDot, assignee, ec);
				break;
			case BRACKET:
				final ALangObject attrBracket = jjtAccept(prop, last);
				// Compound assignments (a[b]+=c etc.) are the same as
				// a[b]=a[b]+c etc.
				// First we need to evaluate the last attribute accessor (a[b]),
				// then the expression method (+).
				if (method != EMethod.EQUAL)
					assignee = res.evaluateBracketAccessor(attrBracket, ec)
					.evaluateExpressionMethod(method.equalMethod(ec), ec, assignee);
				if (assignee == null)
					assignee = NullLangObject.getInstance();
				res.executeBracketAssigner(attrBracket, assignee, ec);
				break;
				// $CASES-OMITTED$
			default:
				throw new UnhandledEnumException(last.getSiblingMethod(), ec);
			}
			break;
			// $CASES-OMITTED$
		default:
			throw new UnhandledNodeTypeException(child, ec);
		}
		return assignee;
	}

	private ALangObject performPostUnaryAssignment(final Node node, @Nullable final Node child,
			final EMethod method) throws EvaluationException {
		if (child == null)
			throw new UncatchableEvaluationException(ec, CmnCnst.Error.NULL_CHILD_NODE);
		final ALangObject res, tmp;
		switch (child.jjtGetNodeId()) {
		case JJTVARIABLENODE:
			final ASTVariableNode var = (ASTVariableNode) child;
			// Compound assignments (a+=b, etc.) are the same as a=a+b etc.
			// First we need to evaluate the variable (a),
			// then the expression method (+).
			res = jjtAccept(node, var);
			// Now we can set the variable to its new value.
			setVariable(var, res.evaluateExpressionMethod(method.equalMethod(ec), ec));
			break;
		case JJTPROPERTYEXPRESSIONNODE:
			final ASTPropertyExpressionNode prop = (ASTPropertyExpressionNode) child;
			tmp = evaluatePropertyExpression(prop, prop.jjtGetNumChildren() - 1);
			final Node last = prop.jjtGetChild(prop.jjtGetNumChildren()-1);
			switch (last.getSiblingMethod()) {
			case DOT:
				final String attrDot = jjtAccept(prop, last).coerceString(ec).stringValue();
				// Compound assignments (a.b+=c etc.) are the same as a.b=a.b+c
				// etc.
				// First we need to evaluate the last attribute accessor (a.b),
				// then the expression method (+).
				res = tmp.evaluateDotAccessor(attrDot, ec);
				// Now we can call the attribute assigner and assign the
				// value.
				res.executeDotAssigner(attrDot, res.evaluateExpressionMethod(method.equalMethod(ec), ec), ec);
				break;
			case BRACKET:
				final ALangObject attrBracket = jjtAccept(prop, last);
				// Compound assignments (a[b]+=c etc.) are the same as
				// a[b]=a[b]+c etc.
				// First we need to evaluate the last attribute accessor (a[b]),
				// then the expression method (+).
				res = tmp.evaluateBracketAccessor(attrBracket, ec);
				tmp.executeBracketAssigner(attrBracket, res.evaluateExpressionMethod(method.equalMethod(ec), ec),
						ec);
				break;
				// $CASES-OMITTED$
			default:
				throw new UnhandledEnumException(last.getSiblingMethod(), ec);
			}
			break;
			// $CASES-OMITTED$
		default:
			throw new UnhandledEnumException(child.getSiblingMethod(), ec);
		}
		return res;
	}

	@Override
	public ALangObject visit(@Nonnull final ASTUnaryExpressionNode node) throws EvaluationException {
		// Child must be an expression and cannot be a break/continue/return
		// node.
		if (node.getUnaryMethod().isAssigning())
			return performAssignment(node, node.getFirstChildOrNull(), node.getUnaryMethod(), null);
		if (node.getUnaryMethod() == EMethod.EXCLAMATION)
			return jjtAccept(node, node.getFirstChildOrNull()).coerceBoolean(ec).not();
		return jjtAccept(node, node.getFirstChildOrNull()).evaluateExpressionMethod(node.getUnaryMethod(), ec);
	}

	@Override
	public ALangObject visit(final ASTPostUnaryExpressionNode node) throws EvaluationException {
		// Child must be an expression and cannot be a break/continue/return
		// node.
		if (node.getUnaryMethod().isAssigning())
			return performPostUnaryAssignment(node, node.getFirstChildOrNull(), node.getUnaryMethod());
		return jjtAccept(node, node.getFirstChildOrNull()).evaluateExpressionMethod(node.getUnaryMethod(), ec);
	}

	@Override
	public ALangObject visit(final ASTExpressionNode node) throws EvaluationException {
		// Arguments are expressions which cannot be or contain clause/continue/return
		// clauses.

		// Empty expression node.
		if (node.jjtGetNumChildren() == 0)
			return NullLangObject.getInstance();

		// Binary expression node.
		// Children are expressions and cannot contain break/clause/return
		// clauses.
		ALangObject res = jjtAccept(node, node.getFirstChildOrNull());
		for (int i = 1; i != node.jjtGetNumChildren(); ++i) {
			// Children are expressions and cannot contain break/clause/return
			// clauses.
			final Node arg = node.jjtGetChild(i);
			final EMethod method = arg.getSiblingMethod();
			if (method == EMethod.DOUBLE_AMPERSAND)  {
				final BooleanLangObject lhs = res.coerceBoolean(ec);
				res = !lhs.booleanValue() ? lhs : jjtAccept(node, arg).coerceBoolean(ec);
			}
			else if (method == EMethod.DOUBLE_BAR) {
				final BooleanLangObject lhs = res.coerceBoolean(ec);
				res = lhs.booleanValue() ? lhs : jjtAccept(node, arg).coerceBoolean(ec);
			}
			else
				res = res.evaluateExpressionMethod(method, ec, jjtAccept(node, arg));
		}

		return res;
	}

	@Override
	public ALangObject visit(final ASTPropertyExpressionNode node) throws EvaluationException {
		return evaluatePropertyExpression(node, node.jjtGetNumChildren());
	}

	@Override
	public ALangObject visit(final ASTNumberNode node) throws EvaluationException {
		return NumberLangObject.create(node.getDoubleValue());
	}

	@Override
	public ALangObject visit(final ASTStringNode node) throws EvaluationException {
		// Most common case is no inline expressions.
		if (node.getStringNodeCount() == 1)
			return jjtAccept(node, node.getStringNode(0)).coerceString(ec);
		// Join result of all children.
		// Template literals can contain only expressions and therefore
		//  no return, continue, or break clauses that would cause jumping.
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < node.getStringNodeCount(); ++i)
			sb.append(jjtAccept(node, node.getStringNode(i)).coerceString(ec).stringValue());
		return StringLangObject.create(sb.toString());
	}

	@Override
	public ALangObject visit(final ASTStringCharactersNode node) throws EvaluationException {
		return StringLangObject.create(node.getStringValue());
	}

	@Override
	public ALangObject visit(final ASTArrayNode node) throws EvaluationException {
		final int len = node.jjtGetNumChildren();
		final List<ALangObject> list = new ArrayList<>(len);
		for (int i = 0; i < len; ++i) {
			// Children are expression and cannot be break/clause/return
			// clauses.
			list.add(jjtAccept(node, node.jjtGetChild(i)));
		}
		return ArrayLangObject.create(list);
	}

	@Override
	public ALangObject visit(final ASTHashNode node) throws EvaluationException {
		final int len = node.jjtGetNumChildren();
		final List<ALangObject> list = new ArrayList<>(len);
		for (int i = 0; i < len; ++i) {
			// Children are expressions and cannot contain break/clause/return
			// clauses.
			list.add(jjtAccept(node, node.jjtGetChild(i)));
		}
		return HashLangObject.create(list);
	}

	@Override
	public ALangObject visit(final ASTNullNode node) throws EvaluationException {
		return NullLangObject.getInstance();
	}

	@Override
	public ALangObject visit(final ASTBooleanNode node) throws EvaluationException {
		return BooleanLangObject.create(node.getBooleanValue());
	}

	@Override
	public ALangObject visit(final ASTVariableNode node) throws EvaluationException {
		final String scope = node.getScope();
		switch (node.getSourceType()) {
		case ENVIRONMENTAL: {
			return symbolTable[node.getClosureSource()];
		}
		case LIBRARY: {
			if (scope == null)
				throw new UnresolvedVariableSourceException(null, node.getVariableName(), ec);
			return ec.getLibrary().getVariable(scope, node.getVariableName(), ec);
		}
		case EXTERNAL_CONTEXT: {
			final IExternalContext ex = ec.getExternalContext();
			if (ex == null)
				throw new MissingExternalContextException(ec);
			if (scope == null)
				throw new UnresolvedVariableSourceException(null, node.getVariableName(), ec);
			return ex.fetchScopedVariable(scope, node.getVariableName(), ec);
		}
		case CLOSURE:
			final int source = node.getClosureSource();
			return getClosure(source).getObject(source&0x0000FFFF);
		case UNRESOLVED:
			throw new UnresolvedVariableSourceException(node.getScope(), node.getVariableName(), ec);
		default:
			throw new UnhandledEnumException(node.getSourceType(), ec);
		}
	}

	@Override
	public ALangObject visit(final ASTStatementListNode node) throws EvaluationException {
		ALangObject res = NullLangObject.getInstance();
		for (int i = 0; i < node.jjtGetNumChildren(); ++i) {
			res = jjtAccept(node, node.jjtGetChild(i));
			if (ec.hasJump())
				break;
		}
		return res;
	}

	@Override
	public ALangObject visit(final ASTIfClauseNode node) throws EvaluationException {
		// If branch, no explicit check for mustJump as it returns
		// immediately anyway
		if (jjtAccept(node, node.getConditionNode()).coerceBoolean(ec).booleanValue())
			return jjtAccept(node, node.getIfNode());
		// Else branch, no explicit check for mustJump as it returns
		// immediately anyway
		else if (node.hasElseNode())
			return jjtAccept(node, node.getElseNode());
		// No else
		else
			return NullLangObject.getInstance();
	}

	@Override
	public ALangObject visit(final ASTForLoopNode node) throws EvaluationException {
		ALangObject res = NullLangObject.getInstance();
		if (node.isPlainLoop()) {
			// Plain for loop
			jjtAccept(node, node.getPlainInitializerNode());
			whileloop: while (jjtAccept(node, node.getPlainConditionNode()).coerceBoolean(ec).booleanValue()) {
				res = jjtAccept(node, node.getBodyNode());
				// Handle break, continue, return.
				switch (ec.getJumpType()) {
				case NONE:
					break;
				case BREAK:
					if (!ec.matchesNamedJump(node.getLabel()))
						return res;
					ec.unsetJump();
					break whileloop;
				case CONTINUE:
					if (!ec.matchesNamedJump(node.getLabel()))
						return res;
					ec.unsetJump();
					break;
				case RETURN:
					return res;
				default:
					throw new UncatchableEvaluationException(ec,
							NullUtil.messageFormat(CmnCnst.Error.INVALID_JUMP_TYPE, ec.getJumpType()));
				}
				jjtAccept(node, node.getPlainIncrementNode());
			}
		}
		else {
			// Iterating for loop
			final INonNullIterator<ALangObject> it = jjtAccept(node, node.getEnhancedIteratorNode()).getIterable(ec).iterator();
			forloop: while (it.hasNext()) {
				setVariable(node, it.next());
				res = jjtAccept(node, node.getBodyNode());
				// Handle break, continue, return.
				switch (ec.getJumpType()) {
				case NONE:
					break;
				case BREAK:
					if (!ec.matchesNamedJump(node.getLabel()))
						return res;
					ec.unsetJump();
					break forloop;
				case CONTINUE:
					if (!ec.matchesNamedJump(node.getLabel()))
						return res;
					ec.unsetJump();
					break;
				case RETURN:
					return res;
				default:
					throw new UncatchableEvaluationException(ec,
							NullUtil.messageFormat(CmnCnst.Error.INVALID_JUMP_TYPE, ec.getJumpType()));
				}
			}
		}
		return res;
	}

	@Override
	public ALangObject visit(final ASTWhileLoopNode node) throws EvaluationException {
		ALangObject res = NullLangObject.getInstance();
		whileloop: while (jjtAccept(node, node.getWhileHeaderNode()).coerceBoolean(ec).booleanValue()) {
			res = jjtAccept(node, node.getBodyNode());
			// Handle break, continue, return.
			switch (ec.getJumpType()) {
			case NONE:
				break;
			case BREAK:
				if (!ec.matchesNamedJump(node.getLabel()))
					return res;
				ec.unsetJump();
				break whileloop;
			case CONTINUE:
				if (!ec.matchesNamedJump(node.getLabel()))
					return res;
				ec.unsetJump();
				break;
			case RETURN:
				return res;
			default:
				throw new UncatchableEvaluationException(ec,
						NullUtil.messageFormat(CmnCnst.Error.INVALID_JUMP_TYPE, ec.getJumpType()));
			}
		}
		return res;
	}

	@Override
	public ALangObject visit(final ASTDoWhileLoopNode node) throws EvaluationException {
		ALangObject res = NullLangObject.getInstance();
		doloop: do {
			res = jjtAccept(node, node.getBodyNode());
			// Handle break, continue, return.
			switch (ec.getJumpType()) {
			case NONE:
				break;
			case BREAK:
				if (!ec.matchesNamedJump(node.getLabel()))
					return res;
				ec.unsetJump();
				break doloop;
			case CONTINUE:
				if (!ec.matchesNamedJump(node.getLabel()))
					return res;
				ec.unsetJump();
				break;
			case RETURN:
				return res;
			default:
				throw new UncatchableEvaluationException(ec,
						NullUtil.messageFormat(CmnCnst.Error.INVALID_JUMP_TYPE, ec.getJumpType()));
			}
		} while (jjtAccept(node, node.getDoFooterNode()).coerceBoolean(ec).booleanValue());
		return res;
	}

	@Override
	public ALangObject visit(final ASTTryClauseNode node) throws EvaluationException {
		ALangObject res = NullLangObject.getInstance();
		CatchableEvaluationException exception = null;
		try {
			// Try branch, no explicit check for mustJump as it returns
			// immediately anyway
			res = jjtAccept(node, node.getTryNode());
		}
		catch (final CatchableEvaluationException e) {
			exception = e;
		}
		// If mustJump is true, break/continue/return was the last statement
		// evaluated and no exception could have been thrown.
		if (exception != null) {
			setVariable(node, ExceptionLangObject.create(exception, ec));
			// Catch branch, no explicit check for mustJump as it returns
			// immediately anyway
			res = jjtAccept(node, node.getCatchNode());
		}
		return res;
	}

	@Override
	public ALangObject visit(final ASTSwitchClauseNode node) throws EvaluationException {
		final Node[] children = node.getChildArray();
		ALangObject res = NullLangObject.getInstance();
		boolean matchingCase = false;
		final ALangObject switchValue = jjtAccept(node, children[0]);
		forloop: for (int i = 1; i < children.length; ++i) {
			switch (children[i].getSiblingMethod()) {
			case SWITCHCASE:
				// Case contains an expression and may not contain break,
				// continue, or throw clauses.
				// When there is a matching case already, we skip any further
				// case statements.
				if (!matchingCase)
					if (switchValue.equals(jjtAccept(node, children[i])))
							matchingCase = true;
				break;
			case SWITCHCLAUSE:
				if (matchingCase) {
					res = jjtAccept(node, children[i]);
					// Handle continue, break, return.
					switch (ec.getJumpType()) {
					case NONE:
						break;
					case BREAK:
						if (!ec.matchesNamedJump(node.getLabel()))
							return res;
						ec.unsetJump();
						break forloop;
					case CONTINUE:
						if (!ec.matchesNamedJump(node.getLabel()))
							return res;
						ec.unsetJump();
						break;
					case RETURN:
						return res;
					default:
						throw new UncatchableEvaluationException(ec,
								NullUtil.messageFormat(CmnCnst.Error.INVALID_JUMP_TYPE, ec.getJumpType()));
					}
				}
				break;
			case SWITCHDEFAULT:
				res = jjtAccept(node, children[i]);
				// Handle continue, break, return.
				switch (ec.getJumpType()) {
				case NONE:
					break;
				case BREAK:
					if (!ec.matchesNamedJump(node.getLabel()))
						return res;
					ec.unsetJump();
					break forloop;
				case CONTINUE:
					if (!ec.matchesNamedJump(node.getLabel()))
						return res;
					ec.unsetJump();
					break;
				case RETURN:
					return res;
				default:
					throw new UncatchableEvaluationException(ec,
							NullUtil.messageFormat(CmnCnst.Error.INVALID_JUMP_TYPE, ec.getJumpType()));
				}
				break;
				// $CASES-OMITTED$
			default:
				throw new UnhandledEnumException(children[i].getSiblingMethod(), ec);
			}
		}
		return res;
	}

	@Override
	public ALangObject visit(final ASTExceptionNode node) throws EvaluationException {
		// Child is an expression and cannot contain any break, continue, or
		// return clause.
		final StringLangObject message = node.hasErrorMessage()
				? jjtAccept(node, node.getErrorMessageNode()).coerceString(ec) : StringLangObject.getEmptyInstance();
		return ExceptionLangObject.create(message.stringValue(), ec);
	}

	@Override
	public ALangObject visit(final ASTThrowClauseNode node) throws EvaluationException {
		// Child is an expression and cannot contain any break, continue, or
		// return clause.
		throw jjtAccept(node, node.getThrowNode()).coerceException(ec).exceptionValue();
	}

	@Override
	public ALangObject visit(final ASTBreakClauseNode node) throws EvaluationException {
		ec.setJump(EJump.BREAK, node.getLabel());
		return NullLangObject.getInstance();
	}

	@Override
	public ALangObject visit(final ASTContinueClauseNode node) throws EvaluationException {
		ec.setJump(EJump.CONTINUE, node.getLabel());
		return NullLangObject.getInstance();
	}

	@Override
	public ALangObject visit(final ASTReturnClauseNode node) throws EvaluationException {
		final ALangObject res = node.hasReturn() ? jjtAccept(node, node.getReturnNode()) : NullLangObject.getInstance();
		ec.setJump(EJump.RETURN, null);
		return res;
	}

	@Override
	public ALangObject visit(final ASTLogNode node) throws EvaluationException {
		// Child must be an expression and cannot contain any break, continue,
		// or return clause.
		final StringLangObject message = jjtAccept(node, node.getLogMessageNode()).coerceString(ec);
		node.getLogLevel().log(ec.getLogger(), message.stringValue(), null);
		return message;
	}

	@Override
	public ALangObject visit(final ASTFunctionNode node) throws EvaluationException {
		final IClosure parentClosure = ec.closureStackPeek();
		final FunctionLangObject func = FunctionLangObject.create(new EvaluateVisitorAnonymousFunction(this, node, ec),
				parentClosure, node.getClosureTableSize());
		func.bind(NullLangObject.getInstance(), ec);
		return func;
	}

	@Override
	public ALangObject visit(final ASTFunctionClauseNode node) throws EvaluationException {
		final IClosure parentClosure = ec.closureStackPeek();
		final FunctionLangObject func = FunctionLangObject.create(new EvaluateVisitorNamedFunction(this, node, ec),
				parentClosure, node.getClosureTableSize());
		func.bind(NullLangObject.getInstance(), ec);
		return func;
	}

	@Override
	public ALangObject visit(final ASTIdentifierNameNode node) throws EvaluationException {
		return StringLangObject.create(node.getName());
	}

	@Override
	public ALangObject visit(final ASTWithClauseNode node) throws EvaluationException {
		// Need not do anything as variables are already resolved.
		// Need not check for must jump as we return immediately anyway.
		return jjtAccept(node, node.getBodyNode());
	}

	@Override
	public ALangObject visit(final ASTAssignmentExpressionNode node) throws EvaluationException {
		// Iterate from the end of each assignment pair and assign the rvalue to
		// the lvalue.
		// Child must be an expression and cannot contain break/continue/return.
		ALangObject assignee = jjtAccept(node, node.getAssignValueNode());
		for (int i = node.getAssignableNodeCount(); i-- > 0;) {
			final EMethod method = node.getAssignMethod(i);
			assignee = performAssignment(node, node.getAssignableNode(i), method, assignee);
		}
		return assignee;
	}

	@Override
	public ALangObject visit(final ASTEmptyNode node) throws EvaluationException {
		return this.currentResult;
	}

	@Override
	public ALangObject visit(final ASTLosNode node) throws EvaluationException {
		if (node.isHasClose())
			ec.getEmbedment().outputCode(currentResult.coerceString(ec).stringValue(), ec);
		if (node.isHasText())
			ec.getEmbedment().outputText(node.getText(), ec);
		// if (node.isHasOpen()) {
		// }
		return NullLangObject.getInstance();
	}

	@Override
	public ALangObject visit(final ASTRegexNode node) throws EvaluationException {
		return RegexLangObject.create(node.getPattern());
	}

	@Override
	public ALangObject visit(final ASTTernaryExpressionNode node) throws EvaluationException {
		if (jjtAccept(node, node.getConditionNode()).coerceBoolean(ec).booleanValue()) {
			return jjtAccept(node, node.getIfNode());
		}
		return jjtAccept(node, node.getElseNode());
	}

	@Override
	public ALangObject visit(final ASTParenthesisExpressionNode node) throws EvaluationException {
		return jjtAccept(node, node.getFirstChildOrNull());
	}

	@Override
	public ALangObject visit(final ASTComparisonExpressionNode node) throws EvaluationException {
		// Arguments are expressions which cannot be nor contain clause/continue/return
		// clauses.

		// Empty expression node.
		if (node.isLeaf())
			return NullLangObject.getInstance();

		// Binary expression node.
		// Children are expressions and cannot contain break/clause/return
		// clauses.
		ALangObject res = jjtAccept(node, node.getFirstChildOrNull());
		for (int i = 1; i < node.jjtGetNumChildren(); ++i) {
			// Children are expressions and cannot contain break/clause/return
			// clauses.
			res = BooleanLangObject.create(node.jjtGetChild(i).getSiblingMethod()
					.checkComparison(res.compareTo(jjtAccept(node, node.jjtGetChild(i)))));
		}
		return res;
	}

	@Override
	public ALangObject visit(final ASTEqualExpressionNode node) throws EvaluationException {
		// Arguments are expressions which cannot be clause/continue/return
		// clauses
		final Node[] childrenArray = node.getChildArray();

		// Empty expression node.
		if (childrenArray.length == 0)
			return NullLangObject.getInstance();

		// Binary expression node.
		// Children are expressions and cannot contain break/clause/return
		// clauses.
		ALangObject res = jjtAccept(node, childrenArray[0]);
		for (int i = 1; i != childrenArray.length; ++i) {
			// Children are expressions and cannot contain break/clause/return
			// clauses.
			// !~ =~ == === !== !=
			switch (childrenArray[i].getSiblingMethod()) {
			case DOUBLE_EQUAL:
				res = BooleanLangObject.create(res.equals(jjtAccept(node, childrenArray[i])));
				break;
			case EXCLAMATION_EQUAL:
				res = BooleanLangObject.create(!res.equals(jjtAccept(node, childrenArray[i])));
				break;
			case TRIPLE_EQUAL:
				res = BooleanLangObject.create(res.equalsSameObject(jjtAccept(node, childrenArray[i])));
				break;
			case EXCLAMATION_DOUBLE_EQUAL:
				res = BooleanLangObject.create(!res.equalsSameObject(jjtAccept(node, childrenArray[i])));
				break;
			case EQUAL_TILDE:
				res = res.evaluateExpressionMethod(EMethod.EQUAL_TILDE, ec, jjtAccept(node, childrenArray[i]))
				.coerceBoolean(ec);
				break;
			case EXCLAMATION_TILDE:
				res = res.evaluateExpressionMethod(EMethod.EQUAL_TILDE, ec, jjtAccept(node, childrenArray[i]))
				.coerceBoolean(ec).not();
				break;
				// $CASES-OMITTED$
			default:
				throw new UnhandledEnumException(childrenArray[i].getSiblingMethod(), ec);
			}
		}
		return res;
	}

	@Override
	public ALangObject visit(final ASTVariableDeclarationClauseNode node) throws EvaluationException {
		if (node.hasAssignment())
			return setVariable(node, jjtAccept(node, node.getAssignmentNode()));
		return NullLangObject.getInstance();
	}

	@Override
	public ALangObject visit(final ASTScopeExternalNode node) throws EvaluationException {
		throw new UncatchableEvaluationException(ec,
				NullUtil.messageFormat(CmnCnst.Error.ILLEGAL_SCOPE_DEFINITIONS_AT_EVALUATION, node.getNodeName()));
	}

	@Override
	public ALangObject visit(final ASTScopeManualNode node) throws EvaluationException {
		throw new UncatchableEvaluationException(ec,
				NullUtil.messageFormat(CmnCnst.Error.ILLEGAL_SCOPE_DEFINITIONS_AT_EVALUATION, node.getNodeName()));
	}

	@Override
	public ALangObject visit(final ASTScopeGlobalNode node) throws EvaluationException {
		throw new UncatchableEvaluationException(ec,
				NullUtil.messageFormat(CmnCnst.Error.ILLEGAL_SCOPE_DEFINITIONS_AT_EVALUATION, node.getNodeName()));
	}

	@Override
	public ALangObject visit(final ASTVariableTypeNode node) throws EvaluationException {
		throw new UncatchableEvaluationException(ec,
				NullUtil.messageFormat(CmnCnst.Error.ILLEGAL_VARIABLE_TYPE_AT_EVALUATION, node.toString()));
	}

	@Override
	public ALangObject visit(final ASTFunctionArgumentNode node) throws EvaluationException {
		return NullLangObject.getInstance();
	}

	/**
	 * Evaluates the given node as a complete program or template with the given
	 * context. This method itself may be used by multiple threads, however, the
	 * node and evaluation context passed require external synchronization.
	 * Usually you would create a new evaluation context for each evaluation or
	 * get one from a pool so that it is not shared by multiple thread.
	 * {@link Node} is not muted anymore after parsing finishes (,but the class
	 * is not principally immutable).
	 *
	 * @param node
	 *            Node to evaluate.
	 * @param scopeDefs The definitions for global and manually scoped variables.
	 * @param ec
	 *            Evaluation context to use.
	 * @return The evaluated result.
	 * @throws EvaluationException
	 *             When the code cannot be evaluated.
	 */
	public static ALangObject evaluateCode(final Node node, final IScopeDefinitions scopeDefs, final int symbolTableSize,
			final IEvaluationContext ec) throws EvaluationException {
		final EvaluateVisitor v = new EvaluateVisitor(ec, symbolTableSize);
		final ALangObject res;
		final IExternalContext ex = ec.getExternalContext();
		if (ex != null)
			ex.beginWriting();
		try {
			v.applyScopeDefs(scopeDefs);
			res = node.jjtAccept(v);
		}
		finally {
			if (ex != null)
				ex.finishWriting();
		}
		v.assertNoJumps();
		v.reinit();
		return res;
	}

	private void applyScopeDefs(final IScopeDefinitions scopeDefs) throws EvaluationException {
		applyAll(scopeDefs.getGlobal());
		for (final Collection<IHeaderNode> coll : scopeDefs.getManual().values())
			if (coll != null)
				applyAll(coll);
	}

	private void applyAll(final Collection<IHeaderNode> coll) throws EvaluationException {
		for (final IHeaderNode header : coll)
			setVariable(header, header.getNode().jjtAccept(this));
	}
}