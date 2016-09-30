package de.xima.fc.form.expression.visitor;

import java.util.ArrayList;
import java.util.List;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IFunction;
import de.xima.fc.form.expression.context.ILogger;
import de.xima.fc.form.expression.enums.EJump;
import de.xima.fc.form.expression.exception.BreakClauseException;
import de.xima.fc.form.expression.exception.CatchableEvaluationException;
import de.xima.fc.form.expression.exception.ContinueClauseException;
import de.xima.fc.form.expression.exception.CustomRuntimeException;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.exception.IllegalThisContextException;
import de.xima.fc.form.expression.exception.UncatchableEvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.node.ASTArrayNode;
import de.xima.fc.form.expression.node.ASTAssignmentExpressionNode;
import de.xima.fc.form.expression.node.ASTBooleanNode;
import de.xima.fc.form.expression.node.ASTBreakClauseNode;
import de.xima.fc.form.expression.node.ASTContinueClauseNode;
import de.xima.fc.form.expression.node.ASTDoWhileLoopNode;
import de.xima.fc.form.expression.node.ASTExceptionNode;
import de.xima.fc.form.expression.node.ASTExpressionNode;
import de.xima.fc.form.expression.node.ASTForLoopNode;
import de.xima.fc.form.expression.node.ASTFunctionNode;
import de.xima.fc.form.expression.node.ASTHashNode;
import de.xima.fc.form.expression.node.ASTIdentifierNameNode;
import de.xima.fc.form.expression.node.ASTIfClauseNode;
import de.xima.fc.form.expression.node.ASTLogNode;
import de.xima.fc.form.expression.node.ASTNullNode;
import de.xima.fc.form.expression.node.ASTNumberNode;
import de.xima.fc.form.expression.node.ASTPropertyExpressionNode;
import de.xima.fc.form.expression.node.ASTReturnClauseNode;
import de.xima.fc.form.expression.node.ASTStatementListNode;
import de.xima.fc.form.expression.node.ASTStringNode;
import de.xima.fc.form.expression.node.ASTSwitchClauseNode;
import de.xima.fc.form.expression.node.ASTThrowClauseNode;
import de.xima.fc.form.expression.node.ASTTryClauseNode;
import de.xima.fc.form.expression.node.ASTUnaryExpressionNode;
import de.xima.fc.form.expression.node.ASTVariableNode;
import de.xima.fc.form.expression.node.ASTWhileLoopNode;
import de.xima.fc.form.expression.node.ASTWithClauseNode;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ArrayLangObject;
import de.xima.fc.form.expression.object.BooleanLangObject;
import de.xima.fc.form.expression.object.ExceptionLangObject;
import de.xima.fc.form.expression.object.FunctionLangObject;
import de.xima.fc.form.expression.object.HashLangObject;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.object.StringLangObject;

public class EvaluateVisitor implements IFormExpressionParserVisitor<ALangObject, IEvaluationContext> {

	private boolean mustJump;
	private EJump jumpType;
	private String jumpLabel;

	public EvaluateVisitor() {
		reinit();
	}

	public void reinit() {
		mustJump = false;
		jumpType = null;
		jumpLabel = null;
	}


	private void nestLocal(final IEvaluationContext ec) {
		ec.setBinding(ec.getBinding().nestLocal());
	}

	private void nest(final IEvaluationContext ec) {
		ec.setBinding(ec.getBinding().nest());
	}

	private void unnest(final IEvaluationContext ec) {
		ec.setBinding(ec.getBinding().unnest());
	}

	private ALangObject[] evaluateChildren(final Node node, final IEvaluationContext ec) {
		final Node[] children = node.getChildArray();
		final ALangObject[] res = new ALangObject[children.length];
		for (int i = 0; i != children.length; ++i) res[i] = children[i].jjtAccept(this, ec);
		return res;
	}

	@Override
	public ALangObject visit(final ASTUnaryExpressionNode node, final IEvaluationContext ec) throws EvaluationException {
		final ALangObject res = node.jjtGetChild(0).jjtAccept(this, ec);
		return res.evaluateExpressionMethod(node.getUnaryMethod(), ec);
	}

	@Override
	public ALangObject visit(final ASTExpressionNode node, final IEvaluationContext ec) throws EvaluationException {
		// Arguments are expressions which cannot be clause/continue/return clauses
		final Node[] childrenArray = node.getChildArray();

		// Empty expression node.
		if (childrenArray.length == 0)
			return NullLangObject.getInstance();

		// Binary expression node.
		ALangObject res = childrenArray[0].jjtAccept(this, ec);
		for (int i = 1; i != childrenArray.length; ++i) {
			final Node arg = childrenArray[i];
			res = res.evaluateExpressionMethod(arg.getSiblingMethod(), ec, arg.jjtAccept(this, ec));
		}

		return res;
	}

	@Override
	public ALangObject visit(final ASTPropertyExpressionNode node, final IEvaluationContext ec) throws EvaluationException {
		final Node[] children = node.getChildArray();
		ALangObject res = children[0].jjtAccept(this, ec);
		ALangObject thisContext = NullLangObject.getInstance();
		for (int i = 1; i < children.length; ++i) {
			final Node n = children[i];
			switch(n.getSiblingMethod()) {
			case DOT:
				thisContext = res;
				final StringLangObject attrDot = n.jjtAccept(this, ec).coerceString(ec);
				res = res.evaluateAttrAccessor(attrDot, true, ec);
				break;
			case BRACKET:
				thisContext = res;
				final ALangObject attrBracket = n.jjtAccept(this, ec);
				res = res.evaluateAttrAccessor(attrBracket, false, ec);
				break;
			case PARENTHESIS:
				// Get a function object.
				final IFunction<ALangObject> func = res.coerceFunction(ec).functionValue();

				// Evaluate function arguments
				final ALangObject[] args = evaluateChildren(n, ec);

				// Check thisContext of the function.
				if (func.getThisContextType() != thisContext.getType())
					throw new IllegalThisContextException(res, func.getThisContextType(), func, ec);

				nestLocal(ec);
				try {
					// Evaluate function
					thisContext = res = func.evaluate(ec, thisContext, args);
				}
				finally {
					unnest(ec);
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
				//$CASES-OMITTED$
			default:
				throw new EvaluationException(ec, "Illegal enum constant " + n.getSiblingMethod()
				+ " at ASTPropertyExpressionNode. This is likely a bug with the parser. Contact support.");
			}
		}
		return res;
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
			// Children are expression and cannot be break/clause/return clauses.
			list.add(n.jjtAccept(this, ec));
		}
		return ArrayLangObject.create(list);
	}

	@Override
	public ALangObject visit(final ASTHashNode node, final IEvaluationContext ec) throws EvaluationException {
		final Node[] childArray = node.getChildArray();
		final List<ALangObject> list = new ArrayList<ALangObject>(childArray.length);
		for (final Node n : childArray) {
			// Children are expressions and cannot contain break/clause/return clauses.
			list.add(n.jjtAccept(this, ec));
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
		return scope != null ? ALangObject.create(ec.getScope().getVariable(scope, node.getName()))
				: ec.getUnqualifiedVariable(node.getName());
	}

	@Override
	public ALangObject visit(final ASTStatementListNode node, final IEvaluationContext ec) throws EvaluationException {
		ALangObject res = NullLangObject.getInstance();
		for (final Node n : node.getChildArray()) {
			res = n.jjtAccept(this, ec);
			if (mustJump) break;
		}
		return res;
	}

	@Override
	public ALangObject visit(final ASTIfClauseNode node, final IEvaluationContext ec) throws EvaluationException {
		final Node[] children = node.getChildArray();
		nest(ec);
		try {
			// If branch, no explicit check for mustJump as it returns immediately anyway
			if (children[0].jjtAccept(this, ec).coerceBoolean(ec).booleanValue())
				return children[1].jjtAccept(this, ec);
			// Else branch, no explicit check for mustJump as it returns immediately anyway
			else if (children.length == 3)
				return children[2].jjtAccept(this, ec);
			// No else
			else
				return NullLangObject.getInstance();
		}
		finally {
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
				whileloop : while (children[1].jjtAccept(this, ec).coerceBoolean(ec).booleanValue()) {
					res = children[3].jjtAccept(this, ec);
					// Handle break, continue, return.
					if (mustJump) {
						if (jumpType == EJump.RETURN || (jumpLabel != null && !jumpLabel.equals(node.getLabel())))
							return res;
						mustJump = false;
						if (jumpType == EJump.BREAK) break whileloop;
					}
					children[2].jjtAccept(this, ec);
				}
			}
			else {
				// Iterating for loop
				forloop : for (final ALangObject value : children[0].jjtAccept(this, ec)) {
					ec.getBinding().setVariable(variableName, value);
					res = children[1].jjtAccept(this, ec);
					// Handle break, continue, return.
					if (mustJump) {
						if (jumpType == EJump.RETURN || (jumpLabel != null && !jumpLabel.equals(node.getLabel())))
							return res;
						mustJump = false;
						if (jumpType == EJump.BREAK) break forloop;
					}
				}
			}
		}
		finally {
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
			whileloop : while (children[0].jjtAccept(this, ec).coerceBoolean(ec).booleanValue()) {
				res = children[1].jjtAccept(this, ec);
				// Handle break, continue, return.
				if (mustJump) {
					if (jumpType == EJump.RETURN || (jumpLabel != null && !jumpLabel.equals(node.getLabel())))
						return res;
					mustJump = false;
					if (jumpType == EJump.BREAK) break whileloop;
				}
			}
		}
		finally {
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
			doloop : do {
				res = children[1].jjtAccept(this, ec);
				// Handle break, continue, return.
				if (mustJump) {
					if (jumpType == EJump.RETURN || (jumpLabel != null && !jumpLabel.equals(node.getLabel())))
						return res;
					mustJump = false;
					if (jumpType == EJump.BREAK) break doloop;
				}
			}
			while (children[0].jjtAccept(this, ec).coerceBoolean(ec).booleanValue());
		}
		finally {
			unnest(ec);
		}
		return res;
	}

	@Override
	public ALangObject visit(final ASTTryClauseNode node, final IEvaluationContext ec) throws EvaluationException {
		final Node[] children = node.getChildArray();
		ALangObject res = NullLangObject.getInstance();
		CatchableEvaluationException exception = null;
		nest(ec);
		try {
			// Try branch, no explicit check for mustJump as it returns immediately anyway
			res = children[0].jjtAccept(this, ec);
		}
		catch (final CatchableEvaluationException e) {
			exception = e;
		}
		finally {
			unnest(ec);
		}
		// If mustJump is true, break/continue/return was the last statement
		// evaluated and no exception could have been thrown.
		if (exception != null) {
			try {
				final ALangObject e = ExceptionLangObject.create(exception);
				ec.getBinding().setVariable(node.getErrorVariableName(), e);
				// Catch branch, no explicit check for mustJump as it returns immediately anyway
				res = children[1].jjtAccept(this, ec);
			}
			finally {
				unnest(ec);
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
		nest(ec);
		try {
			final ALangObject switchValue = children[0].jjtAccept(this, ec);
			for (int i = 1; i < children.length; ++i) {
				switchclause : switch (children[i].getSiblingMethod()) {
				case SWITCHCASE:
					// Case contains an expression and may not contain break, continue, or throw clauses.
					if (!matchingCase && switchValue.equals(children[i - 1].jjtAccept(this, ec))) {
						matchingCase = true;
					}
					break;
				case SWITCHCLAUSE:
					if (matchingCase) {
						res = children[i].jjtAccept(this, ec);
						// Handle continue, break, return.
						if (mustJump) {
							if (jumpType == EJump.RETURN || (jumpLabel != null))
								return res;
							mustJump = false;
							if (jumpType == EJump.BREAK) break switchclause;
						}
					}
					break;
				case SWITCHDEFAULT:
					res = children[i].jjtAccept(this, ec);
					// Handle continue, break, return.
					if (mustJump) {
						if (jumpType == EJump.RETURN || (jumpLabel != null))
							return res;
						mustJump = false;
						if (jumpType == EJump.BREAK) break switchclause;
					}
				default:
					throw new UncatchableEvaluationException(ec, "Invalid switch syntax. This is most likely an error with the parser. Contact support.");
				}
			}
		}
		finally {
			unnest(ec);
		}
		return res;
	}

	@Override
	public ALangObject visit(final ASTExceptionNode node, final IEvaluationContext ec) throws EvaluationException {
		// Child is an expression and cannot contain any break, continue, or return clause.
		final StringLangObject message = node.jjtGetChild(0).jjtAccept(this, ec).coerceString(ec);
		return ExceptionLangObject.create(message.stringValue());
	}

	@Override
	public ALangObject visit(final ASTThrowClauseNode node, final IEvaluationContext ec) throws EvaluationException {
		// Child is an expression and cannot contain any break, continue, or return clause.
		final String message = node.jjtGetChild(0).jjtAccept(this, ec).coerceString(ec).stringValue();
		throw new CustomRuntimeException(message);
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
		return NullLangObject.getInstance();
	}

	@Override
	public ALangObject visit(final ASTLogNode node, final IEvaluationContext ec) throws EvaluationException {
		// Child must be an expression and cannot contain any break, continue, or return clause.
		final StringLangObject message = node.jjtGetChild(0).jjtAccept(this, ec).coerceString(ec);
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
	public ALangObject visit(final ASTIdentifierNameNode node, final IEvaluationContext data) throws EvaluationException {
		return StringLangObject.create(node.getName());
	}
	
	@Override
	public ALangObject visit(final ASTAssignmentExpressionNode node, final IEvaluationContext ec)
			throws EvaluationException {
		// TODO
		return null;
	}

	@Override
	public ALangObject visit(ASTWithClauseNode node, IEvaluationContext ec) throws EvaluationException {
		final Node[] children = node.getChildArray();
		final int len = children.length-1;
		// Set scopes.
		for (int i = 0; i != len; ++i) {
			ec.beginDefaultScope(children[i].jjtAccept(this, ec).coerceString(ec).stringValue());
		}
		// Evaluate block.
		final ALangObject res = children[children.length-1].jjtAccept(this, ec);
		// Remove scopes.
		for (int i = 0; i != len; ++i)
			ec.endDefaultScope();
		return res;
	}
}
