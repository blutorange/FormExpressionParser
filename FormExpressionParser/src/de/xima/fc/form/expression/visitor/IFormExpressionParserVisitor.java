package de.xima.fc.form.expression.visitor;

import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.node.ASTArrayNode;
import de.xima.fc.form.expression.node.ASTAssignmentExpressionNode;
import de.xima.fc.form.expression.node.ASTBooleanNode;
import de.xima.fc.form.expression.node.ASTBreakClauseNode;
import de.xima.fc.form.expression.node.ASTContinueClauseNode;
import de.xima.fc.form.expression.node.ASTDoWhileLoopNode;
import de.xima.fc.form.expression.node.ASTDotExpressionNode;
import de.xima.fc.form.expression.node.ASTExceptionNode;
import de.xima.fc.form.expression.node.ASTExpressionNode;
import de.xima.fc.form.expression.node.ASTForLoopNode;
import de.xima.fc.form.expression.node.ASTHashNode;
import de.xima.fc.form.expression.node.ASTIfClauseNode;
import de.xima.fc.form.expression.node.ASTLogNode;
import de.xima.fc.form.expression.node.ASTNullNode;
import de.xima.fc.form.expression.node.ASTNumberNode;
import de.xima.fc.form.expression.node.ASTParenthesesFunction;
import de.xima.fc.form.expression.node.ASTPlainFunction;
import de.xima.fc.form.expression.node.ASTReturnClauseNode;
import de.xima.fc.form.expression.node.ASTStatementListNode;
import de.xima.fc.form.expression.node.ASTStringNode;
import de.xima.fc.form.expression.node.ASTSwitchClauseNode;
import de.xima.fc.form.expression.node.ASTThrowClauseNode;
import de.xima.fc.form.expression.node.ASTTryClauseNode;
import de.xima.fc.form.expression.node.ASTWhileLoopNode;

public interface IFormExpressionParserVisitor<R,T>
{
	public R visit(ASTExpressionNode node, T data) throws EvaluationException;
	public R visit(ASTAssignmentExpressionNode node, T data) throws EvaluationException;
	public R visit(ASTDotExpressionNode node, T data) throws EvaluationException;
	public R visit(ASTParenthesesFunction node, T data) throws EvaluationException;
	public R visit(ASTNumberNode node, T data) throws EvaluationException;
	public R visit(ASTArrayNode node, T data) throws EvaluationException;
	public R visit(ASTHashNode node, T data) throws EvaluationException;
	public R visit(ASTNullNode node, T data) throws EvaluationException;
	public R visit(ASTBooleanNode node, T data) throws EvaluationException;
	public R visit(ASTPlainFunction node, T data) throws EvaluationException;
	public R visit(ASTStringNode node, T data) throws EvaluationException;
	public R visit(ASTStatementListNode node, T data) throws EvaluationException;
	public R visit(ASTIfClauseNode node, T data) throws EvaluationException;
	public R visit(ASTForLoopNode node, T data) throws EvaluationException;
	public R visit(ASTWhileLoopNode node, T data) throws EvaluationException;
	public R visit(ASTTryClauseNode node, T data) throws EvaluationException;
	public R visit(ASTSwitchClauseNode node, T data) throws EvaluationException;
	public R visit(ASTDoWhileLoopNode node, T data) throws EvaluationException;
	public R visit(ASTExceptionNode node, T data) throws EvaluationException;
	public R visit(ASTThrowClauseNode node, T data) throws EvaluationException;
	public R visit(ASTBreakClauseNode node, T data) throws EvaluationException;
	public R visit(ASTContinueClauseNode node, T data) throws EvaluationException;
	public R visit(ASTReturnClauseNode node, T data) throws EvaluationException;
	public R visit(ASTLogNode node, T data) throws EvaluationException;
}
