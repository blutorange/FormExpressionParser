package de.xima.fc.form.expression.visitor;

import de.xima.fc.form.expression.node.ASTArrayNode;
import de.xima.fc.form.expression.node.ASTAssignmentExpressionNode;
import de.xima.fc.form.expression.node.ASTBooleanNode;
import de.xima.fc.form.expression.node.ASTBreakClauseNode;
import de.xima.fc.form.expression.node.ASTContinueClauseNode;
import de.xima.fc.form.expression.node.ASTDoWhileLoopNode;
import de.xima.fc.form.expression.node.ASTEmptyNode;
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
import de.xima.fc.form.expression.node.ASTPropertyExpressionNode;
import de.xima.fc.form.expression.node.ASTReturnClauseNode;
import de.xima.fc.form.expression.node.ASTStatementListNode;
import de.xima.fc.form.expression.node.ASTStringNode;
import de.xima.fc.form.expression.node.ASTSwitchClauseNode;
import de.xima.fc.form.expression.node.ASTTemplateNode;
import de.xima.fc.form.expression.node.ASTThrowClauseNode;
import de.xima.fc.form.expression.node.ASTTryClauseNode;
import de.xima.fc.form.expression.node.ASTUnaryExpressionNode;
import de.xima.fc.form.expression.node.ASTVariableNode;
import de.xima.fc.form.expression.node.ASTWhileLoopNode;
import de.xima.fc.form.expression.node.ASTWithClauseNode;

public interface IFormExpressionParserVisitor<R,T,E extends Throwable>
{
	public R visit(ASTExpressionNode node, T data) throws E;
	public R visit(ASTAssignmentExpressionNode node, T data) throws E;
	public R visit(ASTNumberNode node, T data) throws E;
	public R visit(ASTArrayNode node, T data) throws E;
	public R visit(ASTHashNode node, T data) throws E;
	public R visit(ASTNullNode node, T data) throws E;
	public R visit(ASTBooleanNode node, T data) throws E;
	public R visit(ASTVariableNode node, T data) throws E;
	public R visit(ASTStringNode node, T data) throws E;
	public R visit(ASTStatementListNode node, T data) throws E;
	public R visit(ASTIfClauseNode node, T data) throws E;
	public R visit(ASTForLoopNode node, T data) throws E;
	public R visit(ASTWhileLoopNode node, T data) throws E;
	public R visit(ASTTryClauseNode node, T data) throws E;
	public R visit(ASTSwitchClauseNode node, T data) throws E;
	public R visit(ASTDoWhileLoopNode node, T data) throws E;
	public R visit(ASTExceptionNode node, T data) throws E;
	public R visit(ASTThrowClauseNode node, T data) throws E;
	public R visit(ASTBreakClauseNode node, T data) throws E;
	public R visit(ASTContinueClauseNode node, T data) throws E;
	public R visit(ASTReturnClauseNode node, T data) throws E;
	public R visit(ASTLogNode node, T data) throws E;
	public R visit(ASTFunctionNode node, T data) throws E;
	public R visit(ASTUnaryExpressionNode node, T data) throws E;
	public R visit(ASTPropertyExpressionNode node, T data) throws E;
	public R visit(ASTIdentifierNameNode node, T data) throws E;
	public R visit(ASTWithClauseNode node, T data) throws E;
	public R visit(ASTFunctionClauseNode node, T data) throws E;
	public R visit(ASTEmptyNode node, T data) throws E;
	public R visit(ASTLosNode node, T data) throws E;
	public R visit(ASTTemplateNode node, T data) throws E;
}
