package de.xima.fc.form.expression.visitor;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.exception.EvaluationException;
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
import de.xima.fc.form.expression.node.ASTStatementListNode;
import de.xima.fc.form.expression.node.ASTStringNode;
import de.xima.fc.form.expression.node.ASTSwitchClauseNode;
import de.xima.fc.form.expression.node.ASTTernaryExpressionNode;
import de.xima.fc.form.expression.node.ASTThrowClauseNode;
import de.xima.fc.form.expression.node.ASTTryClauseNode;
import de.xima.fc.form.expression.node.ASTUnaryExpressionNode;
import de.xima.fc.form.expression.node.ASTVariableNode;
import de.xima.fc.form.expression.node.ASTVariableTypeDeclarationNode;
import de.xima.fc.form.expression.node.ASTWhileLoopNode;
import de.xima.fc.form.expression.node.ASTWithClauseNode;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.Void;

public class VariableScopeCheckVisitor implements IFormExpressionParserVisitor<Void, IEvaluationContext, EvaluationException>{

	@Override
	public Void visit(final ASTExpressionNode node, final IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		throw new RuntimeException(CmnCnst.Error.TODO);
	}

	@Override
	public Void visit(final ASTAssignmentExpressionNode node, final IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		throw new RuntimeException(CmnCnst.Error.TODO);
	}

	@Override
	public Void visit(final ASTNumberNode node, final IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		throw new RuntimeException(CmnCnst.Error.TODO);
	}

	@Override
	public Void visit(final ASTArrayNode node, final IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		throw new RuntimeException(CmnCnst.Error.TODO);
	}

	@Override
	public Void visit(final ASTHashNode node, final IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		throw new RuntimeException(CmnCnst.Error.TODO);
	}

	@Override
	public Void visit(final ASTNullNode node, final IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		throw new RuntimeException(CmnCnst.Error.TODO);
	}

	@Override
	public Void visit(final ASTBooleanNode node, final IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		throw new RuntimeException(CmnCnst.Error.TODO);
	}

	@Override
	public Void visit(final ASTVariableNode node, final IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		throw new RuntimeException(CmnCnst.Error.TODO);
	}

	@Override
	public Void visit(final ASTStringNode node, final IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		throw new RuntimeException(CmnCnst.Error.TODO);
	}

	@Override
	public Void visit(final ASTStatementListNode node, final IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		throw new RuntimeException(CmnCnst.Error.TODO);
	}

	@Override
	public Void visit(final ASTIfClauseNode node, final IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		throw new RuntimeException(CmnCnst.Error.TODO);
	}

	@Override
	public Void visit(final ASTForLoopNode node, final IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		throw new RuntimeException(CmnCnst.Error.TODO);
	}

	@Override
	public Void visit(final ASTWhileLoopNode node, final IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		throw new RuntimeException(CmnCnst.Error.TODO);
	}

	@Override
	public Void visit(final ASTTryClauseNode node, final IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		throw new RuntimeException(CmnCnst.Error.TODO);
	}

	@Override
	public Void visit(final ASTSwitchClauseNode node, final IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		throw new RuntimeException(CmnCnst.Error.TODO);
	}

	@Override
	public Void visit(final ASTDoWhileLoopNode node, final IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		throw new RuntimeException(CmnCnst.Error.TODO);
	}

	@Override
	public Void visit(final ASTExceptionNode node, final IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		throw new RuntimeException(CmnCnst.Error.TODO);
	}

	@Override
	public Void visit(final ASTThrowClauseNode node, final IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		throw new RuntimeException(CmnCnst.Error.TODO);
	}

	@Override
	public Void visit(final ASTBreakClauseNode node, final IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		throw new RuntimeException(CmnCnst.Error.TODO);
	}

	@Override
	public Void visit(final ASTContinueClauseNode node, final IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		throw new RuntimeException(CmnCnst.Error.TODO);
	}

	@Override
	public Void visit(final ASTReturnClauseNode node, final IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		throw new RuntimeException(CmnCnst.Error.TODO);
	}

	@Override
	public Void visit(final ASTLogNode node, final IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		throw new RuntimeException(CmnCnst.Error.TODO);
	}

	@Override
	public Void visit(final ASTFunctionNode node, final IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		throw new RuntimeException(CmnCnst.Error.TODO);
	}

	@Override
	public Void visit(final ASTUnaryExpressionNode node, final IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		throw new RuntimeException(CmnCnst.Error.TODO);
	}

	@Override
	public Void visit(final ASTPropertyExpressionNode node, final IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		throw new RuntimeException(CmnCnst.Error.TODO);
	}

	@Override
	public Void visit(final ASTIdentifierNameNode node, final IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		throw new RuntimeException(CmnCnst.Error.TODO);
	}

	@Override
	public Void visit(final ASTWithClauseNode node, final IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		throw new RuntimeException(CmnCnst.Error.TODO);
	}

	@Override
	public Void visit(final ASTFunctionClauseNode node, final IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		throw new RuntimeException(CmnCnst.Error.TODO);
	}

	@Override
	public Void visit(final ASTEmptyNode node, final IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		throw new RuntimeException(CmnCnst.Error.TODO);
	}

	@Override
	public Void visit(final ASTLosNode node, final IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		throw new RuntimeException(CmnCnst.Error.TODO);
	}

	@Override
	public Void visit(final ASTRegexNode node, final IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		return Void.NULL;
	}

	@Override
	public Void visit(final ASTTernaryExpressionNode node, final IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		throw new RuntimeException(CmnCnst.Error.TODO);
	}

	@Override
	public Void visit(final ASTParenthesisExpressionNode node, final IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		throw new RuntimeException(CmnCnst.Error.TODO);
	}

	@Override
	public Void visit(final ASTEqualExpressionNode node, final IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		throw new RuntimeException(CmnCnst.Error.TODO);
	}

	@Override
	public Void visit(final ASTPostUnaryExpressionNode node, final IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		return Void.NULL;
	}

	@Override
	public Void visit(final ASTComparisonExpressionNode node, final IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		return Void.NULL;
	}

	@Override
	public Void visit(final ASTVariableTypeDeclarationNode node, final IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		throw new RuntimeException(CmnCnst.Error.TODO);
	}
}