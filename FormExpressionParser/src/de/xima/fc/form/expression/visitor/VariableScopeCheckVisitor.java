package de.xima.fc.form.expression.visitor;

import de.xima.fc.form.expression.context.IEvaluationContext;
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
import de.xima.fc.form.expression.node.ASTFunctionNode;
import de.xima.fc.form.expression.node.ASTHashNode;
import de.xima.fc.form.expression.node.ASTIfClauseNode;
import de.xima.fc.form.expression.node.ASTLogNode;
import de.xima.fc.form.expression.node.ASTNullNode;
import de.xima.fc.form.expression.node.ASTNumberNode;
import de.xima.fc.form.expression.node.ASTReturnClauseNode;
import de.xima.fc.form.expression.node.ASTStatementListNode;
import de.xima.fc.form.expression.node.ASTStringNode;
import de.xima.fc.form.expression.node.ASTSwitchClauseNode;
import de.xima.fc.form.expression.node.ASTThrowClauseNode;
import de.xima.fc.form.expression.node.ASTTryClauseNode;
import de.xima.fc.form.expression.node.ASTUnaryExpressionNode;
import de.xima.fc.form.expression.node.ASTVariableNode;
import de.xima.fc.form.expression.node.ASTWhileLoopNode;

public class VariableScopeCheckVisitor implements IFormExpressionParserVisitor<Void, IEvaluationContext>{

	@Override
	public Void visit(ASTExpressionNode node, IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ASTAssignmentExpressionNode node, IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ASTDotExpressionNode node, IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ASTNumberNode node, IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ASTArrayNode node, IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ASTHashNode node, IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ASTNullNode node, IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ASTBooleanNode node, IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ASTVariableNode node, IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ASTStringNode node, IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ASTStatementListNode node, IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ASTIfClauseNode node, IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ASTForLoopNode node, IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ASTWhileLoopNode node, IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ASTTryClauseNode node, IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ASTSwitchClauseNode node, IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ASTDoWhileLoopNode node, IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ASTExceptionNode node, IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ASTThrowClauseNode node, IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ASTBreakClauseNode node, IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ASTContinueClauseNode node, IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ASTReturnClauseNode node, IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ASTLogNode node, IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ASTFunctionNode node, IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ASTUnaryExpressionNode node, IEvaluationContext data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

}
