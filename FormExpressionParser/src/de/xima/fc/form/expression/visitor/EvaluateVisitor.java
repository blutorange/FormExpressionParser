package de.xima.fc.form.expression.visitor;

import static de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants.JJTPROPERTYEXPRESSIONNODE;
import static de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants.JJTVARIABLENODE;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Optional;

import de.xima.fc.form.expression.enums.EJump;
import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.enums.EVariableSource;
import de.xima.fc.form.expression.exception.evaluation.BreakClauseException;
import de.xima.fc.form.expression.exception.evaluation.CatchableEvaluationException;
import de.xima.fc.form.expression.exception.evaluation.ContinueClauseException;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.exception.evaluation.IllegalExternalScopeAssignmentException;
import de.xima.fc.form.expression.exception.evaluation.IllegalThisContextException;
import de.xima.fc.form.expression.exception.evaluation.MissingExternalContextException;
import de.xima.fc.form.expression.exception.evaluation.ReturnClauseException;
import de.xima.fc.form.expression.exception.evaluation.UncatchableEvaluationException;
import de.xima.fc.form.expression.exception.evaluation.UnresolvedVariableSourceException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.iface.context.IExternalContext;
import de.xima.fc.form.expression.iface.context.IFunction;
import de.xima.fc.form.expression.iface.context.ILogger;
import de.xima.fc.form.expression.iface.parse.IHeaderNode;
import de.xima.fc.form.expression.iface.parse.IScopeDefinitions;
import de.xima.fc.form.expression.iface.parse.IScopedSourceResolvable;
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
import de.xima.fc.form.expression.node.ASTStringNode;
import de.xima.fc.form.expression.node.ASTSwitchClauseNode;
import de.xima.fc.form.expression.node.ASTTernaryExpressionNode;
import de.xima.fc.form.expression.node.ASTThrowClauseNode;
import de.xima.fc.form.expression.node.ASTTryClauseNode;
import de.xima.fc.form.expression.node.ASTUnaryExpressionNode;
import de.xima.fc.form.expression.node.ASTVariableDeclarationClauseNode;
import de.xima.fc.form.expression.node.ASTVariableNode;
import de.xima.fc.form.expression.node.ASTWhileLoopNode;
import de.xima.fc.form.expression.node.ASTWithClauseNode;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ALangObject.Type;
import de.xima.fc.form.expression.object.ArrayLangObject;
import de.xima.fc.form.expression.object.BooleanLangObject;
import de.xima.fc.form.expression.object.ExceptionLangObject;
import de.xima.fc.form.expression.object.FunctionLangObject;
import de.xima.fc.form.expression.object.HashLangObject;
import de.xima.fc.form.expression.object.NonNullIterator;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.object.RegexLangObject;
import de.xima.fc.form.expression.object.StringLangObject;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class EvaluateVisitor implements IFormExpressionReturnVoidVisitor<ALangObject, EvaluationException> {

	//TODO use utility functions for node, and dont access children array directly
	@Nonnull
	private ALangObject currentResult = NullLangObject.getInstance();
	@Nonnull
	private final IEvaluationContext ec;
	private boolean mustJump;
	private EJump jumpType;
	private String jumpLabel;

	private EvaluateVisitor(@Nonnull final IEvaluationContext ec)
			throws EvaluationException {
		reinit();
		this.ec = ec;
	}

	private void reinit() throws EvaluationException {
		currentResult = NullLangObject.getInstance();
		mustJump = false;
		jumpType = null;
		jumpLabel = null;
	}

	private void assertNoJumps() throws UncatchableEvaluationException {
		if (mustJump) {
			switch (jumpType) {
			case BREAK:
				throw new BreakClauseException(jumpLabel, ec);
			case CONTINUE:
				throw new ContinueClauseException(jumpLabel, ec);
			case RETURN:
				throw new ReturnClauseException(ec);
			default:
				throw new UncatchableEvaluationException(ec,
						NullUtil.format(CmnCnst.Error.INVALID_JUMP_TYPE, jumpType));
			}
		}
	}

	@Nonnull
	private ALangObject jjtAccept(@Nonnull final Node parentNode, @Nullable final Node node,
			@Nonnull final IEvaluationContext ec) throws EvaluationException {
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

	@Nonnull
	private ALangObject setVariable(@Nullable final IScopedSourceResolvable node, @Nonnull final ALangObject val,
			@Nonnull final IEvaluationContext ec) throws EvaluationException {
		if (node == null)
			throw new UncatchableEvaluationException(ec, CmnCnst.Error.NULL_NODE_INTERNAL);
		switch (node.getSource()) {
		case EVariableSource.ID_LIBRARY:
		case EVariableSource.ID_EXTERNAL_CONTEXT:
			throw new IllegalExternalScopeAssignmentException(node.getScope(), node.getVariableName(), ec);
		case EVariableSource.ID_UNRESOLVED:
			throw new UnresolvedVariableSourceException(node.getScope(), node.getVariableName(), ec);
		default:
			ec.getSymbolTable()[node.getSource()].setCurrentObject(val);
			return val;
		}
	}
	
	@Nonnull
	private ALangObject setSimpleVariable(@Nullable final ISourceResolvable node, @Nonnull final ALangObject val,
			@Nonnull final IEvaluationContext ec) throws EvaluationException {
		if (node == null)
			throw new UncatchableEvaluationException(ec, CmnCnst.Error.NULL_NODE_INTERNAL);
		if (node.getSource() < 0)
			throw new UnresolvedVariableSourceException(null, node.getVariableName(), ec);
		ec.getSymbolTable()[node.getSource()].setCurrentObject(val);
		return val;
	}

	@Nonnull
	private ALangObject[] evaluateChildren(@Nonnull final Node node, @Nonnull final IEvaluationContext ec)
			throws EvaluationException {
		final int len = node.jjtGetNumChildren();
		final ALangObject[] res = new ALangObject[len];
		for (int i = 0; i != len; ++i)
			res[i] = jjtAccept(node, node.jjtGetChild(i), ec);
		return res;
	}

	@Nonnull
	private ALangObject evaluatePropertyExpression(@Nonnull final Node parentNode, final int indexOneAfterEnd,
			@Nonnull final IEvaluationContext ec) throws EvaluationException {
		// Child is an expressions and cannot contain break/clause/return
		// clauses.
		@Nonnull
		ALangObject res = jjtAccept(parentNode, parentNode.jjtGetChild(0), ec);
		@Nonnull
		ALangObject thisContext = NullLangObject.getInstance();
		for (int i = 1; i < indexOneAfterEnd; ++i) {
			final Node n = parentNode.jjtGetChild(i);
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

				ec.getTracer().descend(parentNode);
				try {
					if (func.getThisContextType() == Type.NULL)
						thisContext = NullLangObject.getInstance();
					// Evaluate function
					thisContext = res = func.evaluate(ec, thisContext, args);
				}
				finally {
					ec.getTracer().ascend();
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
						throw new EvaluationException(ec, NullUtil.format(CmnCnst.Error.INVALID_JUMP_TYPE, jumpType));
					}
				}
				break;
			// $CASES-OMITTED$
			default:
				throw new UncatchableEvaluationException(ec,
						NullUtil.format(CmnCnst.Error.ILLEGAL_ENUM_PROPERTY_EXPRESSION, n.getSiblingMethod()));
			}
		}
		return res;
	}

	@Nonnull
	private ALangObject performAssignment(@Nonnull final Node node, @Nullable final Node child,
			@Nullable final EMethod method, @Nullable ALangObject assignee, @Nonnull final IEvaluationContext ec)
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
				assignee = jjtAccept(node, var, ec).evaluateExpressionMethod(method.equalMethod(ec), ec, assignee);
			// Now we can set the variable to its new value.
			if (assignee == null)
				assignee = NullLangObject.getInstance();
			setVariable(var, assignee, ec);
			break;
		case JJTPROPERTYEXPRESSIONNODE:
			final ASTPropertyExpressionNode prop = (ASTPropertyExpressionNode) child;
			final ALangObject res = evaluatePropertyExpression(prop, prop.jjtGetNumChildren() - 1, ec);
			final Node last = prop.getLastChildOrNull();
			if (last == null)
				throw new UncatchableEvaluationException(ec,
						NullUtil.format(CmnCnst.Error.ILLEGAL_ENUM_ASSIGNMENT, null, node.getClass().getSimpleName()));
			switch (last.getSiblingMethod()) {
			case DOT:
				final StringLangObject attrDot = jjtAccept(prop, last, ec).coerceString(ec);
				// Compound assignments (a.b+=c etc.) are the same as a.b=a.b+c
				// etc.
				// First we need to evaluate the last attribute accessor (a.b),
				// then the expression method (+).
				if (method != EMethod.EQUAL)
					assignee = res.evaluateAttrAccessor(attrDot, true, ec)
							.evaluateExpressionMethod(method.equalMethod(ec), ec, assignee);
				// Now we can call the attribute assigner and assign the
				// value.
				if (assignee == null)
					assignee = NullLangObject.getInstance();
				res.executeAttrAssigner(attrDot, true, assignee, ec);
				break;
			case BRACKET:
				final ALangObject attrBracket = jjtAccept(prop, last, ec);
				// Compound assignments (a[b]+=c etc.) are the same as
				// a[b]=a[b]+c etc.
				// First we need to evaluate the last attribute accessor (a[b]),
				// then the expression method (+).
				if (method != EMethod.EQUAL)
					assignee = res.evaluateAttrAccessor(attrBracket, false, ec)
							.evaluateExpressionMethod(method.equalMethod(ec), ec, assignee);
				if (assignee == null)
					assignee = NullLangObject.getInstance();
				res.executeAttrAssigner(attrBracket, false, assignee, ec);
				break;
			// $CASES-OMITTED$
			default:
				throw new UncatchableEvaluationException(ec, NullUtil.format(CmnCnst.Error.ILLEGAL_ENUM_ASSIGNMENT,
						last.getSiblingMethod(), node.getClass().getSimpleName()));
			}
			break;
		// $CASES-OMITTED$
		default:
			throw new UncatchableEvaluationException(ec, NullUtil.format(CmnCnst.Error.ILLEGAL_ENUM_ASSIGNMENT,
					child.getSiblingMethod(), node.getClass().getSimpleName()));
		}
		return assignee;
	}

	@Nonnull
	private ALangObject performPostUnaryAssignment(@Nonnull final Node node, @Nullable final Node child,
			@Nonnull final EMethod method, @Nonnull final IEvaluationContext ec) throws EvaluationException {
		if (child == null)
			throw new UncatchableEvaluationException(ec, CmnCnst.Error.NULL_CHILD_NODE);
		final ALangObject res, tmp;
		switch (child.jjtGetNodeId()) {
		case JJTVARIABLENODE:
			final ASTVariableNode var = (ASTVariableNode) child;
			// Compound assignments (a+=b, etc.) are the same as a=a+b etc.
			// First we need to evaluate the variable (a),
			// then the expression method (+).
			res = jjtAccept(node, var, ec);
			// Now we can set the variable to its new value.
			setVariable(var, res.evaluateExpressionMethod(method.equalMethod(ec), ec), ec);
			break;
		case JJTPROPERTYEXPRESSIONNODE:
			final ASTPropertyExpressionNode prop = (ASTPropertyExpressionNode) child;
			tmp = evaluatePropertyExpression(prop, prop.jjtGetNumChildren() - 1, ec);
			final Node last = prop.getLastChildOrNull();
			if (last == null)
				throw new UncatchableEvaluationException(ec,
						NullUtil.format(CmnCnst.Error.ILLEGAL_ENUM_ASSIGNMENT, null, node.getClass().getSimpleName()));
			switch (last.getSiblingMethod()) {
			case DOT:
				final StringLangObject attrDot = jjtAccept(prop, last, ec).coerceString(ec);
				// Compound assignments (a.b+=c etc.) are the same as a.b=a.b+c
				// etc.
				// First we need to evaluate the last attribute accessor (a.b),
				// then the expression method (+).
				res = tmp.evaluateAttrAccessor(attrDot, true, ec);
				// Now we can call the attribute assigner and assign the
				// value.
				res.executeAttrAssigner(attrDot, true, res.evaluateExpressionMethod(method.equalMethod(ec), ec), ec);
				break;
			case BRACKET:
				final ALangObject attrBracket = jjtAccept(prop, last, ec);
				// Compound assignments (a[b]+=c etc.) are the same as
				// a[b]=a[b]+c etc.
				// First we need to evaluate the last attribute accessor (a[b]),
				// then the expression method (+).
				res = tmp.evaluateAttrAccessor(attrBracket, false, ec);
				tmp.executeAttrAssigner(attrBracket, false, res.evaluateExpressionMethod(method.equalMethod(ec), ec),
						ec);
				break;
			// $CASES-OMITTED$
			default:
				throw new UncatchableEvaluationException(ec, NullUtil.format(CmnCnst.Error.ILLEGAL_ENUM_ASSIGNMENT,
						last.getSiblingMethod(), node.getClass().getSimpleName()));
			}
			break;
		// $CASES-OMITTED$
		default:
			throw new UncatchableEvaluationException(ec, NullUtil.format(CmnCnst.Error.ILLEGAL_ENUM_ASSIGNMENT,
					child.getSiblingMethod(), node.getClass().getSimpleName()));
		}
		return res;
	}

	@Override
	public ALangObject visit(@Nonnull final ASTUnaryExpressionNode node) throws EvaluationException {
		// Child must be an expression and cannot be a break/continue/return
		// node.
		if (node.getUnaryMethod().isAssigning())
			return performAssignment(node, node.getFirstChildOrNull(), node.getUnaryMethod(), null, ec);
		if (node.getUnaryMethod() == EMethod.EXCLAMATION)
			return jjtAccept(node, node.getFirstChildOrNull(), ec).coerceBoolean(ec).not();
		return jjtAccept(node, node.getFirstChildOrNull(), ec).evaluateExpressionMethod(node.getUnaryMethod(), ec);
	}

	@Override
	public ALangObject visit(final ASTPostUnaryExpressionNode node) throws EvaluationException {
		// Child must be an expression and cannot be a break/continue/return
		// node.
		if (node.getUnaryMethod().isAssigning())
			return performPostUnaryAssignment(node, node.getFirstChildOrNull(), node.getUnaryMethod(), ec);
		return jjtAccept(node, node.getFirstChildOrNull(), ec).evaluateExpressionMethod(node.getUnaryMethod(), ec);
	}

	@Override
	public ALangObject visit(final ASTExpressionNode node) throws EvaluationException {
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
	public ALangObject visit(final ASTPropertyExpressionNode node) throws EvaluationException {
		return evaluatePropertyExpression(node, node.jjtGetNumChildren(), ec);
	}

	@Override
	public ALangObject visit(final ASTNumberNode node) throws EvaluationException {
		return NumberLangObject.create(node.getDoubleValue());
	}

	@Override
	public ALangObject visit(final ASTStringNode node) throws EvaluationException {
		return StringLangObject.create(node.getStringValue());
	}

	@Override
	public ALangObject visit(final ASTArrayNode node) throws EvaluationException {
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
	public ALangObject visit(final ASTHashNode node) throws EvaluationException {
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
		switch (node.getSource()) {
		case EVariableSource.ID_LIBRARY:
			if (scope == null)
				throw new UnresolvedVariableSourceException(null, node.getVariableName(), ec);
			return ec.getScope().getVariable(scope, node.getVariableName(), ec);
		case EVariableSource.ID_EXTERNAL_CONTEXT:
			final IExternalContext ex = ec.getExternalContext().orNull();
			if (ex == null)
				throw new MissingExternalContextException(ec);
			if (scope == null)
				throw new UnresolvedVariableSourceException(null, node.getVariableName(), ec);
			return ex.fetchScopedVariable(scope, node.getVariableName(), ec);
		case EVariableSource.ID_UNRESOLVED:
			throw new UnresolvedVariableSourceException(node.getScope(), node.getVariableName(), ec);
		default:
			return ec.getSymbolTable()[node.getSource()].getCurrentObject();
		}
	}

	@Override
	public ALangObject visit(final ASTStatementListNode node) throws EvaluationException {
		ALangObject res = NullLangObject.getInstance();
		for (final Node n : node.getChildArray()) {
			res = jjtAccept(node, n, ec);
			if (mustJump)
				break;
		}
		return res;
	}

	@Override
	public ALangObject visit(final ASTIfClauseNode node) throws EvaluationException {
		final Node[] children = node.getChildArray();
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

	@Override
	public ALangObject visit(final ASTForLoopNode node) throws EvaluationException {
		final Node[] children = node.getChildArray();
		ALangObject res = NullLangObject.getInstance();
		if (node.isPlainLoop()) {
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
			final NonNullIterator<ALangObject> it = jjtAccept(node, children[0], ec).getIterable(ec).iterator();
			forloop: while (it.hasNext()) {
				setSimpleVariable(node, it.next(), ec);
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
		return res;
	}

	@Override
	public ALangObject visit(final ASTWhileLoopNode node) throws EvaluationException {
		final Node[] children = node.getChildArray();
		ALangObject res = NullLangObject.getInstance();
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
		return res;
	}

	@Override
	public ALangObject visit(final ASTDoWhileLoopNode node) throws EvaluationException {
		final Node[] children = node.getChildArray();
		ALangObject res = NullLangObject.getInstance();
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
		} while (jjtAccept(node, children[1], ec).coerceBoolean(ec).booleanValue());
		return res;
	}

	@Override
	public ALangObject visit(final ASTTryClauseNode node) throws EvaluationException {
		final Node[] children = node.getChildArray();
		ALangObject res = NullLangObject.getInstance();
		CatchableEvaluationException exception = null;
		try {
			// Try branch, no explicit check for mustJump as it returns
			// immediately anyway
			res = jjtAccept(node, children[0], ec);
		}
		catch (final CatchableEvaluationException e) {
			exception = e;
		}
		// If mustJump is true, break/continue/return was the last statement
		// evaluated and no exception could have been thrown.
		if (exception != null) {
			setSimpleVariable(node, ExceptionLangObject.create(exception, ec), ec);
			// Catch branch, no explicit check for mustJump as it returns
			// immediately anyway
			res = jjtAccept(node, children[1], ec);
		}
		return res;
	}

	@Override
	public ALangObject visit(final ASTSwitchClauseNode node) throws EvaluationException {
		final Node[] children = node.getChildArray();
		ALangObject res = NullLangObject.getInstance();
		boolean matchingCase = false;
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
			// $CASES-OMITTED$
			default:
				throw new UncatchableEvaluationException(ec,
						NullUtil.format(CmnCnst.Error.ILLEGAL_ENUM_SWITCH, children[i].getSiblingMethod()));
			}
		}
		return res;
	}

	@Override
	public ALangObject visit(final ASTExceptionNode node) throws EvaluationException {
		// Child is an expression and cannot contain any break, continue, or
		// return clause.
		final StringLangObject message = jjtAccept(node, node.jjtGetChild(0), ec).coerceString(ec);
		return ExceptionLangObject.create(message.stringValue(), ec);
	}

	@Override
	public ALangObject visit(final ASTThrowClauseNode node) throws EvaluationException {
		// Child is an expression and cannot contain any break, continue, or
		// return clause.
		throw jjtAccept(node, node.jjtGetChild(0), ec).coerceException(ec).exceptionValue();
	}

	@Override
	public ALangObject visit(final ASTBreakClauseNode node) throws EvaluationException {
		jumpLabel = node.getLabel();
		jumpType = EJump.BREAK;
		mustJump = true;
		return NullLangObject.getInstance();
	}

	@Override
	public ALangObject visit(final ASTContinueClauseNode node) throws EvaluationException {
		jumpLabel = node.getLabel();
		jumpType = EJump.CONTINUE;
		mustJump = true;
		return NullLangObject.getInstance();
	}

	@Override
	public ALangObject visit(final ASTReturnClauseNode node) throws EvaluationException {
		jumpLabel = null;
		jumpType = EJump.RETURN;
		mustJump = true;
		return node.jjtGetNumChildren() == 0 ? NullLangObject.getInstance() : jjtAccept(node, node.jjtGetChild(0), ec);
	}

	@Override
	public ALangObject visit(final ASTLogNode node) throws EvaluationException {
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
					NullUtil.format(CmnCnst.Error.ILLEGAL_ENUM_LOGLEVEL, node.getLogLevel()));
		}
		return message;
	}

	@Override
	public ALangObject visit(final ASTFunctionNode node) throws EvaluationException {
		return FunctionLangObject.create(new EvaluateVisitorAnonymousFunction(this, node, ec));
	}

	@Override
	public ALangObject visit(final ASTFunctionClauseNode node) throws EvaluationException {
		final ALangObject func = FunctionLangObject.create(new EvaluateVisitorNamedFunction(this, node, ec));
		return setVariable((ASTVariableNode) node.getFirstChildOrNull(), func, ec);
	}

	@Override
	public ALangObject visit(final ASTIdentifierNameNode node) throws EvaluationException {
		return StringLangObject.create(node.getName());
	}

	@Override
	public ALangObject visit(final ASTWithClauseNode node) throws EvaluationException {
		// Need not do anything as variables are already resolved.
		// Need not check for must jump as we return immediately anyway.
		return jjtAccept(node, node.getBodyNode(), ec);
	}

	@Override
	public ALangObject visit(final ASTAssignmentExpressionNode node) throws EvaluationException {
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
		if (jjtAccept(node, node.jjtGetChild(0), ec).coerceBoolean(ec).booleanValue()) {
			return jjtAccept(node, node.jjtGetChild(1), ec);
		}
		return jjtAccept(node, node.jjtGetChild(2), ec);
	}

	@Override
	public ALangObject visit(final ASTParenthesisExpressionNode node) throws EvaluationException {
		return jjtAccept(node, node.getFirstChildOrNull(), ec);
	}

	@Override
	public ALangObject visit(final ASTComparisonExpressionNode node) throws EvaluationException {
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
		ALangObject res = jjtAccept(node, childrenArray[0], ec);
		for (int i = 1; i != childrenArray.length; ++i) {
			// Children are expressions and cannot contain break/clause/return
			// clauses.
			// !~ =~ == === !== !=
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
				res = res.evaluateExpressionMethod(EMethod.EQUAL_TILDE, ec, jjtAccept(node, childrenArray[i], ec))
						.coerceBoolean(ec);
				break;
			case EXCLAMATION_TILDE:
				res = res.evaluateExpressionMethod(EMethod.EQUAL_TILDE, ec, jjtAccept(node, childrenArray[i], ec))
						.coerceBoolean(ec).not();
				break;
			// $CASES-OMITTED$
			default:
				throw new UncatchableEvaluationException(ec,
						NullUtil.format(CmnCnst.Error.ILLEGAL_ENUM_EQUAL, childrenArray[i].getSiblingMethod()));
			}
		}
		return res;
	}

	@Override
	public ALangObject visit(final ASTVariableDeclarationClauseNode node) throws EvaluationException {
		final Node n = node.getAssignmentNode();
		if (n != null)
			return setSimpleVariable(node, n.jjtAccept(this), ec);
		return NullLangObject.getInstance();
	}

	@Override
	public ALangObject visit(final ASTScopeExternalNode node) throws EvaluationException {
		throw new UncatchableEvaluationException(ec,
				NullUtil.format(CmnCnst.Error.ILLEGAL_SCOPE_DEFINITIONS_AT_EVALUATION, node.getNodeName()));
	}

	@Override
	public ALangObject visit(final ASTScopeManualNode node) throws EvaluationException {
		throw new UncatchableEvaluationException(ec,
				NullUtil.format(CmnCnst.Error.ILLEGAL_SCOPE_DEFINITIONS_AT_EVALUATION, node.getNodeName()));
	}

	@Override
	public ALangObject visit(final ASTScopeGlobalNode node) throws EvaluationException {
		throw new UncatchableEvaluationException(ec,
				NullUtil.format(CmnCnst.Error.ILLEGAL_SCOPE_DEFINITIONS_AT_EVALUATION, node.getNodeName()));
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
	@Nonnull
	public static ALangObject evaluateCode(@Nonnull final Node node, @Nonnull final IScopeDefinitions scopeDefs,
			@Nonnull final IEvaluationContext ec) throws EvaluationException {
		final EvaluateVisitor v = new EvaluateVisitor(ec);
		final ALangObject res;
		final Optional<IExternalContext> ex = ec.getExternalContext();
		if (ex.isPresent())
			ex.get().beginWriting();
		try {
			v.applyScopeDefs(scopeDefs);
			res = node.jjtAccept(v);
		}
		finally {
			if (ex.isPresent())
				ex.get().finishWriting();
		}
		v.assertNoJumps();
		v.reinit();
		return res;
	}

	// TODO create compile time constant check visitor and check whether header var declarations are constant
	private void applyScopeDefs(@Nonnull final IScopeDefinitions scopeDefs) throws EvaluationException {
		applyAll(scopeDefs.getGlobal());
		for (final Collection<IHeaderNode> coll : scopeDefs.getManual().values())
			applyAll(coll);
	}
	
	private void applyAll(final Collection<IHeaderNode> coll) throws EvaluationException {
		for (final IHeaderNode header : coll) {
			if (header.hasNode())
				ec.getSymbolTable()[header.getSource()].setCurrentObject(header.getNode().jjtAccept(this));
		}
	}
}