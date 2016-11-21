package de.xima.fc.form.expression.visitor;

import de.xima.fc.form.expression.exception.parse.SemanticsException;
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
import de.xima.fc.form.expression.util.CmnCnst;

public class CompileTimeConstantCheckVisitor implements IFormExpressionReturnVoidVisitor<Boolean, SemanticsException> {

	@Override
	public Boolean visit(final ASTExpressionNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(final ASTAssignmentExpressionNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(final ASTNumberNode node) throws SemanticsException {
		return CmnCnst.NonnullConstant.BOOLEAN_TRUE;
	}

	@Override
	public Boolean visit(final ASTArrayNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(final ASTHashNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(final ASTNullNode node) throws SemanticsException {
		return CmnCnst.NonnullConstant.BOOLEAN_TRUE;
	}

	@Override
	public Boolean visit(final ASTBooleanNode node) throws SemanticsException {
		return CmnCnst.NonnullConstant.BOOLEAN_TRUE;
	}

	@Override
	public Boolean visit(final ASTVariableNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(final ASTStringNode node) throws SemanticsException {
		return CmnCnst.NonnullConstant.BOOLEAN_TRUE;
	}

	@Override
	public Boolean visit(final ASTStatementListNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(final ASTIfClauseNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(final ASTForLoopNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(final ASTWhileLoopNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(final ASTTryClauseNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(final ASTSwitchClauseNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(final ASTDoWhileLoopNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(final ASTExceptionNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(final ASTThrowClauseNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(final ASTBreakClauseNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(final ASTContinueClauseNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(final ASTReturnClauseNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(final ASTLogNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(final ASTFunctionNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(final ASTUnaryExpressionNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(final ASTPropertyExpressionNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(final ASTIdentifierNameNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(final ASTWithClauseNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(final ASTFunctionClauseNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(final ASTEmptyNode node) throws SemanticsException {
		return CmnCnst.NonnullConstant.BOOLEAN_TRUE;
	}

	@Override
	public Boolean visit(final ASTLosNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(final ASTRegexNode node) throws SemanticsException {
		return CmnCnst.NonnullConstant.BOOLEAN_TRUE;
	}

	@Override
	public Boolean visit(final ASTTernaryExpressionNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(final ASTParenthesisExpressionNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(final ASTEqualExpressionNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(final ASTPostUnaryExpressionNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(final ASTComparisonExpressionNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(final ASTScopeExternalNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(final ASTVariableDeclarationClauseNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(final ASTScopeManualNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(final ASTScopeGlobalNode node) throws SemanticsException {
		// TODO Auto-generated method stub
		return null;
	}

}
