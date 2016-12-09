package de.xima.fc.form.expression.visitor;

import static de.xima.fc.form.expression.enums.ELangObjectType.ARRAY;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.enums.ELangObjectType;
import de.xima.fc.form.expression.exception.parse.IncompatibleBranchConditionTypeException;
import de.xima.fc.form.expression.exception.parse.SemanticsException;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionReturnVoidVisitor;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.impl.variable.SimpleVariableType;
import de.xima.fc.form.expression.impl.variable.VariableTypeBuilder;
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
import de.xima.fc.form.expression.object.BooleanLangObject;
import de.xima.fc.form.expression.object.ExceptionLangObject;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.object.StringLangObject;
import de.xima.fc.form.expression.visitor.VariableTypeCheckVisitor.NodeInfo;

/**
 * <p>
 * Checks whether the variable types are consistent and will not throw an error
 * on runtime. For example, the program given below is illegal because variable
 * <code>a<code> was declared as a
 * {@link NumberLangObject} but is assigned to a {@link StringLangObject}.
 * <pre>
 * global scope {
 *   number a;
 * }
 * a = 'foo';
 * </pre>
 * </p><p>
 * Functions may return a value in two ways, by an explicit
 * return statement or by the last expression or clause executed.
 * Each clause and expression always returns some {@link ALangObject}.
 * Note that in strict mode functions are required to return a result
 * explicitly.
 * </p><p>
 * Empty statement nodes simple pass through the value of the last
 * node. Therefore the function below returns a string:
 * <pre>
 *   function string foo() {
 *     "bar";;;;;
 *   }
 * </pre>
 * </p><p>
 * To verify program, we proceed as follows. For each node,
 * need to know:
 * <ul>
 *   <li> a (unified) type, if any, the node returns via return clauses;</li>
 *   <li> the list of types the node always returns implicitly; and</li>
 *   <li> whether the node can possibly complete normally.</li>
 * </ul>
 * The list of types cannot be empty normally, as each node always returns
 * something. The exception to the rule are throw clause nodes that never
 * return anything implicitly. As a result, other nodes may not return
 * anything either, when all code branches include a return clause:
 * <pre>
 *   function number foo() {
 *     if (true) return 1;
 *     else return 0;
 *   }
 * </p><p>
 * For some nodes such as if-clause nodes this list may contain more than
 * one entry. These types do not need be mutually compatible necessarily,
 * as the implicit return value of a node is not always used. For example,
 * consider the following program:
 * <pre>
 *   function string foo() {
 *     if (someCondition())
 *       5;
 *     else
 *       true;
 *    "bar";
 *   }
 * </pre>
 * The function <code>foo</code> always returns a string indeed, as its
 * signature claims. The if-clause node implicitly returns either a
 * {@link NumberLangObject} (<code>5</code>) or a {@link BooleanLangObject},
 * depending on the condition. However, this implicit return type is never
 * needed as it is not the last statement of the function body.
 * </p>
 * <p>
 * Next we make a slight modification to the above program:
 * 
 * <pre>
 *   function number foo() {
 *     if (someCondition())
 *       return 9;
 *     else
 *       true;
 *     throw exception('bar');
 *   }
 * </pre>
 * 
 * The if-clause node implicitly returns a {@link BooleanLangObject} which may
 * or may not be needed; and may return a {@link NumberLangObject} via a return
 * clause that is definitely needed. The last node, a throw clause, never
 * returns anything. Thus, the program is valid as it cannot return anything
 * other than a number.
 * </p>
 * <p>
 * Regarding loops and break or continue statement, consider this program:
 * 
 * <pre>
 *   function string foo() {
 *     while<label1< (true) {
 *       while<label2> (true) {
 *         if (someCondition()) break;
 *       } 
 *       return '999';
 *     }
 *   }
 * </pre>
 * 
 * When <code>someCondition</code> is not fulfilled, the function
 * <code>foo</code> returns a {@link StringLangObject} via a return clause, in
 * accordance with the function's signature. Otherwise, it impicitly returns the
 * implicit return type the the last node, which is the implicit return type of
 * the if-clause, which is a {@link NullLangObject} implicitly returned by the
 * break clause.
 * </p>
 * <p>
 * Break and continue clauses demand a careful treatment:
 * 
 * <pre>
 *  function string foo() {
 *    var i = 0;
 *    while (i==0) {
 *      if (i == 1) break;
 *      42;
 *    }
 *  }
 *  
 *  function string bar() {
 *    var i = 0;
 *    while (i==0) {
 *      if (i == 0) break;
 *      42;
 *    }
 *  }
 * </pre>
 * 
 * Function <code>foo</code> returns a {@link NumberLangObject}, while
 * <code>bar</code> returns a {@link NullLangObject}. Yet the syntactical
 * structure of both programs is the same, they only differ semantically. To
 * solve this issue, we make the assumption each code segment could possibly
 * reached by some path and thus needs to be inspected. Thus both of the above
 * programs are invalid as they might return a {@link NumberLangObject}, which
 * violates promise of the function's signature. Note that for some programs
 * such as the one below it is possible to prove that there is unreachable code
 * and a warning or error could be issued accordingly.
 * 
 * <pre>
 *  function number foo() {
 *    return 5;
 *    return "5";
 *  }
 * </pre>
 * </p>
 * <p>
 * Regarding possibly infinite loops:
 * 
 * <pre>
 *   function string foo() {
 *     while (someCondition()) {
 *     }
 *   }
 * </pre>
 * 
 * Function <code>foo</code> implicitly returns the implicit return type of its
 * body node, which is an emtpy node that return {@link NullLangObject}.
 * <code>null</code> is compatible with a <code>string</code>, thus the above
 * code is semantically correct.
 * </p>
 * <p>
 * Concerning exceptions and try clauses:
 * 
 * <pre>
 *   function number foo() {
 *       throw exception(5);
 *   }
 *   function number bar() {
 *     try {
 *      raiseError(false);
 *     }
 *     catch(e) {
 *       return e;
 *     }
 *   }
 * </pre>
 * 
 * Function <code>foo</code> implicitly returns {@link NullLangObject}, but
 * never completes normally and is thus legal. <code>bar</code> may return
 * either an {@link ExceptionLangObject} or whatever is returned by the function
 * <code>raiseError</code>, and is thus illegal.
 * </p>
 */
public final class VariableTypeCheckVisitor implements IFormExpressionReturnVoidVisitor<NodeInfo, SemanticsException> {

	private final IVariableType[] table;

	private VariableTypeCheckVisitor(final int symbolTableSize) {
		this.table = new IVariableType[symbolTableSize];
	}

	@Override
	public NodeInfo visit(final ASTExpressionNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeInfo visit(final ASTAssignmentExpressionNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeInfo visit(final ASTNumberNode node) throws SemanticsException {
		return new NodeInfo(null, SimpleVariableType.NUMBER);
	}

	/**
	 * An array literal consists of any number of array times, eg. <code>[1,2,3]</code>.
	 * It cannot contain any return clauses. The implicit return type is the unified
	 * return type of all all array items. 
	 */
	@Override
	public NodeInfo visit(final ASTArrayNode node) throws SemanticsException {
		IVariableType type = SimpleVariableType.NULL;
		for (int i = 0; i < node.jjtGetNumChildren(); ++i) {
			final NodeInfo newInfo = node.jjtGetChild(i).jjtAccept(this);
			// When any item of the array cannot complete normally
			// the array cannot be constructed.
			if (!newInfo.hasImplicitReturnType())
				return new NodeInfo();
			type = type.union(newInfo.getImplicitReturnType());
		}
		return new NodeInfo(null, wrapInArray(type));
	}

	/**
	 * A hash literal consists of any number of hash entries, eg. <code>{3:4,0:1}</code>.
	 * It cannot contain any return clauses. The implicit return type is the unified
	 * return type of all all hash entries. 
	 */
	@Override
	public NodeInfo visit(final ASTHashNode node) throws SemanticsException {
		IVariableType typeKey = SimpleVariableType.NULL;
		IVariableType typeValue = SimpleVariableType.NULL;
		for (int i = 0; i < node.jjtGetNumChildren() - 1; i += 2) {
			// Hash key can be an identifier, as in {key: "value"}
			final NodeInfo infoKey = (node.jjtGetChild(i) instanceof ASTIdentifierNameNode)
					? new NodeInfo(null, SimpleVariableType.STRING) : node.jjtGetChild(i).jjtAccept(this);
			final NodeInfo infoValue = node.jjtGetChild(i+1).jjtAccept(this);
			// When any item of the array cannot complete normally
			// the array cannot be constructed.
			if (!(infoKey.hasImplicitReturnType() && infoKey.hasImplicitReturnType()))
				return new NodeInfo();
			typeKey = typeKey.union(infoKey.getImplicitReturnType());
			typeValue = typeValue.union(infoValue.getImplicitReturnType());
		}
		return new NodeInfo(null, wrapInHash(typeKey, typeValue));
	}

	@Override
	public NodeInfo visit(final ASTNullNode node) throws SemanticsException {
		return new NodeInfo(null, SimpleVariableType.NULL);
	}

	@Override
	public NodeInfo visit(final ASTBooleanNode node) throws SemanticsException {
		return new NodeInfo(null, SimpleVariableType.BOOLEAN);
	}

	@Override
	public NodeInfo visit(final ASTVariableNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeInfo visit(final ASTStringNode node) throws SemanticsException {
		return new NodeInfo(null, SimpleVariableType.STRING);
	}

	/**
	 * <p>
	 * A list of statements executed sequentially. However, a statement may
	 * contain a break, continue, or return clause statement which skips the
	 * remaining statements.
	 * 
	 * <pre>
	 * while (condition) {
	 * 	var i = k + 2;
	 * 	if (i > 9)
	 * 		break;
	 * 	i *= 2;
	 * }
	 * </pre>
	 * </p>
	 * <p>
	 * To determine the return type, we check whether the return types of all
	 * return points are mutually compatible; and return that type. A return
	 * point any statement at which execution of the block may end. By the
	 * language specifications, the last statement is always a return point.
	 * </p>
	 * <p>
	 * 
	 * </p>
	 * <p>
	 * 
	 * @param node
	 * @param typeNeeded
	 * @return
	 * @throws SemanticsException
	 */
	@Override
	public NodeInfo visit(final ASTStatementListNode node) throws SemanticsException {
		// TODO
		return null;
	}

	/**
	 * <p>
	 * The condition cannot contain any return clauses. When it does
	 * not return normally, the if-else-clause never returns normally.
	 * Otherwise, the implicit return type must be a boolean.
	 * </p><p>
	 * When there is an else-clause, we unify both types. In case
	 * both branches do not return normally, the if-else-clause
	 * does not return normally. When there is no else-clause,
	 * this node may return {@link NullLangObject} and we unify
	 * the if-clause type with {@link ELangObjectType#NULL}, which
	 * is a no-op. Finally, we return the unified type.
	 * </p>
	 */
	@Override
	public NodeInfo visit(final ASTIfClauseNode node) throws SemanticsException {
		final NodeInfo infoCondition = node.getConditionNode().jjtAccept(this);
		if (!infoCondition.hasImplicitReturnType())
			return infoCondition;
		if (!infoCondition.getImplicitReturnType().equalsType(SimpleVariableType.BOOLEAN))
			throw new IncompatibleBranchConditionTypeException(infoCondition.getImplicitReturnType(), node);
		final NodeInfo infoIf = node.getIfNode().jjtAccept(this);
		if (node.hasElseNode())
			infoIf.unify(node.getElseNode().jjtAccept(this));
		return infoIf;
	}

	@Override
	public NodeInfo visit(final ASTForLoopNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeInfo visit(final ASTWhileLoopNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeInfo visit(final ASTTryClauseNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeInfo visit(final ASTSwitchClauseNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeInfo visit(final ASTDoWhileLoopNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeInfo visit(final ASTExceptionNode node) throws SemanticsException {
		// TODO check if it can be coerced to string? probably not necessary
		visitChildren(node);
		return new NodeInfo(null, SimpleVariableType.EXCEPTION);
	}

	@Override
	public NodeInfo visit(final ASTThrowClauseNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeInfo visit(final ASTBreakClauseNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeInfo visit(final ASTContinueClauseNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeInfo visit(final ASTReturnClauseNode node) throws SemanticsException {
		// TODO
		return null;
	}

	@Override
	public NodeInfo visit(final ASTLogNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeInfo visit(final ASTFunctionNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeInfo visit(final ASTUnaryExpressionNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeInfo visit(final ASTPropertyExpressionNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeInfo visit(final ASTIdentifierNameNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeInfo visit(final ASTWithClauseNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeInfo visit(final ASTFunctionClauseNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeInfo visit(final ASTEmptyNode node) throws SemanticsException {
		// TODO Note to self: Needs to return the result of the last node,
		// eg. function foo(){23;;} returns a number.
		// TODO think about whether 23;;; should implicitly return
		// 23 or null.
		return null;
	}

	@Override
	public NodeInfo visit(final ASTLosNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeInfo visit(final ASTRegexNode node) throws SemanticsException {
		return new NodeInfo(null, SimpleVariableType.REGEX);
	}

	/**
	 * @see #visit(ASTIfClauseNode)
	 */
	@Override
	public NodeInfo visit(final ASTTernaryExpressionNode node) throws SemanticsException {
		final NodeInfo infoCondition = node.getConditionNode().jjtAccept(this);
		if (!infoCondition.hasImplicitReturnType())
			return infoCondition;
		if (!infoCondition.getImplicitReturnType().equalsType(SimpleVariableType.BOOLEAN))
			throw new IncompatibleBranchConditionTypeException(infoCondition.getImplicitReturnType(), node);
		final NodeInfo infoBody = node.getIfNode().jjtAccept(this);
		infoBody.unify(node.getElseNode().jjtAccept(this));
		return infoBody;
	}

	@Override
	public NodeInfo visit(final ASTParenthesisExpressionNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeInfo visit(final ASTEqualExpressionNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeInfo visit(final ASTPostUnaryExpressionNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeInfo visit(final ASTComparisonExpressionNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeInfo visit(final ASTScopeExternalNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeInfo visit(final ASTVariableDeclarationClauseNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeInfo visit(final ASTScopeManualNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeInfo visit(final ASTScopeGlobalNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeInfo visit(final ASTVariableTypeNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeInfo visit(final ASTFunctionArgumentNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Nonnull
	private static IVariableType wrapInArray(@Nullable final IVariableType type) {
		if (type == null)
			throw new NullPointerException();
		return new VariableTypeBuilder().setBasicType(ARRAY).append(type).build();
	}

	@Nonnull
	private static IVariableType wrapInHash(@Nullable final IVariableType typeKey,
			@Nullable final IVariableType typeValue) {
		if (typeKey == null || typeValue == null)
			throw new NullPointerException();
		return new VariableTypeBuilder().setBasicType(ARRAY).append(typeKey).append(typeValue).build();
	}

	private void visitChildren(@Nonnull final ASTExceptionNode node) throws SemanticsException {
		for (int i = 0; i < node.jjtGetNumChildren(); ++i)
			node.jjtAccept(this);
	}

	protected static class NodeInfo {
		@Nullable
		private IVariableType unifiedReturnType;
		@Nullable
		private IVariableType implicitReturnType;

		public NodeInfo() {
		}

		public NodeInfo(@Nullable final IVariableType unifiedReturnType,
				@Nullable final IVariableType implicitReturnType) {
			this.unifiedReturnType = unifiedReturnType;
			this.implicitReturnType = implicitReturnType;
		}

		public boolean hasUnifiedReturnType() {
			return unifiedReturnType != null;
		}

		public boolean hasImplicitReturnType() {
			return implicitReturnType != null;
		}

		@Nonnull
		public IVariableType getUnifiedReturnType() {
			if (unifiedReturnType != null)
				return unifiedReturnType;
			throw new NullPointerException();
		}

		@Nonnull
		public IVariableType getImplicitReturnType() {
			if (implicitReturnType != null)
				return implicitReturnType;
			throw new NullPointerException();		
		}

		public void unify(final NodeInfo info) {
			unifyReturnType(info.getUnifiedReturnType());
			unifiyImplicitReturnType(info.getImplicitReturnType());
		}

		public void unifyReturnType(@Nonnull final IVariableType type) {
			if (unifiedReturnType == null)
				unifiedReturnType = type;
			else
				unifiedReturnType = unifiedReturnType.union(type);
		}

		public void unifiyImplicitReturnType(@Nonnull final IVariableType type) {
			if (implicitReturnType == null)
				implicitReturnType = type;
			else
				implicitReturnType = implicitReturnType.union(type);
		}
	}
}