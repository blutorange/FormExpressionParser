package de.xima.fc.form.expression.visitor;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Nullable;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.exception.parse.SemanticsException;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionReturnDataVisitor;
import de.xima.fc.form.expression.iface.parse.IHeaderNode;
import de.xima.fc.form.expression.iface.parse.IScopeDefinitions;
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
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.visitor.DefiniteAssignmentCheckVisitor.IAssignInfo;

@NonNullByDefault
public class DefiniteAssignmentCheckVisitor
		implements IFormExpressionReturnDataVisitor<IAssignInfo, IAssignInfo, SemanticsException> {

	private final ALangObject[] symbolTable;
	private final IVariableResolutionResult resolutionResult;

	public DefiniteAssignmentCheckVisitor(final int symbolTableSize, final IVariableResolutionResult resolutionResult) {
		symbolTable = new ALangObject[symbolTableSize];
		this.resolutionResult = resolutionResult;
	}

	@Override
	public IAssignInfo visit(final ASTExpressionNode node, final IAssignInfo assignInfo) throws SemanticsException {
		// TODO Auto-generated method stub
	}

	@Override
	public IAssignInfo visit(final ASTAssignmentExpressionNode node, final IAssignInfo assignInfo) throws SemanticsException {
		// TODO Auto-generated method stub

	}

	@Override
	public IAssignInfo visit(final ASTNumberNode node, final IAssignInfo assignInfo) throws SemanticsException {
		// TODO Auto-generated method stub

	}

	@Override
	public IAssignInfo visit(final ASTArrayNode node, final IAssignInfo assignInfo) throws SemanticsException {
		// TODO Auto-generated method stub

	}

	@Override
	public IAssignInfo visit(final ASTHashNode node, final IAssignInfo assignInfo) throws SemanticsException {
		// TODO Auto-generated method stub

	}

	@Override
	public IAssignInfo visit(final ASTNullNode node, final IAssignInfo assignInfo) throws SemanticsException {
		// TODO Auto-generated method stub

	}

	@Override
	public IAssignInfo visit(final ASTBooleanNode node, final IAssignInfo assignInfo) throws SemanticsException {
		// TODO Auto-generated method stub

	}

	@Override
	public IAssignInfo visit(final ASTVariableNode node, final IAssignInfo assignInfo) throws SemanticsException {
		// TODO Auto-generated method stub

	}

	@Override
	public IAssignInfo visit(final ASTStringNode node, final IAssignInfo assignInfo) throws SemanticsException {
		// TODO Auto-generated method stub

	}

	@Override
	public IAssignInfo visit(final ASTStatementListNode node, final IAssignInfo assignInfo) throws SemanticsException {
		// TODO Auto-generated method stub

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
	 *   v;
	 *
	 *   var w;
	 *   if (maybe() && (w=0))
	 *     w;
	 * }
	 * </pre>
	 */
	@Override
	public IAssignInfo visit(final ASTIfClauseNode node, final IAssignInfo assignInfo) throws SemanticsException {
		if (node.hasElseNode()) {
		}
		else {
		}
	}

	@Override
	public IAssignInfo visit(final ASTForLoopNode node, final IAssignInfo assignInfo) throws SemanticsException {
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
	public IAssignInfo visit(final ASTWhileLoopNode node, final IAssignInfo assignInfo) throws SemanticsException {
		// TODO Auto-generated method stub
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
	public IAssignInfo visit(final ASTTryClauseNode node, final IAssignInfo assignInfo) throws SemanticsException {
		// TODO Auto-generated method stub
	}

	@Override
	public IAssignInfo visit(final ASTSwitchClauseNode node, final IAssignInfo assignInfo) throws SemanticsException {
		// TODO Auto-generated method stub
	}

	@Override
	public IAssignInfo visit(final ASTDoWhileLoopNode node, final IAssignInfo assignInfo) throws SemanticsException {
		// TODO Auto-generated method stub

	}

	@Override
	public IAssignInfo visit(final ASTExceptionNode node, final IAssignInfo assignInfo) throws SemanticsException {
		// TODO Auto-generated method stub

	}

	@Override
	public IAssignInfo visit(final ASTThrowClauseNode node, final IAssignInfo assignInfo) throws SemanticsException {
		// TODO Auto-generated method stub

	}

	@Override
	public IAssignInfo visit(final ASTBreakClauseNode node, final IAssignInfo assignInfo) throws SemanticsException {
		// TODO Auto-generated method stub
	}

	@Override
	public IAssignInfo visit(final ASTContinueClauseNode node, final IAssignInfo assignInfo) throws SemanticsException {
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
	 * }
	 * </pre>
	 */
	@Override
	public IAssignInfo visit(final ASTReturnClauseNode node, final IAssignInfo assignInfo) throws SemanticsException {
		// TODO Auto-generated method stub

	}

	@Override
	public IAssignInfo visit(final ASTLogNode node, final IAssignInfo assignInfo) throws SemanticsException {
		// TODO Auto-generated method stub

	}

	/**
	 * <h1><code>r ; (...) => { s } ; t</code></h1>
	 * <p><ul>
	 * <li>AFTER(lambda) = BEFORE(lambda)</li>
	 * <li>BEFORE(s) = AFTER(r)</li>
	 * <li>VARS(s) = VARS(r) &#8746; VARS(lambda)</li>
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
	public IAssignInfo visit(final ASTFunctionNode node, final IAssignInfo assignInfo) throws SemanticsException {
		// VARS(s) = VARS(r) &#8746; VARS(lambda)
		final Set<Integer> vars = new HashSet<>();
		resolutionResult.putBasicSourcedLocalVariables(node.getFunctionId(), vars);
		resolutionResult.putBasicSourcedClosureVariables(node.getFunctionId(), vars);

		// BEFORE(s) = AFTER(r)
		final IAssignInfo newAssignInfo = assignInfo.copyUnionVars(vars);
		node.getBodyNode().jjtAccept(this, newAssignInfo);

		// AFTER(lambda) = BEFORE(lambda)
		return assignInfo;
		// TODO Auto-generated method stub
	}

	@Override
	public IAssignInfo visit(final ASTUnaryExpressionNode node, final IAssignInfo assignInfo) throws SemanticsException {
		// TODO Auto-generated method stub

	}

	@Override
	public IAssignInfo visit(final ASTPropertyExpressionNode node, final IAssignInfo assignInfo) throws SemanticsException {
		// TODO Auto-generated method stub

	}

	@Override
	public IAssignInfo visit(final ASTIdentifierNameNode node, final IAssignInfo assignInfo) throws SemanticsException {
		// TODO Auto-generated method stub

	}

	@Override
	public IAssignInfo visit(final ASTWithClauseNode node, final IAssignInfo assignInfo) throws SemanticsException {
		// TODO Auto-generated method stub

	}

	/**
	 * <h1><code>function (...) { r }</code></h1>
	 * <p>
	 * We estimate the required set size by adding the number
	 * of local and closure variables of the function.
	 * The set of variables definitely assigned before <code>r</code>
	 * includes the set of global variables, but the implementation
	 * omits these as they are of no interest and do not need to be
	 * checked.
	 * <ul>
	 * <li>BEFORE(r) = VARS(global)</li>
	 * <li>VARS(r) = VARS(global) &#8746; VARS(function)</li>
	 * <li>AFTER(function) = BEFORE(function)</li>
	 * </ul></p>
	 */
	@Override
	public IAssignInfo visit(final ASTFunctionClauseNode node, final IAssignInfo assignInfo) throws SemanticsException {
		final Set<Integer> vars = new HashSet<>();
		resolutionResult.putBasicSourcedLocalVariables(node.getFunctionId(), vars);
		resolutionResult.putBasicSourcedClosureVariables(node.getFunctionId(), vars);
		node.jjtAccept(this, assignInfo.copyUnionVars(vars));
		return assignInfo;
	}

	@Override
	public IAssignInfo visit(final ASTEmptyNode node, final IAssignInfo assignInfo) throws SemanticsException {
		// TODO Auto-generated method stub

	}

	@Override
	public IAssignInfo visit(final ASTLosNode node, final IAssignInfo assignInfo) throws SemanticsException {
		// TODO Auto-generated method stub

	}

	@Override
	public IAssignInfo visit(final ASTRegexNode node, final IAssignInfo assignInfo) throws SemanticsException {
		// TODO Auto-generated method stub

	}

	@Override
	public IAssignInfo visit(final ASTTernaryExpressionNode node, final IAssignInfo assignInfo) throws SemanticsException {
		// TODO Auto-generated method stub

	}

	@Override
	public IAssignInfo visit(final ASTParenthesisExpressionNode node, final IAssignInfo assignInfo) throws SemanticsException {
		// TODO Auto-generated method stub

	}

	@Override
	public IAssignInfo visit(final ASTEqualExpressionNode node, final IAssignInfo assignInfo) throws SemanticsException {
		// TODO Auto-generated method stub

	}

	@Override
	public IAssignInfo visit(final ASTPostUnaryExpressionNode node, final IAssignInfo assignInfo) throws SemanticsException {
		// TODO Auto-generated method stub

	}

	@Override
	public IAssignInfo visit(final ASTComparisonExpressionNode node, final IAssignInfo assignInfo) throws SemanticsException {
		// TODO Auto-generated method stub

	}

	@Override
	public IAssignInfo visit(final ASTScopeExternalNode node, final IAssignInfo assignInfo) throws SemanticsException {
		// TODO Auto-generated method stub

	}

	@Override
	public IAssignInfo visit(final ASTVariableDeclarationClauseNode node, final IAssignInfo assignInfo) throws SemanticsException {
		// TODO Auto-generated method stub

	}

	@Override
	public IAssignInfo visit(final ASTScopeManualNode node, final IAssignInfo assignInfo) throws SemanticsException {
		// TODO Auto-generated method stub
	}

	@Override
	public IAssignInfo visit(final ASTScopeGlobalNode node, final IAssignInfo assignInfo) throws SemanticsException {
		// TODO Auto-generated method stub
	}

	@Override
	public IAssignInfo visit(final ASTVariableTypeNode node, final IAssignInfo assignInfo) throws SemanticsException {
		// TODO Auto-generated method stub
	}

	@Override
	public IAssignInfo visit(final ASTFunctionArgumentNode node, final IAssignInfo assignInfo) throws SemanticsException {
		// TODO Auto-generated method stub
	}

	@Override
	public IAssignInfo visit(final ASTStringCharactersNode node, final IAssignInfo assignInfo) throws SemanticsException {
		// TODO Auto-generated method stub
	}

	private void visitHeader(@Nullable final IHeaderNode header) throws SemanticsException {
		if (header != null && header.isFunction()) {
			header.getNode().jjtAccept(this, new AssignInfo());
		}
	}

	public static void check(final IScopeDefinitions scopeDefs, final int symbolTableSize,
			final IVariableResolutionResult resolutionResult) throws SemanticsException {
		final DefiniteAssignmentCheckVisitor v = new DefiniteAssignmentCheckVisitor(symbolTableSize, resolutionResult);
		for (final IHeaderNode header : scopeDefs.getGlobal())
			v.visitHeader(header);
		for (final Collection<IHeaderNode> coll : scopeDefs.getManual().values())
			for (final IHeaderNode header : coll)
				v.visitHeader(header);
	}


	//TODO get the max/average number of local/closure variables to check and set
	//the initial hashset size appropriately for each function from
	// IResolutionResult
	protected static interface IAssignInfo {
		public IAssignInfo copyUnionVars(Set<Integer> vars);
	}

	private static class VarInfo {
		private final static int FLAG_TRUE = 1;
		private final static int FLAG_FALSE = 2;
		private int infoTrueFalse;
		@Nullable private Set<String> infoLabel;
		public VarInfo() {
			infoTrueFalse = 0;
		}
		private VarInfo(final int infoTrueFalse, @Nullable final Set<String> infoLabel) {
			this.infoTrueFalse = infoTrueFalse;
		}
		public boolean isWhenTrue() {
			return (infoTrueFalse & FLAG_TRUE) != 0;
		}
		public boolean isWhenFalse() {
			return (infoTrueFalse & FLAG_FALSE) != 0;
		}
		public void setWhenTrue() {
			infoTrueFalse |= FLAG_TRUE;
		}
		public void setWhenFalse() {
			infoTrueFalse |= FLAG_FALSE;
		}
		public VarInfo copy() {
			return new VarInfo(infoTrueFalse, infoLabel != null ? new HashSet<>(infoLabel) : null);
		}
	}

	//TODO replace AssignInfo with Map<Integer, VarInfo>, perhaps some static methods?

	private static class AssignInfo implements IAssignInfo {
		private final Map<Integer, VarInfo> map;

		public AssignInfo() {
			map = Collections.emptyMap();
		}

//		public AssignInfo(final Set<Integer> vars) {
//			final int size = vars.size();
//			map = new HashMap<>(size < 4 ? 4 : size);
//			for (final Integer id : vars)
//				map.put(id, new VarInfo());
//		}

		private AssignInfo(final Map<Integer, VarInfo> map) {
			this.map = map;
		}

		@Override
		public IAssignInfo copyUnionVars(final Set<Integer> vars) {
			final Map<Integer, VarInfo> newMap = copyMap();
			for (final Integer id : vars)
				newMap.put(id, new VarInfo());
			return new AssignInfo(map);
		}

		private Map<Integer, VarInfo> copyMap() {
			final Map<Integer, VarInfo> newMap = new HashMap<>(this.map.size());
			for (final Entry<Integer, VarInfo> entry : this.map.entrySet())
				newMap.put(entry.getKey(), entry.getValue().copy());
			return newMap;
		}
	}
}