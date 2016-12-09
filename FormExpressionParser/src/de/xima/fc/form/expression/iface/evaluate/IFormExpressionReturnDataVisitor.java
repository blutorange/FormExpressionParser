package de.xima.fc.form.expression.iface.evaluate;

import javax.annotation.Nonnull;

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

public interface IFormExpressionReturnDataVisitor<R,T,E extends Throwable>
{
	@Nonnull public R visit(@Nonnull ASTExpressionNode node, @Nonnull T data) throws E;
	@Nonnull public R visit(@Nonnull ASTAssignmentExpressionNode node, @Nonnull T data) throws E;
	@Nonnull public R visit(@Nonnull ASTNumberNode node, @Nonnull T data) throws E;
	@Nonnull public R visit(@Nonnull ASTArrayNode node, @Nonnull T data) throws E;
	@Nonnull public R visit(@Nonnull ASTHashNode node, @Nonnull T data) throws E;
	@Nonnull public R visit(@Nonnull ASTNullNode node, @Nonnull T data) throws E;
	@Nonnull public R visit(@Nonnull ASTBooleanNode node, @Nonnull T data) throws E;
	@Nonnull public R visit(@Nonnull ASTVariableNode node, @Nonnull T data) throws E;
	@Nonnull public R visit(@Nonnull ASTStringNode node, @Nonnull T data) throws E;
	@Nonnull public R visit(@Nonnull ASTStatementListNode node, @Nonnull T data) throws E;
	@Nonnull public R visit(@Nonnull ASTIfClauseNode node, @Nonnull T data) throws E;
	@Nonnull public R visit(@Nonnull ASTForLoopNode node, @Nonnull T data) throws E;
	@Nonnull public R visit(@Nonnull ASTWhileLoopNode node, @Nonnull T data) throws E;
	@Nonnull public R visit(@Nonnull ASTTryClauseNode node, @Nonnull T data) throws E;
	@Nonnull public R visit(@Nonnull ASTSwitchClauseNode node, @Nonnull T data) throws E;
	@Nonnull public R visit(@Nonnull ASTDoWhileLoopNode node, @Nonnull T data) throws E;
	@Nonnull public R visit(@Nonnull ASTExceptionNode node, @Nonnull T data) throws E;
	@Nonnull public R visit(@Nonnull ASTThrowClauseNode node, @Nonnull T data) throws E;
	@Nonnull public R visit(@Nonnull ASTBreakClauseNode node, @Nonnull T data) throws E;
	@Nonnull public R visit(@Nonnull ASTContinueClauseNode node, @Nonnull T data) throws E;
	@Nonnull public R visit(@Nonnull ASTReturnClauseNode node, @Nonnull T data) throws E;
	@Nonnull public R visit(@Nonnull ASTLogNode node, @Nonnull T data) throws E;
	@Nonnull public R visit(@Nonnull ASTFunctionNode node, @Nonnull T data) throws E;
	@Nonnull public R visit(@Nonnull ASTUnaryExpressionNode node, @Nonnull T data) throws E;
	@Nonnull public R visit(@Nonnull ASTPropertyExpressionNode node, @Nonnull T data) throws E;
	@Nonnull public R visit(@Nonnull ASTIdentifierNameNode node, @Nonnull T data) throws E;
	@Nonnull public R visit(@Nonnull ASTWithClauseNode node, @Nonnull T data) throws E;
	@Nonnull public R visit(@Nonnull ASTFunctionClauseNode node, @Nonnull T data) throws E;
	@Nonnull public R visit(@Nonnull ASTEmptyNode node, @Nonnull T data) throws E;
	@Nonnull public R visit(@Nonnull ASTLosNode node, @Nonnull T data) throws E;
	@Nonnull public R visit(@Nonnull ASTRegexNode node, @Nonnull T data) throws E;
	@Nonnull public R visit(@Nonnull ASTTernaryExpressionNode node, @Nonnull T data) throws E;
	@Nonnull public R visit(@Nonnull ASTParenthesisExpressionNode node, @Nonnull T data) throws E;
	@Nonnull public R visit(@Nonnull ASTEqualExpressionNode node, @Nonnull T data) throws E;
	@Nonnull public R visit(@Nonnull ASTPostUnaryExpressionNode node, @Nonnull T data) throws E;
	@Nonnull public R visit(@Nonnull ASTComparisonExpressionNode node, @Nonnull T data) throws E;
	@Nonnull public R visit(@Nonnull ASTScopeExternalNode node, @Nonnull T data) throws E;
	@Nonnull public R visit(@Nonnull ASTVariableDeclarationClauseNode node, @Nonnull T data) throws E;
	@Nonnull public R visit(@Nonnull ASTScopeManualNode node, @Nonnull T data) throws E;
	@Nonnull public R visit(@Nonnull ASTScopeGlobalNode node, @Nonnull T data) throws E;
	@Nonnull public R visit(@Nonnull ASTVariableTypeNode node, @Nonnull T data) throws E;
	@Nonnull public R visit(@Nonnull ASTFunctionArgumentNode node, @Nonnull T data) throws E;
}