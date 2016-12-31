package de.xima.fc.form.expression.visitor;

import static de.xima.fc.form.expression.visitor.DefiniteAssignmentCheckVisitor.EType.ALWAYS;
import static de.xima.fc.form.expression.visitor.DefiniteAssignmentCheckVisitor.EType.WHEN_FALSE;
import static de.xima.fc.form.expression.visitor.DefiniteAssignmentCheckVisitor.EType.WHEN_TRUE;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Nullable;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.parse.DuplicateLabelException;
import de.xima.fc.form.expression.exception.parse.SemanticsException;
import de.xima.fc.form.expression.exception.parse.UnhandledEnumException;
import de.xima.fc.form.expression.exception.parse.VariableUsedBeforeAssignmentException;
import de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionReturnDataVisitor;
import de.xima.fc.form.expression.iface.parse.IHeaderNode;
import de.xima.fc.form.expression.iface.parse.ILabeled;
import de.xima.fc.form.expression.iface.parse.IScopeDefinitions;
import de.xima.fc.form.expression.iface.parse.ISourceResolvable;
import de.xima.fc.form.expression.iface.parse.IVariableResolutionResult;
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
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

// TODO Some nodes only analyzed when true or false. Does this make sense?
//
// ASTUnaryExpressionNode
//   !() analyzed only when true / false
//
// ASTDoWhileLoopNode
//  DoFooterNode analyzed only when false

/**
 * Rules for the maps passed around:
 * <ul>
 *   <li>Each method may modify the map it gets passed as it pleases.</li>
 *   <li>Each method must return a map with the variables definitely assigned after the node finishes.</li>
 *   <li>This means each method must make a copy of the map it passed to another method if does not wish it to be modified.</li>
 * </ul>
 * @author madgaksha
 *
 */
@NonNullByDefault
public class DefiniteAssignmentCheckVisitor
		implements IFormExpressionReturnDataVisitor<Map<Integer, Object>, Map<Integer, Object>, SemanticsException> {

	private final static Object OBJECT = new Object();

	private final IVariableResolutionResult resolutionResult;

	/** Used to get the most recently requested EType. */
	private final Deque<EType> typeStack;

	/** Saves all definitely assigned variables before a named break. */
	private final Map<String, LabelInfo> labelMap;

	/**
	 * Used to map a break or continue clause without an explicit label to the
	 * most recent occurrence of a labeled statement.
	 */
	private final Deque<LabelInfo> labelStack;

	public DefiniteAssignmentCheckVisitor(final IVariableResolutionResult resolutionResult) {
		this.resolutionResult = resolutionResult;
		typeStack = new ArrayDeque<>();
		labelStack = new ArrayDeque<>();
		labelMap = new HashMap<>();
	}

	private Map<Integer, Object> jjtAccept(final Node node, final Map<Integer, Object> map, final EType type)
			throws SemanticsException {
		// Type is almost always ALWAYS
		// So we check for the most common case
		if (typeStack.peek() == type)
			return node.jjtAccept(this, map);
		typeStack.push(type);
		try {
			return node.jjtAccept(this, map);
		}
		finally {
			typeStack.pop();
		}
	}

	private Map<Integer, Object> jjtAccept(final Node node, final Map<Integer, Object> map)
			throws SemanticsException {
		return node.jjtAccept(this, map);
	}

	/**
	 * <h1><code>r ; s<sub>0</sub> #<sub>1</sub> s<sub>1</sub> #<sub>2</sub> ... #<sub>n</sub> s<sub>n</sub>  ; t</code></h1>
	 * <p><ul>
	 * <li>BEFORE(<sub>0</sub>)  = BEFORE(expression)</li>
	 * <li>BEFORE(s<sub>i</sub>) = BEFORE(s<sub>i-1</sub>), i &#8712; [1...n]</li>
	 * <li>AFTER(expression)     = AFTER(s<sub>n</sub>)</li>
	 * </ul></p>
	 *
	 * <pre>
	 * function foo() {
	 *   var u;
	 *   var v;
	 *   var w;
	 *   var x;
	 *   var y;
	 *   var z;
	 *
	 *   u + v = (w + (x = y = z));
	 *   u && v = (w || (x = y = z));
	 * }
	 * </pre>
	 *
	 * <h1><code>q ; r && s ; t</code></h1>
	 * <p><ul>
	 * <li>BEFORE(r)         = BEFORE(and)</li>
	 * <li>BEFORE(s)         = AFTER(r, true)</li>
	 * <li>AFTER(and, true)  = AFTER(s, true)</li>
	 * <li>AFTER(and, false) = AFTER(r, false) &#8745; AFTER(s, false)</li>
	 * <li>AFTER(and)        = AFTER(and, true) &#8745; AFTER(and, false)</li>
	 * </ul>
	 * </p>
	 *
	 * <pre>
	 * function foo() {
	 *  var v;
	 *  if maybe() && (v=0)
	 *    0;
	 *  else
	 *    v = 0;
	 *  v;
	 * }
	 * </pre>
	 *
	 * <h1><code>q ; r || s ; t</code></h1>
	 * <p><ul>
	 * <li>BEFORE(r)        = BEFORE(or)</li>
	 * <li>BEFORE(s)        = AFTER(r, false)</li>
	 * <li>AFTER(or, false) = AFTER(s, false)</li>
	 * <li>AFTER(or, true)  = AFTER(r, true) &#8745; AFTER(s, true)</li>
	 * <li>AFTER(or)        = AFTER(or, true) &#8745; AFTER(or, false)</li>
	 * </ul>
	 * </p>
	 *
	 * <pre>
	 * function foo() {
	 *  var v;
	 *  if maybe() || (v=0))
	 *    v = 0
	 *  v;
	 *
	 *  var w;
	 *  (maybe() && (w = 0)) || (w = 0);
	 *  w;
	 * }
	 * </pre>
	 */
	@Override
	public Map<Integer, Object> visit(final ASTExpressionNode node, final Map<Integer, Object> map) throws SemanticsException {
		return visitNaryExpressionNode(node, map, node.jjtGetNumChildren());
	}

	//TODO this is not making sense, visit ALL nodes, not only the first two..
	public Map<Integer, Object> visitNaryExpressionNode(final ASTExpressionNode node, Map<Integer, Object> map,
			final int index) throws SemanticsException {
		// Turn
		//   a && b && c && d
		// into a recursion
		//   ((a && b) && c) && d
		switch (index) {
		case 0:
			return map;
		case 1:
			// Keep current EType, this is like a parenthesized expression.
			return jjtAccept(node.jjtGetChild(0), map);
		default:
			final Node rhs = node.jjtGetChild(index-1);
			switch (rhs.getSiblingMethod()) {
			case DOUBLE_AMPERSAND: {
				// BEFORE(r) = BEFORE(and)
				final Map<Integer, Object> afterRFalse;
				final Map<Integer, Object> afterRTrue;
				typeStack.push(WHEN_FALSE);
				try {
					afterRFalse = visitNaryExpressionNode(node, copy(map), index-1);
				}
				finally {
					typeStack.pop();
				}
				typeStack.push(WHEN_TRUE);
				try {
					afterRTrue = visitNaryExpressionNode(node, map, index-1);
				}
				finally {
					typeStack.pop();
				}

				// BEFORE(s) = AFTER(r, true)
				final Map<Integer, Object> afterSFalse = jjtAccept(rhs, copy(afterRTrue), WHEN_FALSE);
				final Map<Integer, Object> afterSTrue = jjtAccept(rhs, afterRTrue, WHEN_TRUE);

				switch (NullUtil.or(typeStack.peek(), ALWAYS)) {
				case ALWAYS:
					// AFTER(and) = AFTER(and, true) ^ AFTER(and, false)
					//            = AFTER(s, true) ^ AFTER(r, false) ^ AFTER(s, false)
					map = intersectToLhs(afterSTrue, afterRFalse, afterSFalse);
					break;
				case WHEN_FALSE:
					// AFTER(and, false) = AFTER(r, false) ^ AFTER(s, false)
					map = intersectToLhs(afterRFalse, afterSFalse);
					break;
				case WHEN_TRUE:
					// AFTER(and, true)  = AFTER(s, true)
					map = afterSTrue;
					break;
				default:
					throw new UnhandledEnumException(NullUtil.or(typeStack.peek(), ALWAYS), rhs);
				}
				break;
			}
			case DOUBLE_BAR: {
				// BEFORE(r) = BEFORE(or)
				final Map<Integer, Object> afterRFalse;
				final Map<Integer, Object> afterRTrue;
				typeStack.push(WHEN_FALSE);
				try {
					afterRFalse = visitNaryExpressionNode(node, copy(map), index-1);
				}
				finally {
					typeStack.pop();
				}
				typeStack.push(WHEN_TRUE);
				try {
					afterRTrue = visitNaryExpressionNode(node, map, index-1);
				}
				finally {
					typeStack.pop();
				}

				// BEFORE(s) = AFTER(r, false)
				final Map<Integer, Object> afterSFalse = jjtAccept(rhs, copy(afterRFalse), WHEN_FALSE);
				final Map<Integer, Object> afterSTrue = jjtAccept(rhs, afterRFalse, WHEN_TRUE);

				switch (NullUtil.or(typeStack.peek(), ALWAYS)) {
				case ALWAYS:
					// AFTER(or) = AFTER(or, true) ^ AFTER(or, false)
					//           = AFTER(r, true) ^ AFTER(s, true) ^ AFTER(s, false)
					map = intersectToLhs(afterSFalse, afterRTrue, afterSTrue);
					break;
				case WHEN_FALSE:
					// AFTER(or, false) = AFTER(s, false)
					map = afterSFalse;
					break;
				case WHEN_TRUE:
					// AFTER(or, true) = AFTER(r, true) ^ AFTER(s, true)
					map = intersectToLhs(afterRTrue, afterSTrue);
					break;
				default:
					throw new UnhandledEnumException(NullUtil.or(typeStack.peek(), ALWAYS), rhs);
				}
				break;
			}
			// Only && and || need to be treated specially
			// $CASES-OMITTED$
			default:
				typeStack.push(ALWAYS);
				try {
					visitNaryExpressionNode(node, map, index-1);
				}
				finally {
					typeStack.pop();
				}
				map = jjtAccept(rhs, map, ALWAYS);
			}
			return map;
		}
	}

	public Map<Integer, Object> visitBinaryExpression(final Node lhs, final Node rhs, Map<Integer, Object> map)
			throws SemanticsException {
		switch (rhs.getSiblingMethod()) {
		case DOUBLE_AMPERSAND: {
			// BEFORE(r) = BEFORE(and)
			final Map<Integer, Object> afterRFalse = jjtAccept(lhs, copy(map), WHEN_FALSE);
			final Map<Integer, Object> afterRTrue = jjtAccept(lhs, map, WHEN_TRUE);

			// BEFORE(s) = AFTER(r, true)
			final Map<Integer, Object> afterSFalse = jjtAccept(rhs, copy(afterRTrue), WHEN_FALSE);
			final Map<Integer, Object> afterSTrue = jjtAccept(rhs, afterRTrue, WHEN_TRUE);

			switch (NullUtil.or(typeStack.peek(), ALWAYS)) {
			case ALWAYS:
				// AFTER(and) = AFTER(and, true) ^ AFTER(and, false)
				//            = AFTER(s, true) ^ AFTER(r, false) ^ AFTER(s, false)
				map = intersectToLhs(afterSTrue, afterRFalse, afterSFalse);
				break;
			case WHEN_FALSE:
				// AFTER(and, false) = AFTER(r, false) ^ AFTER(s, false)
				map = intersectToLhs(afterRFalse, afterSFalse);
				break;
			case WHEN_TRUE:
				// AFTER(and, true)  = AFTER(s, true)
				map = afterSTrue;
				break;
			default:
				throw new UnhandledEnumException(NullUtil.or(typeStack.peek(), ALWAYS), rhs);
			}
			break;
		}
		case DOUBLE_BAR: {
			// BEFORE(r) = BEFORE(or)
			final Map<Integer, Object> afterRFalse = jjtAccept(lhs, copy(map), WHEN_FALSE);
			final Map<Integer, Object> afterRTrue = jjtAccept(lhs, map, WHEN_TRUE);

			// BEFORE(s) = AFTER(r, false)
			final Map<Integer, Object> afterSFalse = jjtAccept(rhs, copy(afterRFalse), WHEN_FALSE);
			final Map<Integer, Object> afterSTrue = jjtAccept(rhs, afterRFalse, WHEN_TRUE);

			switch (NullUtil.or(typeStack.peek(), ALWAYS)) {
			case ALWAYS:
				// AFTER(or) = AFTER(or, true) ^ AFTER(or, false)
				//           = AFTER(r, true) ^ AFTER(s, true) ^ AFTER(s, false)
				map = intersectToLhs(afterSFalse, afterRTrue, afterSTrue);
				break;
			case WHEN_FALSE:
				// AFTER(or, false) = AFTER(s, false)
				map = afterSFalse;
				break;
			case WHEN_TRUE:
				// AFTER(or, true) = AFTER(r, true) ^ AFTER(s, true)
				map = intersectToLhs(afterRTrue, afterSTrue);
				break;
			default:
				throw new UnhandledEnumException(NullUtil.or(typeStack.peek(), ALWAYS), rhs);
			}
			break;
		}
		// Only && and || need to be treated specially
		// $CASES-OMITTED$
		default:
			map = jjtAccept(lhs, map, ALWAYS);
			map = jjtAccept(rhs, map, ALWAYS);
		}
		return map;
	}

	/**
	 * <h1><code>r ; s<sub>1</sub> = s<sub>2</sub> = ... = s<sub>n</sub>; t</code></h1>
	 * <p>
	 * Note that here we mark variables as definitely assigned.
	 * <ul>
	 * <li>BEFORE(s<sub>n</sub>)   = BEFORE(assignment)</li>
	 * <li>BEFORE(s<sub>n-1</sub>) = AFTER(s<sub>n</sub>)</li>
	 * <li>BEFORE(s<sub>i</sub>) = AFTER(s<sub>i+1</sub>) &#8746; {s<sub>i+1</sub>}, i &#8712; [1,n-2]</li>
	 * <li>AFTER(assignment) = AFTER(s<sub>1</sub></li>
	 * </ul></p>
	 * <pre>
	 * function foo() {
	 *   var v;
	 *   0.0;
	 *   v;
	 * }
	 * </pre>
	 *
	 * <h1><code>r ; s<sub>1</sub> += s<sub>2</sub>; t</code></h1>
	 * <p>
	 * When the expression method is assigning, the same rules as above
	 * apply, but we need to check whether the variable on the left-hand
	 * side was already assigned.
	 * </p>
	 * <pre>
	 * function foo() {
	 *   var v;
	 *   v += 0;
	 *   v;
	 * }
	 * </pre>
	 */
	@Override
	public Map<Integer, Object> visit(final ASTAssignmentExpressionNode node, Map<Integer, Object> map) throws SemanticsException {
		map = jjtAccept(node.getAssignValueNode(), map, ALWAYS);
		final int count = node.getAssignableNodeCount();
		for (int i = count; i -->0;) {
			final Node n = node.getAssignableNode(i);
			if (n.jjtGetNodeId() == FormExpressionParserTreeConstants.JJTVARIABLENODE
					&& node.getAssignMethod(i) == EMethod.EQUAL)
				markAssigned(map, (ASTVariableNode)n);
			else
				map = jjtAccept(n, map, ALWAYS);
		}
		return map;
	}

	/**
	 * <h1><code>s ; &lt;number&gt; ; t</code></h1>
	 * <p><ul>
	 * <li>AFTER(number)        = BEFORE(number)</li>
	 * <li>AFTER(number, true)  = BEFORE(number)</li>
	 * <li>AFTER(number, false) = VARS(s)</li>
	 * </ul></p>
	 * <pre>
	 * function foo() {
	 *   var v;
	 *   0.0;
	 *   v;
	 *
	 *   var u;
	 *   if (0)
	 *     u = 0;
	 *   u;
	 * }
	 * </pre>
	 */
	@Override
	public Map<Integer, Object> visit(final ASTNumberNode node, final Map<Integer, Object> map) throws SemanticsException {
		if (typeStack.peek() == WHEN_FALSE)
			markAllAssigned(map);
		return map;
	}

	/**
	 * <h1><code>r ; [s<sub>1</sub>, s<sub>2</sub>, ..., s<sub>n</sub>] ; t</code></h1>
	 * <p><ul>
	 * <li>BEFORE(s<sub>1</sub>) = BEFORE(r)</li>
	 * <li>BEFORE(s<sub>i</sub>) = BEFORE(s<sub>i-1</sub>), i &#8712; [2,n]</li>
	 * <li>AFTER(array)          = AFTER(s<sub>n</sub>)</li>
	 * <li>AFTER(array, true)    = AFTER(s<sub>n</sub>)</li>
	 * <li>AFTER(array, false)   = VARS(r)</li>
	 * </ul></p>
	 * <pre>
	 * function foo() {
	 *   var v;
	 *   var u;
	 *   [v = 0, 2, u = 0];
	 *   v;
	 *   u;
	 * }
	 * </pre>
	 */
	@Override
	public Map<Integer, Object> visit(final ASTArrayNode node, Map<Integer, Object> map) throws SemanticsException {
		final int count = node.jjtGetNumChildren();
		for (int i = 0; i < count; ++i)
			map = jjtAccept(node.jjtGetChild(i), map, ALWAYS);
		if (typeStack.peek() == WHEN_FALSE)
			markAllAssigned(map);
		return map;
	}

	/**
	 * <h1><code>r ; {k<sub>1</sub>: v<sub>1</sub>, k<sub>2</sub>: v<sub>2</sub>, ..., k<sub>n</sub>: v<sub>n</sub>} ; t</code></h1>
	 * <p><ul>
	 * <li>BEFORE(k<sub>1</sub>) = BEFORE(r)</li>
	 * <li>BEFORE(k<sub>i</sub>) = BEFORE(v<sub>i-1</sub>), i &#8712; [2,n]</li>
	 * <li>BEFORE(v<sub>i</sub>) = BEFORE(k<sub>i</sub>), i &#8712; [1,n]</li>
	 * <li>AFTER(hash)           = AFTER(v<sub>n</sub>)</li>
	 * <li>AFTER(hash, true)     = AFTER(v<sub>n</sub>)</li>
	 * <li>AFTER(hash, false)    = VARS(r)</li>
	 * </ul></p>
	 * <pre>
	 * function foo() {
	 *   var v;
	 *   var u;
	 *   [v = 0, 2, u = 0];
	 *   v;
	 *   u;
	 * }
	 * </pre>
	 */
	@Override
	public Map<Integer, Object> visit(final ASTHashNode node, Map<Integer, Object> map) throws SemanticsException {
		final int count = node.jjtGetNumChildren();
		for (int i = 0; i < count; ++i)
			map = jjtAccept(node.jjtGetChild(i), map, ALWAYS);
		if (typeStack.peek() == WHEN_FALSE)
			markAllAssigned(map);
		return map;
	}

	/**
	 * <h1><code>s ; &lt;null&gt; ; t</code></h1>
	 * <p><ul>
	 * <li>AFTER(null)        = BEFORE(null)</li>
	 * <li>AFTER(null, true)  = VARS(s)</li>
	 * <li>AFTER(null, false) = BEFORE(null)</li>
	 * </ul></p>
	 * <pre>
	 * function foo() {
	 *   var v;
	 *   null;
	 *   v;
	 * }
	 * </pre>
	 */
	@Override
	public Map<Integer, Object> visit(final ASTNullNode node, final Map<Integer, Object> map) throws SemanticsException {
		if (typeStack.peek() == WHEN_TRUE)
			markAllAssigned(map);
		return map;
	}

	/**
	 * <h1><code>s ; true ; t</code></h1>
	 * <p><ul>
	 * <li>AFTER(true, true)  = BEFORE(true)</li>
	 * <li>AFTER(true, false) = VARS(s)</li>
	 * </ul></p>
	 * <pre>
	 * function foo() {
	 *   var v;
	 *   false;
	 *   v;
	 * }
	 * </pre>
	 * <h1><code>s ; false ; t</code></h1>
	 * <p><ul>
	 * <li>AFTER(false, true)  = VARS(s)</li>
	 * <li>AFTER(false, false) = BEFORE(false)</li>
	 * </ul></p>
	 * <pre>
	 * function foo() {
	 *   var v;
	 *   if (!false)
	 *     v = 0;
	 *   v;
	 * }
	 * </pre>
	 */
	@Override
	public Map<Integer, Object> visit(final ASTBooleanNode node, final Map<Integer, Object> map) throws SemanticsException {
		if (NullUtil.or(typeStack.peek(), ALWAYS).isOppositeOf(node.getBooleanValue()))
				markAllAssigned(map);
		return map;
	}

	/**
	 * <h1><code>s ; &lt;variable&gt; ; t</code></h1>
	 * <p>
	 * Note that here we check if the variable has already been
	 * assigned, and throw an error if not.
	 * <ul>
	 * <li>AFTER(variable) = BEFORE(variable)</li>
	 * </ul></p>
	 * <pre>
	 * function foo() {
	 *   var v;
	 *   v;
	 * }
	 * </pre>
	 */
	@Override
	public Map<Integer, Object> visit(final ASTVariableNode node, final Map<Integer, Object> map) throws SemanticsException {
		final Integer variableId = Integer.valueOf(node.getBasicSource());
		if (!resolutionResult.containsBasicSourcedGlobalVariable(variableId)
				&& map.get(Integer.valueOf(node.getBasicSource())) == null)
			throw new VariableUsedBeforeAssignmentException(node.getVariableName(), node);
		return map;
	}

	/**
	 * <h1><code>r ; "..." ; t</code></h1>
	 * <h1><code>r ; '...' ; t</code></h1>
	 * <p><ul>
	 * <li>AFTER(string)        = BEFORE(string)</li>
	 * <li>AFTER(string, true)  = BEFORE(string)</li>
	 * <li>AFTER(string, false) = VARS(r)</li>
	 * </ul></p>
	 * <pre>
	 * function foo() {
	 *   var v;
	 *   "";
	 *   u;
	 * }
	 * </pre>
	 * <h1><code>r ; `...${s<sub>1</sub>}...${<sub>2</sub>}......${<sub>n</sub>}` ; t</code></h1>
	 * <p><ul>
	 * <li>BEFORE(s<sub>1</sub> = BEFORE(string)</li>
	 * <li>BEFORE(s<sub>i</sub> = BEFORE(s<sub>i-1</sub>), i &#8712; [2,n]</li>
	 * <li>AFTER(string) = AFTER(s<sub>n</sub>)</li>
	 * <li>AFTER(string, true)  = BEFORE(string)</li>
	 * <li>AFTER(string, false) = VARS(r)</li>
	 * </ul></p>
	 * <pre>
	 * function foo() {
	 *   var v;
	 *   "";
	 *   u;
	 * }
	 * </pre>
	 */
	@Override
	public Map<Integer, Object> visit(final ASTStringNode node, Map<Integer, Object> map) throws SemanticsException {
		final int count = node.getStringNodeCount();
		for (int i = 0; i < count; ++i)
			map = jjtAccept(node.getStringNode(i), map, ALWAYS);
		if (typeStack.peek() == WHEN_FALSE)
			markAllAssigned(map);
		return map;
	}

	/**
	 * <h1><code>r ; s<sub>1</sub> ; s<sub>2</sub> ; ... ; s<sub>n</sub> ; t</code></h1>
	 * <p><ul>
	 * <li>BEFORE(s<sub>1</sub>)        = BEFORE(statement_list)</li>
	 * <li>BEFORE(s<sub>i</sub>)        = AFTER(s<sub>i-1</sub>), i &#8712; [2,n]</li>
	 * <li>AFTER(statement_list)        = AFTER(s<sub>n</sub>)</li>
	 * <li>AFTER(statement_list, true)  = AFTER(s<sub>n</sub>, true)</li>
	 * <li>AFTER(statement_list, false) = AFTER(s<sub>n</sub>, false)</li>
	 * </ul></p>
	 * <pre>
	 * function foo() {
	 *   var v;
	 *   0;
	 *   v = 0;
	 *   0;
	 *   v;
	 * }
	 * </pre>
	 */
	@Override
	public Map<Integer, Object> visit(final ASTStatementListNode node, Map<Integer, Object> map) throws SemanticsException {
		final int count = node.jjtGetNumChildren() - 1;
		for (int i = 0; i < count; ++i)
			map = jjtAccept(node.jjtGetChild(i), map, ALWAYS);
		if (count >= 0)
			map = jjtAccept(node.jjtGetChild(count), map);
		return map;
	}

	/**
	 * <h1><code>r ; if(s) t ; u</code></h1>
	 * <p><ul>
	 * <li>BEFORE(s)  = BEFORE(if)</li>
	 * <li>BEFORE(t)  = AFTER(s, true)</li>
	 * <li>AFTER(if)  = AFTER(T) &#8745; AFTER(s, false)</li>
	 * </ul></p>
	 * <pre>
	 * function foo() {
	 *   var v;
	 *   if (maybe() || (v=0))
	 *     v = 0;
	 *   v;
	 *
	 *   var w;
	 *   if (maybe() && (w=0))
	 *     w;
	 * }
	 * </pre>
	 *
	 * <h1><code>p ; if(q) r ; else s ; t</code></h1>
	 * <p><ul>
	 * <li>BEFORE(q) = BEFORE(if)</li>
	 * <li>BEFORE(r) = AFTER(q, true)</li>
	 * <li>BEFORE(s) = AFTER(q, false)</li>
	 * <li>AFTER(if) = AFTER(r) &#8745; AFTER(s)</li>
	 * <li></li>
	 * </ul></p>
	 * <pre>
	 * function foo() {
	 *   var v;
	 *   if (maybe() || (v=0))
	 *     v = 0;
	 *   else
	 *     0;
	 *   v;
	 *
	 *   var w;
	 *   if (maybe() && (w=0))
	 *     w;
	 *   else
	 *     w = 0;
	 *   w;
	 * }
	 * </pre>
	 */
	@Override
	public Map<Integer, Object> visit(final ASTIfClauseNode node, final Map<Integer, Object> map) throws SemanticsException {
		if (node.hasElseNode()) {
			// BEFORE(q) = BEFORE(if)
			// BEFORE(r) = AFTER(q, true)
			// BEFORE(s) = AFTER(q, false)
			// AFTER(if) = AFTER(r) ^ AFTER(s)
			final Map<Integer, Object> afterQTrue = jjtAccept(node.getConditionNode(), copy(map), WHEN_TRUE);
			final Map<Integer, Object> afterQFalse = jjtAccept(node.getConditionNode(), map, WHEN_FALSE);
			final Map<Integer, Object> afterR = jjtAccept(node.getIfNode(), afterQTrue, ALWAYS);
			final Map<Integer, Object> afterS = jjtAccept(node.getElseNode(), afterQFalse, ALWAYS);
			return intersectToLhs(afterR, afterS);
		}
		// BEFORE(s)  = BEFORE(if)
		// BEFORE(t)  = AFTER(s, true)
		final Map<Integer, Object> afterSTrue = jjtAccept(node.getConditionNode(), copy(map), WHEN_TRUE);
		final Map<Integer, Object> afterSFalse = jjtAccept(node.getConditionNode(), map, WHEN_FALSE);
		final Map<Integer, Object> afterT = jjtAccept(node.getIfNode(), afterSTrue, ALWAYS);
		// AFTER(if)  = AFTER(T) ^ AFTER(s, false)
		return intersectToLhs(afterT, afterSFalse);
	}

	/**
	 * <h1><code>p ; for ( q ; r ; s ) { t } ; u</code></h1>
	 * <p><ul>
	 * <li>BEFORE(q)  = BEFORE(for)</li>
	 * <li>BEFORE(r)  = AFTER(q)</li>
	 * <li>BEFORE(t)  = AFTER(r, true)</li>
	 * <li>BEFORE(s)  = AFTER(t) &#8745; CONTINUE(t, label)</li>
	 * <li>AFTER(for) = BREAK(t, label) &#8745; AFTER(r, false)</li>
	 * </ul></p>
	 * <pre>
	 * function foo() {
	 *   var v;
	 *   for ( ; maybe()  || (v=0) ;)
	 *     v = 0;
	 *   v;
	 * }
	 * </pre>
	 *
	 * <h1><code>p ; for ( q ; ; s ) { t } ; u</code></h1>
	 * <p><ul>
	 * <li>BEFORE(q)  = BEFORE(for)</li>
	 * <li>BEFORE(t)  = AFTER(q)</li>
	 * <li>BEFORE(s)  = AFTER(t) &#8745; CONTINUE(t, label)</li>
	 * <li>AFTER(for) = BREAK(t, label)</li>
	 * </ul></p>
	 * <pre>
	 * function foo() {
	 *   var v;
	 *   for (;;)
	 *     if (maybe() && (v=0))
	 *       break;
	 *   v;
	 * }
	 *
	 * </pre>
	 * <h1><code>r ; for ( &lt;variable&gt; in s ) { t } ; u</code></h1>
	 * <p><ul>
	 * <li>BEFORE(s)  = BEFORE(for)</li>
	 * <li>BEFORE(t)  = AFTER(s) &#8746; {&lt;variable&gt;}</li>
	 * <li>AFTER(for) = AFTER(s)</li>
	 * </ul></p>
	 * <pre>
	 * function foo() {
	 *   var v;
	 *   for ( i in (v=10));
	 *   v;
	 *
	 *   var u;
	 *   for ( i in 10 )
	 *     u = 0;
	 *   u;
	 * }
	 * </pre>
	 */
	@Override
	public Map<Integer, Object> visit(final ASTForLoopNode node, final Map<Integer, Object> map) throws SemanticsException {
		if (node.isEnhancedLoop()) {
			// BEFORE(s) = BEFORE(for)
			final Map<Integer, Object> afterS = jjtAccept(node.getEnhancedIteratorNode(), map, ALWAYS);

			// BEFORE(t) = AFTER(s) &#8746; {&lt;variable&gt;}
			final Map<Integer, Object> beforeT = markAssigned(copy(afterS), node);
			jjtAccept(node.getBodyNode(), beforeT, ALWAYS);

			// AFTER(for) = AFTER(s)
			return afterS;
		}

		if (node.hasCondition()) {
			// BEFORE(q) = BEFORE(for)
			final Map<Integer, Object> afterQ = jjtAccept(node.getPlainInitializerNode(), map, ALWAYS);

			// BEFORE(r) = AFTER(q)
			final Map<Integer, Object> afterRTrue = jjtAccept(node.getPlainConditionNode(), copy(afterQ), WHEN_TRUE);
			final Map<Integer, Object> afterRFalse = jjtAccept(node.getPlainConditionNode(), afterQ, WHEN_FALSE);

			// BEFORE(t) = AFTER(r, true)
			final LabelInfo infoT = getInfoBoth(node.getBodyNode(), afterRTrue, ALWAYS, node);
			final Map<Integer, Object> afterT = infoT.getMap();
			final Map<Integer, Object> breakT = infoT.getBreakMap();
			final Map<Integer, Object> continueT = infoT.getContinueMap();

			// BEFORE(s) = AFTER(t) ^ CONTINUE(t, label)
			final Map<Integer, Object> beforeS = intersectToLhs(afterT, continueT);
			jjtAccept(node.getPlainIncrementNode(), beforeS, ALWAYS);

			// AFTER(for) = BREAK(t, label) ^ AFTER(r, false)
			return intersectToLhs(breakT, afterRFalse);
		}

		// BEFORE(q) = BEFORE(for)
		final Map<Integer, Object> afterQ = jjtAccept(node.getPlainInitializerNode(), map, ALWAYS);

		// BEFORE(t) = AFTER(q)
		final LabelInfo infoT = getInfoBoth(node.getBodyNode(), afterQ, ALWAYS, node);
		final Map<Integer, Object> afterT = infoT.getMap();
		final Map<Integer, Object> breakT = infoT.getBreakMap();
		final Map<Integer, Object> continueT = infoT.getContinueMap();

		// BEFORE(s) = AFTER(t) ^ CONTINUE(t, label)
		final Map<Integer, Object> beforeS = intersectToLhs(afterT, continueT);
		jjtAccept(node.getPlainIncrementNode(), beforeS, ALWAYS);

		// AFTER(for) = BREAK(t, label)
		return breakT;
	}

	/**
	 * <h1><code>r ; label:while (s) t ; u</code></h1>
	 * <p>
	 * As we are only concerned with whether a variable was definitely
	 * assigned, we do not need to consider what happens when the loop
	 * runs multiple times. Variables cannot be unassigned.
	 * <ul>
	 * <li>BEFORE(s)    = BEFORE(while)</li>
	 * <li>BEFORE(t)    = AFTER(s, true)</li>
	 * <li>AFTER(while) = BREAK(t, label) &#8745; AFTER(s, false)</li>
	 * </ul></p>
	 * <pre>
	 * function foo() {
	 *   var t = 0;
	 *   var v;
	 *   while (maybe(t) || (v=0))
	 *     v = 0;
	 *   v;
	 *
	 *   var w;
	 *   while (maybe(t) && (w=0))
	 *     w;
	 * }
	 *
	 * function bar() {
	 *   var v;
	 *   while (maybe() || (v=0)) {
	 *     if (maybe())
	 *       break;
	 *     v = 0;
	 *   }
	 *   v;
	 *
	 *   var u;
	 *   var w;
	 *   while&lt;outer&gt; (maybe() || (u = w = 0)) {
	 *     while&lt;inner&gt; (maybe() || (u = w = 0)) {
	 *       break inner;
	 *       u = 0;
	 *     }
	 *     w = 0;
	 *   }
	 *   u;
	 *   w;
	 *
	 *   var x;
	 *   while&lt;outer&gt; (maybe() || (x = 0)) {
	 *     while&lt;inner&gt; (maybe() || (x = 0)) {
	 *       break outer;
	 *     }
	 *     x = 0;
	 *   }
	 *   x;
	 * </pre>
	 */
	@Override
	public Map<Integer, Object> visit(final ASTWhileLoopNode node, final Map<Integer, Object> map) throws SemanticsException {
		// BEFORE(s)    = BEFORE(while)
		final Map<Integer,Object> afterSTrue = jjtAccept(node.getWhileHeaderNode(), copy(map), WHEN_TRUE);
		final Map<Integer,Object> afterSFalse = jjtAccept(node.getWhileHeaderNode(), map, WHEN_FALSE);

		// BEFORE(t)    = AFTER(s, true)
		final Map<Integer, Object> afterT = getInfoBreak(node.getBodyNode(), afterSTrue, ALWAYS, node);

		// AFTER(while) = BREAK(t, label) ^ AFTER(s, false)
		return intersectToLhs(afterT, afterSFalse);
	}

	/**
	 * <h1><code>r ; do { s } while ( t ) ; u </code></h1>
	 * <p><ul>
	 * <li>BEFORE(s) = BEFORE(do)</li>
	 * <li>BEFORE(t) = AFTER(s) &#8745; CONTINUE(s, label)</li>
	 * <li>AFTER(do) = AFTER(t, false) &#8745; BREAK(s, label)</li>
	 * </ul></p>
	 * <pre>
	 * function foo() {
	 *   var v;
	 *   do {
	 *     if (maybe())
	 *       continue;
	 *     v = 0;
	 *   } while(v);
	 *
	 *   var u;
	 *   do {
	 *     if (maybe() && (u = 0))
	 *       continue;
	 *   } while(u);
	 *
	 *   var w;
	 *   do {
	 *     break;
	 *   } while(maybe() || (w=0));
	 *   w;
	 *
	 *   var x;
	 *   do {
	 *     break;
	 *     x = 0;
	 *   } while(maybe());
	 *   x;
	 * }
	 * </pre>
	 */
	@Override
	public Map<Integer, Object> visit(final ASTDoWhileLoopNode node, final Map<Integer, Object> map) throws SemanticsException {
		final LabelInfo labelS = this.getInfoBoth(node.getBodyNode(), map, ALWAYS, node);
		final Map<Integer, Object> afterS = labelS.getMap();
		final Map<Integer, Object> breakS = labelS.getBreakMap();
		final Map<Integer, Object> continueS = labelS.getContinueMap();
		final Map<Integer, Object> beforeT = intersectToLhs(continueS, afterS);
		jjtAccept(node.getDoFooterNode(), copy(beforeT), WHEN_TRUE);
		final Map<Integer, Object> afterTFalse = jjtAccept(node.getDoFooterNode(), beforeT, WHEN_FALSE);
		return intersectToLhs(afterTFalse, breakS);
	}

	/**
	 * <h1><code>r ; try { s } catch (exception) { t } ; u </code></h1>
	 * <p><ul>
	 * <li>BEFORE(s) = BEFORE(try)</li>
	 * <li>BEFORE(t) = BEFORE(try) &#8746; {exception}</li>
	 * <li>AFTER(try) = AFTER(s) &#8745; AFTER(t)</li>
	 * </ul></p>
	 * <pre>
	 * function foo() {
	 *   var v;
	 *   try {
	 *     throw exception(v=0);
	 *   }
	 *   catch (e) {
	 *     v = 0;
	 *   }
	 *
	 *   var u;
	 *   try {
	 *     v = 0;
	 *   }
	 *   catch (exception) {
	 *     v = 0;
	 *   }
	 * }
	 * </pre>
	 */
	@Override
	public Map<Integer, Object> visit(final ASTTryClauseNode node, final Map<Integer, Object> map) throws SemanticsException {
		final Map<Integer, Object> afterS = jjtAccept(node.getTryNode(), copy(map), ALWAYS);
		// Add exception variable.
		markAssigned(map, node);
		final Map<Integer, Object> afterT = jjtAccept(node.getTryNode(), map, ALWAYS);
		return intersectToLhs(afterS, afterT);
	}

	//TODO Java does not allow expressions for case(...), I do. Update these rules...
	/**
	 * <h1><code>p ; switch ( q ) case r<sub>1</sub> : s<sub>1</sub> ... case r<sub>n</sub> : s<sub>n</sub> ; default: t ; u </code></h1>
	 * <p><ul>
	 * <li>BEFORE(q) = BEFORE(switch)</li>
	 * <li>BEFORE(s<sub>1</sub>) = AFTER(q)</li>
	 * <li>BEFORE(s<sub>i</sub>) = AFTER(q) &#8745; AFTER(s<sub>i-1</sub>), i &#8712; [2,n]</li>
	 * <li></li>
	 * <li>AFTER(switch) </li>
	 * <li>&#8745;</li>
	 * </ul></p>
	 * <pre>
	 * function foo() {
	 *   var v;
	 *   try {
	 *     throw exception(v=0);
	 *   }
	 *   catch (e) {
	 *     v = 0;
	 *   }
	 *
	 *   var u;
	 *   try {
	 *     v = 0;
	 *   }
	 *   catch (exception) {
	 *     v = 0;
	 *   }
	 * }
	 * </pre>
	 */
	@Override
	public Map<Integer, Object> visit(final ASTSwitchClauseNode node, final Map<Integer, Object> map) throws SemanticsException {
		// TODO Auto-generated method stub
	}

	/**
	 * <h1><code>r ; exception(s) ; t </code></h1>
	 * <p><ul>
	 * <li>BEFORE(s)               = BEFORE(exception)</li>
	 * <li>AFTER(exception)        = AFTER(s)</li>
	 * <li>AFTER(exception, true)  = AFTER(s)</li>
	 * <li>AFTER(exception, false) = VARS(r)</li>
	 * </ul></p>
	 * <pre>
	 * function foo() {
	 *   var v;
	 *   exception(v=0);
	 *   v;
	 *
	 *   var u;
	 *   if (exception())
	 *     u = 0;
	 *   u;
	 * }
	 * </pre>
	 *
	 * <h1><code>s ; exception() ; t </code></h1>
	 * <p><ul>
	 * <li>AFTER(exception)        = BEFORE(exception)</li>
	 * <li>AFTER(exception, true)  = AFTER(s)</li>
	 * <li>AFTER(exception, false) = VARS(r)</li>
	 * </ul></p>
	 * <pre>
	 * function foo() {
	 *   var v;
	 *   exception();
	 *   v;
	 * }
	 * </pre>
	 */
	@Override
	public Map<Integer, Object> visit(final ASTExceptionNode node, Map<Integer, Object> map) throws SemanticsException {
		if (node.hasErrorMessage())
			map = jjtAccept(node.getErrorMessageNode(), map, ALWAYS);
		if (typeStack.peek() == WHEN_FALSE)
			markAllAssigned(map);
		return map;
	}

	/**
	 * <h1><code>r ; throw s ; t</code></h1>
	 * <p><ul>
	 * <li>BEFORE(s)    = BEFORE(throw)</li>
	 * <li>AFTER(throw) = VARS(r)</li>
	 * </ul></p>
	 * <pre>
	 * function foo() {
	 *   try {
	 *     var v;
	 *     if (true)
	 *       throw exception();
	 *     else
	 *       v = 0;
	 *     v;
	 *   }
	 * }
	 * </pre>
	 */
	@Override
	public Map<Integer, Object> visit(final ASTThrowClauseNode node, final Map<Integer, Object> map) throws SemanticsException {
		// BEFORE(s)    = BEFORE(throw)
		jjtAccept(node.getThrowNode(), map, ALWAYS);
		// AFTER(throw) = VARS(r)
		return markAllAssigned(map);
	}

	/**
	 * <h1><code>r ; break [&lt;label&gt;] ; t</code></h1>
	 * <p><ul>
	 * <li>AFTER(break) = VARS(r)</li>
	 * </ul></p>
	 * <pre>
	 * function foo() {
	 *   var v;
	 *   while (maybe() || (v=0)) {
	 *     if (maybe()) {
	 *       v = 0;
	 *       break;
	 *     }
	 *   }
	 *   v;
	 * }
	 * </pre>
	 */
	@Override
	public Map<Integer, Object> visit(final ASTBreakClauseNode node, final Map<Integer, Object> map) throws SemanticsException {
		final String label = node.getLabel();
		final LabelInfo info = label == null ? labelStack.peek() : labelMap.get(label);
		if (info != null)
			info.intersectToBreak(map);
		return markAllAssigned(map);
	}

	/**
	 * <h1><code>r ; continue [&lt;label&gt;] ; t</code></h1>
	 * <p><ul>
	 * <li>AFTER(continue) = VARS(r)</li>
	 * </ul></p>
	 * <pre>
	 * function foo() {
	 *   var v;
	 *   do {
	 *     if (maybe())
	 *       continue;
	 *     v = 0;
	 *   } while (v>0)
	 *   v;
	 * }
	 * </pre>
	 */
	@Override
	public Map<Integer, Object> visit(final ASTContinueClauseNode node, final Map<Integer, Object> map) throws SemanticsException {
		final String label = node.getLabel();
		final LabelInfo info = label == null ? labelStack.peek() : labelMap.get(label);
		if (info != null)
			info.intersectToContinue(map);
		return markAllAssigned(map);
	}

	/**
	 * <h1><code>r ; return s ; t</code></h1>
	 * <p><ul>
	 * <li>BEFORE(s) = BEFORE(return)</li>
	 * <li>AFTER(return) = VARS(r)</li>
	 * </ul></p>
	 * <pre>
	 * function foo() {
	 *   var v;
	 *   var w = 0;
	 *   if (maybe())
	 *     return w;
	 *   v = 0;
	 *   return v;
	 *
	 *   var r;
	 *   if (maybe() || (r=0))
	 *     return 0;
	 *   r;
	 * }
	 * </pre>
	 * <h1><code>s ; return ; t</code></h1>
	 * <p><ul>
	 * <li>AFTER(return) = VARS(r)</li>
	 * </ul></p>
	 * <pre>
	 * function foo() {
	 *   var v;
	 *   return v;
	 *   v;
	 * }
	 * </pre>
	 */
	@Override
	public Map<Integer, Object> visit(final ASTReturnClauseNode node, Map<Integer, Object> map) throws SemanticsException {
		if (node.hasReturn())
			map = jjtAccept(node.getReturnNode(), map, ALWAYS);
		// No need to copy the map as the set of variables can only be changed when
		// we enter a function or lambda, and both ASTFunctionClauseNode and
		// ASTFunctionNode make a copy of the map.
		return markAllAssigned(map);
	}

	/**
	 * <h1><code>r ; log...(s) ; t</code></h1>
	 * <p><ul>
	 * <li>BEFORE(s) = BEFORE(log)</li>
	 * <li>AFTER(log) = AFTER(s)</li>
	 * </ul></p>
	 * <pre>
	 * function foo() {
	 *   var v;
	 *   loginfo(v = 0);
	 *   v;
	 * }
	 * </pre>
	 */
	@Override
	public Map<Integer, Object> visit(final ASTLogNode node, final Map<Integer, Object> map) throws SemanticsException {
		return jjtAccept(node.getLogMessageNode(), map, ALWAYS);
	}

	/**
	 * <h1><code>q ; (r<sub>1</sub>, r<sub>2</sub>, ..., r<sub>n</sub>) => { s } ; t</code></h1>
	 * <p><ul>
	 * <li>AFTER(lambda) = BEFORE(lambda)</li>
	 * <li>BEFORE(s) = BEFORE(lambda) &#8746; &#8899;<sub>i=1</sub><sup>n</sup> {r<sub>i</sub>} </li>
	 * <li>VARS(s) = VARS(q) &#8746; VARS(lambda)</li>
	 * </ul></p>
	 * <pre>
	 * function foo() {
	 *   var x;
	 *   var y = 9;
	 *   () => {
	 *     x = 9;
	 *     y;
	 *   }
	 *   x;
	 * }
	 * </pre>
	 */
	@Override
	public Map<Integer, Object> visit(final ASTFunctionNode node, final Map<Integer, Object> map) throws SemanticsException {
		final Set<Integer> vars = new HashSet<>();
		resolutionResult.putBasicSourcedLocalVariables(node.getFunctionId(), vars);
		resolutionResult.putBasicSourcedClosureVariables(node.getFunctionId(), vars);

		// BEFORE(s) = AFTER(r) + parameters
		final Map<Integer, Object> newMap = copy(map);
		final int count = node.getArgumentCount();
		for (int i = 0; i  < count; ++i)
			markAssigned(newMap, node.getArgResolvable(i));

		// VARS(s) = VARS(r) v VARS(lambda)
		addNewVars(newMap, vars);

		jjtAccept(node.getBodyNode(), newMap, ALWAYS);

		// AFTER(lambda) = BEFORE(lambda)
		return map;
	}

	/**
	 * <h1><code>r ; ( +s | -s | ++s | --s | s++ | s-- | ~s ) ; t</code></h1>
	 * <p>
	 * Note that we also need to check whether the variable was already assigned
	 * when the unary method is assigning (++s, --s). Assuming the variable was
	 * not already assigned, the {@link ASTVariableNode} will throw the error.
	 * <ul>
	 * <li>BEFORE(s) = BEFORE(unary)</li>
	 * <li>AFTER(unary) = AFTER(s) </li>
	 * </ul></p>
	 * <pre>
	 * function foo() {
	 *   var x;
	 *   +(x=0);
	 *   x;
	 * }
	 * </pre>
	 *
	 * <h1><code>r ; !s ; t</code></h1>
	 * <p><ul>
	 * <li>BEFORE(s)         = BEFORE(not)</li>
	 * <li>AFTER(and, true)  = AFTER(s, false)</li>
	 * <li>AFTER(and, false) = AFTER(s, true)</li>
	 * <li>AFTER(and)        = AFTER(s,true) &#8745; AFTER(s, false) = AFTER(s)</li>
	 * </ul></p>
	 * <pre>
	 * function foo() {
	 *   var y;
	 *   if (!(maybe() && (y=false)))
	 *     y;
	 * }
	 * </pre>
	 */
	@Override
	public Map<Integer, Object> visit(final ASTUnaryExpressionNode node, final Map<Integer, Object> map)
			throws SemanticsException {
		if (node.getUnaryMethod() == EMethod.EXCLAMATION) {
			switch (NullUtil.or(typeStack.peek(), ALWAYS)) {
			case ALWAYS:
				// AFTER(and) = AFTER(s)
				return jjtAccept(node.getUnaryNode(), map, ALWAYS);
			case WHEN_FALSE:
				// AFTER(and, false)  = AFTER(s, true)
				jjtAccept(node.getUnaryNode(), copy(map), WHEN_FALSE);
				return jjtAccept(node.getUnaryNode(), map, WHEN_TRUE);
			case WHEN_TRUE:
				// AFTER(and, true)  = AFTER(s, false)
				jjtAccept(node.getUnaryNode(), copy(map), WHEN_TRUE);
				return jjtAccept(node.getUnaryNode(), map, WHEN_FALSE);
			default:
				throw new UnhandledEnumException(NullUtil.or(typeStack.peek(), ALWAYS), node.getUnaryNode());
			}
		}
		return jjtAccept(node.getUnaryNode(), map, ALWAYS);
	}

	/** @see #visit(ASTUnaryExpressionNode, Map) */
	@Override
	public Map<Integer, Object> visit(final ASTPostUnaryExpressionNode node, final Map<Integer, Object> map)
			throws SemanticsException {
		return jjtAccept(node.getUnaryNode(), map, ALWAYS);
	}

	/**
	 * <h1><code>r ; s<sub>1</sub>.s<sub>2</sub>[s<sub>3</sub>](s<sub>4</sub>)...(s<sub>n</sub>) ; t</code></h1>
	 * <p><ul>
	 * <li>BEFORE(s<sub>1</sub>) = BEFORE(r)</li>
	 * <li>BEFORE(s<sub>i</sub>) = BEFORE(s<sub>i-1</sub>), i &#8712; [2,n]</li>
	 * <li>AFTER(array) = AFTER(s<sub>n</sub>)</li>
	 * </ul></p>
	 * <pre>
	 * function foo() {
	 *   var v = [[]];
	 *   var u;
	 *   v[0].push(u=0);
	 *   v;
	 *   u;
	 * }
	 * </pre>
	 */
	@Override
	public Map<Integer, Object> visit(final ASTPropertyExpressionNode node, Map<Integer, Object> map)
			throws SemanticsException {
		map = jjtAccept(node.getStartNode(), map, ALWAYS);
		final int count = node.getPropertyNodeCount();
		for (int i = 0; i < count; ++i)
			map = jjtAccept(node.getPropertyNode(i), map, ALWAYS);
		return map;
	}

	/**
	 * <h1><code>r ; a.b ; t</code></h1>
	 * <p><ul>
	 * <li>AFTER(identifier) = AFTER(identifier)</li>
	 * </ul></p>
	 * <pre>
	 * function foo() {
	 *   var v = [];
	 *   v.length;
	 *   v;
	 * }
	 * </pre>
	 */
	@Override
	public Map<Integer, Object> visit(final ASTIdentifierNameNode node, final Map<Integer, Object> map)
			throws SemanticsException {
		return map;
	}

	/**
	 * <h1><code>r ; with(...) (s) ; t</code></h1>
	 * <p><ul>
	 * <li>BEFORE(s)          = BEFORE(with)</li>
	 * <li>AFTER(with, true)  = AFTER(s, true)</li>
	 * <li>AFTER(with, false) = AFTER(s, false)</li>
	 * <li>AFTER(with)        = AFTER(s)</li>
	 * </ul></p>
	 * <pre>
	 * function foo() {
	 *   var v;
	 *   with (system) {
	 *     v = 0;
	 *   }
	 *   v;
	 * }
	 * </pre>
	 */
	@Override
	public Map<Integer, Object> visit(final ASTWithClauseNode node, final Map<Integer, Object> map) throws SemanticsException {
		// Keep the current type, this is like a parenthesis expression.
		return jjtAccept(node.getBodyNode(), map);
	}

	/**
	 * <h1><code>q ; function (r<sub>1</sub>, r<sub>2</sub>, ..., r<sub>n</sub>) { s } ; t</code></h1>
	 * <p>
	 * We estimate the required set size by adding the number
	 * of local and closure variables of the function.
	 * The set of variables definitely assigned before <code>r</code>
	 * includes the set of global variables, but the implementation
	 * omits these as they are of no interest and do not need to be
	 * checked.
	 * <ul>
	 * <li>VARS(s) = VARS(q) &#8746; VARS(function)</li>
	 * <li>BEFORE(s) = BEFORE(function) &#8746; &#8899;<sub>i=1</sub><sup>n</sup> {r<sub>i</sub>} </li>
	 * <li>AFTER(function) = BEFORE(function)</li>
	 * </ul></p>
	 */
	@Override
	public Map<Integer, Object> visit(final ASTFunctionClauseNode node, final Map<Integer, Object> map) throws SemanticsException {
		final Set<Integer> vars = new HashSet<>();
		resolutionResult.putBasicSourcedLocalVariables(node.getFunctionId(), vars);
		resolutionResult.putBasicSourcedClosureVariables(node.getFunctionId(), vars);

		// BEFORE(s) = AFTER(r) + parameters
		final Map<Integer, Object> newMap = copy(map);
		final int count = node.getArgumentCount();
		for (int i = 0; i  < count; ++i)
			markAssigned(newMap, node.getArgResolvable(i));

		// VARS(s) = VARS(r) v VARS(lambda)
		addNewVars(newMap, vars);

		jjtAccept(node.getBodyNode(), newMap, ALWAYS);

		// AFTER(lambda) = BEFORE(lambda)
		return map;
	}

	/**
	 * <h1><code>s ; ; t</code></h1>
	 * <p><ul>
	 * <li>AFTER(empty) = BEFORE(empty)</li>
	 * </ul></p>
	 * <pre>
	 * function foo() {
	 *   var v;
	 *   if (maybe());
	 *   v;
	 * }
	 * </pre>
	 */
	@Override
	public Map<Integer, Object> visit(final ASTEmptyNode node, final Map<Integer, Object> map) throws SemanticsException {
		return map;
	}

	/**
	 * <h1><code>r ; %]...[% ; t </code></h1>
	 * <p>
	 * This is like a comment.
	 * <ul>
	 * <li>AFTER(los) = BEFORE(los)</li>
	 * </ul></p>
	 * <pre>
	 * [%%
	 *   function foo() {
	 *     var v;
	 *     %]v = 0;[%%
	 *     v;
	 *   }
	 * %]
	 * </pre>
	 */
	@Override
	public Map<Integer, Object> visit(final ASTLosNode node, final Map<Integer, Object> map) throws SemanticsException {
		return map;
	}

	/**
	 * <h1><code>s ; #...# ; t</code></h1>
	 * <p><ul>
	 * <li>AFTER(regex)        = BEFORE(regex)</li>
	 * <li>AFTER(regex, true)  = BEFORE(regex)</li>
	 * <li>AFTER(regex, false) = VARS(s)</li>
	 * </ul></p>
	 * <pre>
	 * function foo() {
	 *   var v;
	 *   ##;
	 *   v;
	 * }
	 * </pre>
	 */
	@Override
	public Map<Integer, Object> visit(final ASTRegexNode node, final Map<Integer, Object> map) throws SemanticsException {
		if (typeStack.peek() == WHEN_FALSE)
			markAllAssigned(map);
		return map;
	}

	/**
	 * <h1><code>p ; q ? r : s ; t</code></h1>
	 * <p><ul>
	 * <li>BEFORE(q)             = BEFORE(ternary)</li>
	 * <li>BEFORE(r)             = AFTER(q, true)</li>
	 * <li>BEFORE(s)             = AFTER(q, false)</li>
	 * <li>AFTER(ternary, true)  = AFTER(r, true) &#8745; AFTER(s, true)</li>
	 * <li>AFTER(ternary, false) = AFTER(r, false) &#8745; AFTER(s, false)</li>
	 * <li>AFTER(ternary)        = AFTER(ternary, true) &#8745; AFTER(ternary, false)</li>
	 * </ul></p>
	 * <pre>
	 * function foo() {
	 *   var v;
	 *   (maybe() || (v=0)) ? v = 0 : 0;
	 *   v;
	 *
	 *   var w;
	 *   (maybe() && (w=0)) ? w : w = 0;
	 *   w;
	 *
	 *   var u;
	 *
	 * }
	 * </pre>
	 */
	@Override
	public Map<Integer, Object> visit(final ASTTernaryExpressionNode node, final Map<Integer, Object> map) throws SemanticsException {
		// BEFORE(q) = BEFORE(ternary)
		final Map<Integer, Object> afterQTrue = jjtAccept(node.getConditionNode(), copy(map), WHEN_TRUE);
		final Map<Integer, Object> afterQFalse = jjtAccept(node.getConditionNode(), map, WHEN_FALSE);

		// BEFORE(r) = AFTER(q, true)
		final Map<Integer, Object> afterRTrue = jjtAccept(node.getIfNode(), copy(afterQTrue), WHEN_TRUE);
		final Map<Integer, Object> afterRFalse = jjtAccept(node.getIfNode(), afterQTrue, WHEN_FALSE);

		// BEFORE(s) = AFTER(q, false)
		final Map<Integer, Object> afterSTrue = jjtAccept(node.getElseNode(), copy(afterQFalse), WHEN_TRUE);
		final Map<Integer, Object> afterSFalse = jjtAccept(node.getElseNode(), afterQFalse, WHEN_FALSE);

		switch (NullUtil.or(typeStack.peek(), ALWAYS)) {
		case ALWAYS:
			// AFTER(ternary)        = AFTER(ternary, true) &#8745; AFTER(ternary, false)
			//                       = AFTER(r, false) ^ AFTER(s, false) ^ AFTER(r, true) ^ AFTER(s, true)
			return intersectToLhs(afterRFalse, afterSFalse, afterRTrue, afterSTrue);
		case WHEN_FALSE:
			// AFTER(ternary, false) = AFTER(r, false) ^ AFTER(s, false)
			return intersectToLhs(afterRFalse, afterSFalse);
		case WHEN_TRUE:
			// AFTER(ternary, true)  = AFTER(r, true) ^ AFTER(s, true)
			return intersectToLhs(afterRTrue, afterSTrue);
		default:
			throw new UnhandledEnumException(NullUtil.or(typeStack.peek(), ALWAYS), node);
		}
	}

	/**
	 * <h1><code>r ; ( s ) ; t</code></h1>
	 * <p><ul>
	 * <li>BEFORE(s)                 = BEFORE(parenthesis)</li>
	 * <li>AFTER(parenthesis, true)  = AFTER(s, true)</li>
	 * <li>AFTER(parenthesis, false) = AFTER(s, false)</li>
	 * <li>AFTER(parenthesis)        = AFTER(s)</li>
	 * </ul></p>
	 * <pre>
	 * function foo() {
	 *   var v;
	 *   (v = 0);
	 *   v;
	 * }
	 * </pre>
	 */
	@Override
	public Map<Integer, Object> visit(final ASTParenthesisExpressionNode node, final Map<Integer, Object> map) throws SemanticsException {
		return jjtAccept(node.getNode(), map);
	}

	/**
	 * <h1><code>r ; s<sub>1</sub> == s<sub>2</sub> == ... == s<sub>n</sub> ; t</code></h1>
	 * <p><ul>
	 * <li>BEFORE(s<sub>1</sub>) = BEFORE(equal_expression)</li>
	 * <li>BEFORE(s<sub>i</sub>) = AFTER(s<sub>i-1</sub>), i &#8712; [2,n]</li>
	 * <li>AFTER(equal_expression) = AFTER(s<sub>n</sub>)</li>
	 * </ul></p>
	 * <pre>
	 * function foo() {
	 *   var v;
	 *   3 == 4 == (v = 0) == 5;
	 *   v;
	 * }
	 * </pre>
	 */
	@Override
	public Map<Integer, Object> visit(final ASTEqualExpressionNode node, Map<Integer, Object> map)
			throws SemanticsException {
		final int count = node.jjtGetNumChildren();
		for (int i = 0; i < count; ++i)
			map = jjtAccept(node.jjtGetChild(i), map, ALWAYS);
		return map;
	}

	/**
	 * <h1><code>r ; s<sub>1</sub> < s<sub>2</sub> < ... < s<sub>n</sub> ; t</code></h1>
	 * <p><ul>
	 * <li>BEFORE(s<sub>1</sub>) = BEFORE(equal_expression)</li>
	 * <li>BEFORE(s<sub>i</sub>) = AFTER(s<sub>i-1</sub>), i &#8712; [2,n]</li>
	 * <li>AFTER(equal_expression) = AFTER(s<sub>n</sub>)</li>
	 * </ul></p>
	 * <pre>
	 * function foo() {
	 *   var v;
	 *   3 == 4 == (v = 0) == 5;
	 *   v;
	 * }
	 * </pre>
	 */
	@Override
	public Map<Integer, Object> visit(final ASTComparisonExpressionNode node, Map<Integer, Object> map)
			throws SemanticsException {
		final int count = node.jjtGetNumChildren();
		for (int i = 0; i < count; ++i)
			map = jjtAccept(node.jjtGetChild(i), map, ALWAYS);
		return map;
	}

	/**
	 * <h1><code>r ; var &lt;variable&gt; = s ; t</code></h1>
	 * <p>
	 * Here we mark the variable as definitely assigned.
	 * <ul>
	 * <li>BEFORE(s) = BEOFRE(declaration)</li>
	 * <li>AFTER(declaration) = AFTER(s) &#8746; {&lt;variable&gt;}</li>
	 * </ul></p>
	 * <pre>
	 * function foo() {
	 *   var v;
	 *   var u = (v = 0);
	 *   v;
	 * }
	 * </pre>
	 */
	@Override
	public Map<Integer, Object> visit(final ASTVariableDeclarationClauseNode node, Map<Integer, Object> map)
			throws SemanticsException {
		if (node.hasAssignment()) {
			map = jjtAccept(node.getAssignmentNode(), map, ALWAYS);
			markAssigned(map, node);
		}
		return map;
	}

	/** Not evaluated. */
	@Override
	public Map<Integer, Object> visit(final ASTScopeExternalNode node, final Map<Integer, Object> map) throws SemanticsException {
		return map;
	}

	/** Not evaluated. */
	@Override
	public Map<Integer, Object> visit(final ASTScopeManualNode node, final Map<Integer, Object> map) throws SemanticsException {
		return map;
	}

	/** Not evaluated. */
	@Override
	public Map<Integer, Object> visit(final ASTScopeGlobalNode node, final Map<Integer, Object> map) throws SemanticsException {
		return map;
	}

	/** Not evaluated. */
	@Override
	public Map<Integer, Object> visit(final ASTVariableTypeNode node, final Map<Integer, Object> map) throws SemanticsException {
		return map;
	}

	/** @see #visit(ASTVariableDeclarationClauseNode, Map) */
	@Override
	public Map<Integer, Object> visit(final ASTFunctionArgumentNode node, final Map<Integer, Object> map) throws SemanticsException {
		markAssigned(map, node);
		return map;
	}

	/**
	 * <h1><code>r ; &lt;chars&gt; ; t</code></h1>
	 * <p><ul>
	 * <li>AFTER(chars) = BEFORE(chars)</li>
	 * </ul></p>
	 * <pre>
	 * function foo() {
	 *   var v;
	 *   `...${v=0}...${v}...`;
	 * }
	 * </pre>
	 */

	@Override
	public Map<Integer, Object> visit(final ASTStringCharactersNode node, final Map<Integer, Object> map) throws SemanticsException {
		return map;
	}

	private void visitHeader(@Nullable final IHeaderNode header) throws SemanticsException {
		if (header != null && header.isFunction())
			jjtAccept(header.getNode(), Collections.<Integer, Object>emptyMap(), ALWAYS);
	}

	private <T extends Node & ILabeled> void addLabel(final T node, @Nullable final Map<Integer, Object> forBreak,
			@Nullable final Map<Integer, Object> forContinue) throws SemanticsException {
		final String label = node.getLabel();
		final LabelInfo info = new LabelInfo(forBreak, forContinue);
		if (label == null) {
			labelStack.push(info);
		}
		else {
			if (labelMap.put(label, info) != null)
				throw new DuplicateLabelException(node);
		}
	}

	@Nullable
	private <T extends Node & ILabeled> LabelInfo popLabel(final T node) {
		final String label = node.getLabel();
		LabelInfo info;
		if (label == null)
			info = labelStack.isEmpty() ? null : labelStack.pop();
		else
			info = labelMap.remove(label);
		return info;
	}

	private <T extends ILabeled & Node> LabelInfo getInfo(final Node node, Map<Integer, Object> map,
			@Nullable final Map<Integer, Object> forBreak, @Nullable final Map<Integer, Object> forContinue,
			final EType type, final T labeledNode) throws SemanticsException {
		addLabel(labeledNode, forBreak, forContinue);
		final LabelInfo info;
		try {
			map = jjtAccept(node, map, type);
		}
		finally {
			info = popLabel(labeledNode);
		}
		if (info == null)
			throw new SemanticsException(
					NullUtil.messageFormat(CmnCnst.Error.NO_MATCHING_LABEL_INFO, labeledNode.getLabel()), node);
		info.setMap(map);
		return info;
	}

	private <T extends ILabeled & Node> Map<Integer, Object> getInfoBreak(final Node node, final Map<Integer, Object> map,
			final EType type, final T labeledNode) throws SemanticsException {
		final LabelInfo info = getInfo(node, map, markAllAssigned(copy(map)), null, type, labeledNode);
		return info.getBreakMap();
	}

	private <T extends ILabeled & Node> LabelInfo getInfoBoth(final Node node, final Map<Integer, Object> map,
			final EType type, final T labeledNode) throws SemanticsException {
		final Map<Integer, Object> fullMap = markAllAssigned(copy(map));
		return getInfo(node, map, fullMap, copy(fullMap), type, labeledNode);
	}

	//TODO must also visit lambda defined at global scope, not only header functions
	public static void check(final Node node, final IScopeDefinitions scopeDefs, final IVariableResolutionResult resolutionResult) throws SemanticsException {
		final DefiniteAssignmentCheckVisitor v = new DefiniteAssignmentCheckVisitor(resolutionResult);
		for (final IHeaderNode header : scopeDefs.getGlobal())
			v.visitHeader(header);
		for (final Collection<IHeaderNode> coll : scopeDefs.getManual().values())
			for (final IHeaderNode header : coll)
				v.visitHeader(header);
	}

	private static class LabelInfo {
		private final Map<Integer, Object> forBreak;
		private final Map<Integer, Object> forContinue;
		private Map<Integer, Object> map;
		public LabelInfo(@Nullable final Map<Integer, Object> forBreak, @Nullable final Map<Integer, Object> forContinue) {
			this.forBreak = forBreak != null ? forBreak : Collections.<Integer,Object>emptyMap();
			this.forContinue = forContinue != null ? forContinue : Collections.<Integer,Object>emptyMap();
			map = Collections.emptyMap();
		}
		public Map<Integer, Object> getContinueMap() {
			return forContinue;
		}
		public Map<Integer, Object> getBreakMap() {
			return forBreak;
		}
		public void intersectToBreak(final Map<Integer, Object> rhs) {
			intersectToLhs(forBreak, rhs);
		}
		public void intersectToContinue(final Map<Integer, Object> rhs) {
			intersectToLhs(forContinue, rhs);
		}
		@Override
		public String toString() {
			return NullUtil.messageFormat(CmnCnst.ToString.LABEL_INFO, forBreak, forContinue);
		}
		public void setMap(final Map<Integer, Object> map) {
			this.map = map;
		}
		public Map<Integer, Object> getMap() {
			return map;
		}
	}

	protected static enum EType {
		ALWAYS {
			@Override
			public boolean isOppositeOf(final boolean b) {
				return false;
			}
		},
		WHEN_TRUE {
			@Override
			public boolean isOppositeOf(final boolean b) {
				return !b;
			}
		},
		WHEN_FALSE{
			@Override
			public boolean isOppositeOf(final boolean b) {
				return b;
			}
		};
		public abstract boolean isOppositeOf(boolean b);
	}

	/**
	 * Creates a copy of the map of definitely assigned variables
	 * adds the given variables marked as not yet definitely assigned,
	 * unless they already exists in the map.
	 * @param map
	 * @param vars
	 * @return The copied map.
	 */
	private static void addNewVars(final Map<Integer, Object> map, final Set<Integer> vars) {
		for (final Integer id : vars)
			if (map.put(id, null) != null)
				// Note that this case should not happen
				// as each variable has got a distinct ID,
				// and no variable occurs twice in any set
				// of local or closure variables of any
				// function.
				map.put(id, OBJECT);
	}

	private static Map<Integer, Object> copy(final Map<Integer, Object> map) {
		return new HashMap<>(map);
	}

	/**
	 * Makes the first argument the intersection of both maps.
	 * The resulting map contains {@link Object} for a specific key
	 * iff both maps contain {@link Object} for that key; and
	 * <code>null</code> otherwise.
	 * @param lhs First map.
	 * @param rhs Second map.
	 * @return The result for chaining.
	 */
	protected static Map<Integer, Object> intersectToLhs(final Map<Integer, Object> lhs, final Map<Integer, Object> rhs) {
		for (final Entry<Integer, Object> entryLhs : lhs.entrySet())
			if (entryLhs.getValue() != null && rhs.get(entryLhs.getKey()) == null)
				entryLhs.setValue(null);
		return lhs;
	}

	/**
	 * Same as {@link #intersectToLhs(Map, Map)}, but intersects three sets instead of two.
	 * @see #intersectToLhs(Map, Map)
	 */
	private static Map<Integer, Object> intersectToLhs(final Map<Integer, Object> lhs, final Map<Integer, Object> rhs1,
			final Map<Integer, Object> rhs2) {
		Integer key;
		for (final Entry<Integer, Object> entryLhs : lhs.entrySet())
			if (entryLhs.getValue() != null && (rhs1.get(key = entryLhs.getKey()) == null || rhs2.get(key) == null))
				entryLhs.setValue(null);
		return lhs;
	}

	/**
	 * Same as {@link #intersectToLhs(Map, Map)}, but intersects four sets instead of two.
	 * @see #intersectToLhs(Map, Map)
	 */
	private static Map<Integer, Object> intersectToLhs(final Map<Integer, Object> lhs, final Map<Integer, Object> rhs1,
			final Map<Integer, Object> rhs2, final Map<Integer, Object> rhs3) {
		Integer key;
		for (final Entry<Integer, Object> entryLhs : lhs.entrySet())
			if (entryLhs.getValue() != null
					&& (rhs1.get(key = entryLhs.getKey()) == null || rhs2.get(key) == null || rhs3.get(key) == null))
				entryLhs.setValue(null);
		return lhs;
	}

	/**
	 * Marks all variables as set.
	 * @param map Map to be changed.
	 * @return The map with all variables marked as assigned.
	 */
	private static Map<Integer, Object> markAllAssigned(final Map<Integer, Object> map) {
		for (final Entry<?,Object> entry : map.entrySet())
			entry.setValue(OBJECT);
		return map;
	}

	private static Map<Integer, Object> markAssigned(final Map<Integer, Object> map, final ISourceResolvable resolvable) {
		map.put(Integer.valueOf(resolvable.getBasicSource()), OBJECT);
		return map;
	}
}