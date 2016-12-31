package de.xima.fc.form.expression.visitor;

import java.util.Collection;

import javax.annotation.Nullable;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.exception.FormExpressionException;
import de.xima.fc.form.expression.exception.parse.HeaderAssignmentNotCompileTimeConstantException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionReturnVoidVisitor;
import de.xima.fc.form.expression.iface.parse.IHeaderNode;
import de.xima.fc.form.expression.iface.parse.IScopeDefinitions;
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

@NonNullByDefault
public class CompileTimeConstantCheckVisitor
		implements IFormExpressionReturnVoidVisitor<Boolean, FormExpressionException> {
	private CompileTimeConstantCheckVisitor() {
	}

	private Boolean visitChildren(final Node node) {
		for (int i = 0; i < node.jjtGetNumChildren(); ++i) {
			if (!node.jjtGetChild(i).jjtAccept(this).booleanValue())
				return CmnCnst.NonnullConstant.BOOLEAN_FALSE;
		}
		return CmnCnst.NonnullConstant.BOOLEAN_TRUE;
	}

	@Override
	public Boolean visit(final ASTExpressionNode node) {
		return visitChildren(node);
	}

	@Override
	public Boolean visit(final ASTAssignmentExpressionNode node) {
		// Assignment is not compile-time constant.
		// When there is only one child (which should not happen...),
		// there is no assignment.
		return node.jjtGetNumChildren() <= 1 ? CmnCnst.NonnullConstant.BOOLEAN_TRUE
				: CmnCnst.NonnullConstant.BOOLEAN_TRUE;
	}

	@Override
	public Boolean visit(final ASTNumberNode node) {
		return CmnCnst.NonnullConstant.BOOLEAN_TRUE;
	}

	@Override
	public Boolean visit(final ASTArrayNode node) {
		return visitChildren(node);
	}

	@Override
	public Boolean visit(final ASTHashNode node) {
		return visitChildren(node);
	}

	@Override
	public Boolean visit(final ASTNullNode node) {
		return CmnCnst.NonnullConstant.BOOLEAN_TRUE;
	}

	@Override
	public Boolean visit(final ASTBooleanNode node) {
		return CmnCnst.NonnullConstant.BOOLEAN_TRUE;
	}

	@Override
	public Boolean visit(final ASTVariableNode node) {
		return CmnCnst.NonnullConstant.BOOLEAN_FALSE;
	}

	@Override
	public Boolean visit(final ASTStringNode node) {
		return visitChildren(node);
	}

	@Override
	public Boolean visit(final ASTStringCharactersNode node) {
		return CmnCnst.NonnullConstant.BOOLEAN_TRUE;
	}

	@Override
	public Boolean visit(final ASTStatementListNode node) {
		return visitChildren(node);
	}

	@Override
	public Boolean visit(final ASTIfClauseNode node) {
		return visitChildren(node);
	}

	@Override
	public Boolean visit(final ASTForLoopNode node) {
		// non-constant because we do not know whether it terminates
		return CmnCnst.NonnullConstant.BOOLEAN_FALSE;
	}

	@Override
	public Boolean visit(final ASTWhileLoopNode node) {
		// non-constant because we do not know whether it terminates
		return CmnCnst.NonnullConstant.BOOLEAN_FALSE;
	}

	@Override
	public Boolean visit(final ASTTryClauseNode node) {
		return visitChildren(node);
	}

	@Override
	public Boolean visit(final ASTSwitchClauseNode node) {
		return visitChildren(node);
	}

	@Override
	public Boolean visit(final ASTDoWhileLoopNode node) {
		// non-constant because we do not know whether it terminates
		return CmnCnst.NonnullConstant.BOOLEAN_FALSE;
	}

	@Override
	public Boolean visit(final ASTExceptionNode node) {
		// Exception is constant when the error message is constant
		if (node.hasErrorMessage())
			return node.getErrorMessageNode().jjtAccept(this);
		return CmnCnst.NonnullConstant.BOOLEAN_TRUE;
	}

	@Override
	public Boolean visit(final ASTThrowClauseNode node) {
		// Throwing an exception is not considered constant, but
		// it might be caught by a catch block. The try-catch-statement
		// is then considered constant.
		return visitChildren(node);
	}

	@Override
	public Boolean visit(final ASTBreakClauseNode node) {
		return CmnCnst.NonnullConstant.BOOLEAN_TRUE;
	}

	@Override
	public Boolean visit(final ASTContinueClauseNode node) {
		return CmnCnst.NonnullConstant.BOOLEAN_TRUE;
	}

	@Override
	public Boolean visit(final ASTReturnClauseNode node) {
		return CmnCnst.NonnullConstant.BOOLEAN_TRUE;
	}

	@Override
	public Boolean visit(final ASTLogNode node) {
		// Not constant because of side effects.
		return CmnCnst.NonnullConstant.BOOLEAN_FALSE;
	}

	@Override
	public Boolean visit(final ASTFunctionNode node) {
		return CmnCnst.NonnullConstant.BOOLEAN_TRUE;
	}

	@Override
	public Boolean visit(final ASTUnaryExpressionNode node) {
		// i++ is definitely not compile-time constant because it involved
		// assignment
		// i++ => i = i + 1
		return Boolean.valueOf(!node.getUnaryMethod().isAssigning() && visitChildren(node).booleanValue());
	}

	// TODO [1,2,3][0] is constant, as is {foo:"bar"}.foo
	// ()=>{42;}() might be considered constant as well
	@Override
	public Boolean visit(final ASTPropertyExpressionNode node) {
		// Function calls are not compile time constant.
		return CmnCnst.NonnullConstant.BOOLEAN_FALSE;
	}

	@Override
	public Boolean visit(final ASTIdentifierNameNode node) {
		return CmnCnst.NonnullConstant.BOOLEAN_TRUE;
	}

	@Override
	public Boolean visit(final ASTWithClauseNode node) {
		return visitChildren(node);
	}

	@Override
	public Boolean visit(final ASTFunctionClauseNode node) {
		return CmnCnst.NonnullConstant.BOOLEAN_TRUE;
	}

	@Override
	public Boolean visit(final ASTEmptyNode node) {
		return CmnCnst.NonnullConstant.BOOLEAN_TRUE;
	}

	@Override
	public Boolean visit(final ASTLosNode node) {
		// Not constant because of side effects (writing output).
		return CmnCnst.NonnullConstant.BOOLEAN_FALSE;
	}

	@Override
	public Boolean visit(final ASTRegexNode node) {
		return CmnCnst.NonnullConstant.BOOLEAN_TRUE;
	}

	@Override
	public Boolean visit(final ASTTernaryExpressionNode node) {
		return visitChildren(node);
	}

	@Override
	public Boolean visit(final ASTParenthesisExpressionNode node) {
		return visitChildren(node);
	}

	@Override
	public Boolean visit(final ASTEqualExpressionNode node) {
		return visitChildren(node);
	}

	@Override
	public Boolean visit(final ASTPostUnaryExpressionNode node) {
		// i++ is definitely not compile-time constant because it involves
		// assignment
		// i++ => i = i + 1
		return Boolean.valueOf(!node.getUnaryMethod().isAssigning());
	}

	@Override
	public Boolean visit(final ASTComparisonExpressionNode node) {
		return visitChildren(node);
	}

	@Override
	public Boolean visit(final ASTScopeExternalNode node) {
		// Meta language construct, not relevant for evaluation.
		return CmnCnst.NonnullConstant.BOOLEAN_FALSE;
	}

	@Override
	public Boolean visit(final ASTVariableDeclarationClauseNode node) {
		return CmnCnst.NonnullConstant.BOOLEAN_FALSE;
	}

	@Override
	public Boolean visit(final ASTScopeManualNode node) {
		// Meta language construct, not relevant for evaluation.
		return CmnCnst.NonnullConstant.BOOLEAN_FALSE;
	}

	@Override
	public Boolean visit(final ASTScopeGlobalNode node) {
		// Meta language construct, not relevant for evaluation.
		return CmnCnst.NonnullConstant.BOOLEAN_FALSE;
	}

	@Override
	public Boolean visit(final ASTVariableTypeNode node) throws RuntimeException {
		return CmnCnst.NonnullConstant.BOOLEAN_FALSE;
	}

	@Override
	public Boolean visit(final ASTFunctionArgumentNode node) throws RuntimeException {
		return CmnCnst.NonnullConstant.BOOLEAN_FALSE;
	}

	public static void check(final IScopeDefinitions scopeDef) throws HeaderAssignmentNotCompileTimeConstantException {
		final CompileTimeConstantCheckVisitor visitor = new CompileTimeConstantCheckVisitor();
		checkScopeDefsConstancy(scopeDef.getGlobal(), visitor);
		for (final Collection<IHeaderNode> coll : scopeDef.getManual().values())
			checkScopeDefsConstancy(coll, visitor);
	}

	private static void checkScopeDefsConstancy(@Nullable final Collection<IHeaderNode> collection,
			final CompileTimeConstantCheckVisitor visitor) throws HeaderAssignmentNotCompileTimeConstantException {
		if (collection == null)
			return;
		for (final IHeaderNode hn : collection)
			if (!hn.getHeaderValueNode().jjtAccept(visitor).booleanValue())
				throw new HeaderAssignmentNotCompileTimeConstantException(null, hn.getVariableName(), hn.getHeaderValueNode());
	}
}