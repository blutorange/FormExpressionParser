package de.xima.fc.form.expression.visitor;

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
import de.xima.fc.form.expression.exception.parse.SemanticsException;
import de.xima.fc.form.expression.exception.parse.VariableUsedBeforeAssignmentException;
import de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionReturnDataVisitor;
import de.xima.fc.form.expression.iface.parse.IHeaderNode;
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
	private final Deque<EType> typeStack;

	public DefiniteAssignmentCheckVisitor(final IVariableResolutionResult resolutionResult) {
		this.resolutionResult = resolutionResult;
		typeStack = new ArrayDeque<>();
	}

	private Map<Integer, Object> jjtAccept(final Node node, final Map<Integer, Object> map, final EType type)
			throws SemanticsException {
		// type is almost always EType.ALWAYS
		// so we check for the most common case
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
	 * </ul>
	 * The following applies when there are any <code>&&</code> or <code>||</code> operators:
	 * <ul>
	 *   <li><code>whenTrue / always</code> and <code>#<sub>i</sub></code> = <code>||</code>: AFTER(expression) = AFTER(s<sub>i-1</sub>)</li>
	 *   <li><code>whenFalse / always</code> and <code>#<sub>i</sub></code> = <code>&&</code>: AFTER(expression) = AFTER(s<sub>i-1</sub>)</li>
	 *   <li><code>whenTrue</code> and <code>#<sub>i</sub></code> = <code>&&</code>: No special rules.</li>
	 *   <li><code>whenFalse</code> and <code>#<sub>i</sub></code> = <code>||</code>: No special rules.</li>
	 * </ul>
	 * </p>
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
	 */
	@Override
	public Map<Integer, Object> visit(final ASTExpressionNode node, Map<Integer, Object> map) throws SemanticsException {
		final int count = node.jjtGetNumChildren();
		Map<Integer, Object> afterExpression = null;
		for (int i = 0; i < count; ++i) {
			final Node n = node.jjtGetChild(i);
			switch (n.getSiblingMethod()) {
			case DOUBLE_AMPERSAND:
				if (typeStack.peek() != EType.WHEN_TRUE && afterExpression == null)
					afterExpression = copy(map);
				break;
			case DOUBLE_BAR:
				if (typeStack.peek() != EType.WHEN_FALSE && afterExpression == null)
					afterExpression = copy(map);
				break;
			//$CASES-OMITTED$
			default:
			}
			map = jjtAccept(n, map, EType.ALWAYS);
		}
		return afterExpression == null ? map : afterExpression;
	}

	/**
	 * <h1><code>r ; s<sub>1</sub> = s<sub>2</sub> = ... = s<sub>n</sub>; t</code></h1>
	 * <p>
	 * Note that here we mark variables as definitely assigned.
	 * <ul>
	 * <li>BEFORE(s<sub>n</sub>) = BEFORE(assignment)</li>
	 * <li>BEFORE(s<sub>i</sub>) = AFTER(s<sub>i+1</sub>), i &#8712; [1,n-1]</li>
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
		map = jjtAccept(node.getAssignValueNode(), map, EType.ALWAYS);
		final int count = node.getAssignableNodeCount();
		for (int i = count; i -->0;) {
			final Node n = node.getAssignableNode(i);
			if (n.jjtGetNodeId() == FormExpressionParserTreeConstants.JJTVARIABLENODE
					&& node.getAssignMethod(i) == EMethod.EQUAL)
				markAssigned(map, (ASTVariableNode)n);
			else
				map = jjtAccept(n, map, EType.ALWAYS);
		}
		return map;
	}

	/**
	 * <h1><code>s ; &lt;number&gt; ; t</code></h1>
	 * <p><ul>
	 * <li>AFTER(number) = BEFORE(number)</li>
	 * </ul></p>
	 * <pre>
	 * function foo() {
	 *   var v;
	 *   0.0;
	 *   v;
	 * }
	 * </pre>
	 */
	@Override
	public Map<Integer, Object> visit(final ASTNumberNode node, final Map<Integer, Object> map) throws SemanticsException {
		return map;

	}

	/**
	 * <h1><code>r ; [s<sub>1</sub>, s<sub>2</sub>, ..., s<sub>n</sub>] ; t</code></h1>
	 * <p><ul>
	 * <li>BEFORE(s<sub>1</sub>) = BEFORE(r)</li>
	 * <li>BEFORE(s<sub>i</sub>) = BEFORE(s<sub>i-1</sub>), i &#8712; [2,n]</li>
	 * <li>AFTER(array) = AFTER(s<sub>n</sub>)</li>
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
			map = jjtAccept(node.jjtGetChild(i), map, EType.ALWAYS);
		return map;
	}

	/**
	 * <h1><code>r ; {k<sub>1</sub>: v<sub>1</sub>, k<sub>2</sub>: v<sub>2</sub>, ..., k<sub>n</sub>: v<sub>n</sub>} ; t</code></h1>
	 * <p><ul>
	 * <li>BEFORE(k<sub>1</sub>) = BEFORE(r)</li>
	 * <li>BEFORE(k<sub>i</sub>) = BEFORE(v<sub>i-1</sub>), i &#8712; [2,n]</li>
	 * <li>BEFORE(v<sub>i</sub>) = BEFORE(k<sub>i</sub>), i &#8712; [1,n]</li>
	 * <li>AFTER(hash) = AFTER(v<sub>n</sub>)</li>
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
			map = jjtAccept(node.jjtGetChild(i), map, EType.ALWAYS);
		return map;
	}

	/**
	 * <h1><code>s ; &lt;null&gt; ; t</code></h1>
	 * <p><ul>
	 * <li>AFTER(null) = BEFORE(null)</li>
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
		return map;
	}

	/**
	 * <h1><code>s ; &lt;boolean&gt; ; t</code></h1>
	 * <p><ul>
	 * <li>AFTER(boolean) = BEFORE(boolean)</li>
	 * </ul></p>
	 * <pre>
	 * function foo() {
	 *   var v;
	 *   false;
	 *   v;
	 * }
	 * </pre>
	 */
	@Override
	public Map<Integer, Object> visit(final ASTBooleanNode node, final Map<Integer, Object> map) throws SemanticsException {
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
		// FIXME Do not throw if variable is global variable.
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
	 * <li>AFTER(string) = BEFORE(string)</li>
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
			map = jjtAccept(node.getStringNode(i), map, EType.ALWAYS);
		return map;
	}

	/**
	 * <h1><code>r ; s<sub>1</sub> ; s<sub>2</sub> ; ... ; s<sub>n</sub> ; t</code></h1>
	 * <p><ul>
	 * <li>BEFORE(s<sub>1</sub>) = BEFORE(statement_list)</li>
	 * <li>BEFORE(s<sub>i</sub>) = AFTER(s<sub>i-1</sub>), i &#8712; [2,n]</li>
	 * <li>AFTER(statement_list) = AFTER(s<sub>n</sub>)</li>
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
		final int count = node.jjtGetNumChildren();
		for (int i = 0; i < count; ++i) {
			map = jjtAccept(node.jjtGetChild(i), map, EType.ALWAYS);
		}
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
			final Map<Integer, Object> afterQTrue = jjtAccept(node.getConditionNode(), copy(map), EType.WHEN_TRUE);
			final Map<Integer, Object> afterQFalse = jjtAccept(node.getConditionNode(), map, EType.WHEN_FALSE);
			final Map<Integer, Object> afterR = jjtAccept(node.getIfNode(), afterQTrue, EType.ALWAYS);
			final Map<Integer, Object> afterS = jjtAccept(node.getElseNode(), afterQFalse, EType.ALWAYS);
			return intersectToLhs(afterR, afterS);
		}
		// BEFORE(s)  = BEFORE(if)
		// BEFORE(t)  = AFTER(s, true)
		final Map<Integer, Object> afterSTrue = jjtAccept(node.getConditionNode(), copy(map), EType.WHEN_TRUE);
		final Map<Integer, Object> afterSFalse = jjtAccept(node.getConditionNode(), map, EType.WHEN_FALSE);
		final Map<Integer, Object> afterT = jjtAccept(node.getIfNode(), afterSTrue, EType.ALWAYS);
		// AFTER(if)  = AFTER(T) ^ AFTER(s, false)
		return intersectToLhs(afterT, afterSFalse);
	}

	@Override
	public Map<Integer, Object> visit(final ASTForLoopNode node, final Map<Integer, Object> map) throws SemanticsException {
		// TODO Auto-generated method stub
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
	 * <li>AFTER(while) = AFTER(t, label) &#8745; AFTER(s, false)</li>
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
		//FIXME handle labels, breaks, continues
		// BEFORE(s)    = BEFORE(while)
		// BEFORE(t)    = AFTER(s, true)
		final Map<Integer,Object> afterSTrue = jjtAccept(node.getWhileHeaderNode(), copy(map), EType.WHEN_TRUE);
		final Map<Integer,Object> afterSFalse = jjtAccept(node.getWhileHeaderNode(), map, EType.WHEN_TRUE);
		final Map<Integer,Object> afterT = jjtAccept(node.getBodyNode(), afterSTrue, EType.ALWAYS);
		// AFTER(while) = AFTER(t, label) ^ AFTER(s, false)
		return intersectToLhs(afterT, afterSFalse);
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
		final Map<Integer, Object> afterS = jjtAccept(node.getTryNode(), copy(map), EType.ALWAYS);
		// Add exception variable.
		markAssigned(map, node);
		final Map<Integer, Object> afterT = jjtAccept(node.getTryNode(), map, EType.ALWAYS);
		return intersectToLhs(afterS, afterT);
	}

	@Override
	public Map<Integer, Object> visit(final ASTSwitchClauseNode node, final Map<Integer, Object> map) throws SemanticsException {
		// TODO Auto-generated method stub
	}

	@Override
	public Map<Integer, Object> visit(final ASTDoWhileLoopNode node, final Map<Integer, Object> map) throws SemanticsException {
		// TODO Auto-generated method stub

	}

	/**
	 * <h1><code>r ; exception(s) ; t </code></h1>
	 * <p><ul>
	 * <li>BEFORE(s) = BEFORE(exception)</li>
	 * <li>AFTER(exception) = AFTER(s)</li>
	 * </ul></p>
	 * <pre>
	 * function foo() {
	 *   var v;
	 *   exception(v=0);
	 *   v;
	 * }
	 * </pre>
	 *
	 * <h1><code>s ; exception() ; t </code></h1>
	 * <p><ul>
	 * <li>AFTER(exception) = BEFORE(exception)</li>
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
	public Map<Integer, Object> visit(final ASTExceptionNode node, final Map<Integer, Object> map) throws SemanticsException {
		if (node.hasErrorMessage())
			return jjtAccept(node.getErrorMessageNode(), map, EType.ALWAYS);
		return map;
	}

	@Override
	public Map<Integer, Object> visit(final ASTThrowClauseNode node, final Map<Integer, Object> map) throws SemanticsException {
		// TODO Auto-generated method stub

	}

	@Override
	public Map<Integer, Object> visit(final ASTBreakClauseNode node, final Map<Integer, Object> map) throws SemanticsException {
		// TODO Auto-generated method stub
	}

	@Override
	public Map<Integer, Object> visit(final ASTContinueClauseNode node, final Map<Integer, Object> map) throws SemanticsException {
		// TODO Auto-generated method stub

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
	 */
	@Override
	public Map<Integer, Object> visit(final ASTReturnClauseNode node, Map<Integer, Object> map) throws SemanticsException {
		if (node.hasReturn())
			map = jjtAccept(node.getReturnNode(), map, EType.ALWAYS);
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
		return jjtAccept(node.getLogMessageNode(), map, EType.ALWAYS);
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

		jjtAccept(node.getBodyNode(), newMap, EType.ALWAYS);

		// AFTER(lambda) = BEFORE(lambda)
		return map;
	}

	/**
	 * <h1><code>r ; ( !s | +s | -s | ++s | --s | s++ | s-- | ~s ) ; t</code></h1>
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
	 */
	@Override
	public Map<Integer, Object> visit(final ASTUnaryExpressionNode node, final Map<Integer, Object> map)
			throws SemanticsException {
		return jjtAccept(node.getUnaryNode(), map, EType.ALWAYS);
	}

	/** @see #visit(ASTUnaryExpressionNode, Map) */
	@Override
	public Map<Integer, Object> visit(final ASTPostUnaryExpressionNode node, final Map<Integer, Object> map)
			throws SemanticsException {
		return jjtAccept(node.getUnaryNode(), map, EType.ALWAYS);
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
		map = jjtAccept(node.getStartNode(), map, EType.ALWAYS);
		final int count = node.getPropertyNodeCount();
		for (int i = 0; i < count; ++i)
			map = jjtAccept(node.getPropertyNode(i), map, EType.ALWAYS);
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
	 * <li>BEFORE(s) = BEFORE(with)</li>
	 * <li>AFTER(with) = AFTER(s)</li>
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
		return jjtAccept(node.getBodyNode(), map, EType.ALWAYS);
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

		jjtAccept(node.getBodyNode(), newMap, EType.ALWAYS);

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
	 * <li>AFTER(regex) = BEFORE(regex)</li>
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
		return map;
	}

	/**
	 * <h1><code>p ; q ? r : s ; t</code></h1>
	 * <p><ul>
	 * <li>BEFORE(q) = BEFORE(ternary)</li>
	 * <li>BEFORE(r) = AFTER(q, true)</li>
	 * <li>BEFORE(s) = AFTER(q, false)</li>
	 * <li>AFTER(ternary) = AFTER(r) &#8745; AFTER(s)</li>
	 * <li></li>
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
	 * }
	 * </pre>
	 */
	@Override
	public Map<Integer, Object> visit(final ASTTernaryExpressionNode node, final Map<Integer, Object> map) throws SemanticsException {
		// BEFORE(q) = BEFORE(if)
		// BEFORE(r) = AFTER(q, true)
		// BEFORE(s) = AFTER(q, false)
		// AFTER(if) = AFTER(r) ^ AFTER(s)
		final Map<Integer, Object> afterQTrue = jjtAccept(node.getConditionNode(), copy(map), EType.WHEN_TRUE);
		final Map<Integer, Object> afterQFalse = jjtAccept(node.getConditionNode(), map, EType.WHEN_FALSE);
		final Map<Integer, Object> afterR = jjtAccept(node.getIfNode(), afterQTrue, EType.ALWAYS);
		final Map<Integer, Object> afterS = jjtAccept(node.getElseNode(), afterQFalse, EType.ALWAYS);
		return intersectToLhs(afterR, afterS);
	}

	/**
	 * <h1><code>r ; ( s ) ; t</code></h1>
	 * <p><ul>
	 * <li>BEFORE(s) = BEOFRE(parenthesis)</li>
	 * <li>AFTER(parenthesis) = AFTER(s)</li>
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
			map = jjtAccept(node.jjtGetChild(i), map, EType.ALWAYS);
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
			map = jjtAccept(node.jjtGetChild(i), map, EType.ALWAYS);
		return map;
	}

	/**
	 * <h1><code>r ; var &lt;variable&gt; = s ; t</code></h1>
	 * <p>
	 * Note that here we mark the variable as definitely assigned.
	 * <ul>
	 * <li>BEFORE(s) = BEOFRE(declaration)</li>
	 * <li>AFTER(declaration) = AFTER(s)</li>
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
			map = jjtAccept(node.getAssignmentNode(), map, EType.ALWAYS);
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
			jjtAccept(header.getNode(), Collections.<Integer, Object>emptyMap(), EType.ALWAYS);
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

	private static enum EType {
		WHEN_TRUE,
		WHEN_FALSE,
		ALWAYS;
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
	private static Map<Integer, Object> intersectToLhs(final Map<Integer, Object> lhs, final Map<Integer, Object> rhs) {
		for (final Entry<Integer, Object> entryLhs : lhs.entrySet())
			if (entryLhs.getValue() != null && rhs.get(entryLhs.getKey()) == null)
				entryLhs.setValue(null);
		return lhs;
	}

	// Marks all variables as set.
	private static Map<Integer, Object> markAllAssigned(final Map<Integer, Object> map) {
		for (final Entry<?,Object> entry : map.entrySet())
			entry.setValue(OBJECT);
		return map;
	}

	private static void markAssigned(final Map<Integer, Object> map, final ISourceResolvable resolvable) {
		map.put(Integer.valueOf(resolvable.getBasicSource()), OBJECT);
	}
}