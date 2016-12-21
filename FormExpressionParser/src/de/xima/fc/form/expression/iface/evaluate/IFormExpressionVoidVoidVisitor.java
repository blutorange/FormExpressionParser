package de.xima.fc.form.expression.iface.evaluate;

import javax.annotation.ParametersAreNonnullByDefault;

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

@ParametersAreNonnullByDefault
public interface IFormExpressionVoidVoidVisitor<E extends Throwable>
{
	public void visit(ASTExpressionNode node) throws E;
	public void visit(ASTAssignmentExpressionNode node) throws E;
	public void visit(ASTNumberNode node) throws E;
	public void visit(ASTArrayNode node) throws E;
	public void visit(ASTHashNode node) throws E;
	public void visit(ASTNullNode node) throws E;
	public void visit(ASTBooleanNode node) throws E;
	public void visit(ASTVariableNode node) throws E;
	public void visit(ASTStringNode node) throws E;
	public void visit(ASTStatementListNode node) throws E;
	public void visit(ASTIfClauseNode node) throws E;
	public void visit(ASTForLoopNode node) throws E;
	public void visit(ASTWhileLoopNode node) throws E;
	public void visit(ASTTryClauseNode node) throws E;
	public void visit(ASTSwitchClauseNode node) throws E;
	public void visit(ASTDoWhileLoopNode node) throws E;
	public void visit(ASTExceptionNode node) throws E;
	public void visit(ASTThrowClauseNode node) throws E;
	public void visit(ASTBreakClauseNode node) throws E;
	public void visit(ASTContinueClauseNode node) throws E;
	public void visit(ASTReturnClauseNode node) throws E;
	public void visit(ASTLogNode node) throws E;
	public void visit(ASTFunctionNode node) throws E;
	public void visit(ASTUnaryExpressionNode node) throws E;
	public void visit(ASTPropertyExpressionNode node) throws E;
	public void visit(ASTIdentifierNameNode node) throws E;
	public void visit(ASTWithClauseNode node) throws E;
	public void visit(ASTFunctionClauseNode node) throws E;
	public void visit(ASTEmptyNode node) throws E;
	public void visit(ASTLosNode node) throws E;
	public void visit(ASTRegexNode node) throws E;
	public void visit(ASTTernaryExpressionNode node) throws E;
	public void visit(ASTParenthesisExpressionNode node) throws E;
	public void visit(ASTEqualExpressionNode node) throws E;
	public void visit(ASTPostUnaryExpressionNode node) throws E;
	public void visit(ASTComparisonExpressionNode node) throws E;
	public void visit(ASTScopeExternalNode node) throws E;
	public void visit(ASTVariableDeclarationClauseNode node) throws E;
	public void visit(ASTScopeManualNode node) throws E;
	public void visit(ASTScopeGlobalNode node) throws E;
	public void visit(ASTVariableTypeNode node) throws E;
	public void visit(ASTFunctionArgumentNode node) throws E;
	public void visit(ASTStringCharactersNode node) throws E;
}