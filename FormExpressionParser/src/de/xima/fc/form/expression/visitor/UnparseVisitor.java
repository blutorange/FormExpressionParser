package de.xima.fc.form.expression.visitor;

import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.node.ASTArrayNode;
import de.xima.fc.form.expression.node.ASTAssignmentExpressionNode;
import de.xima.fc.form.expression.node.ASTBooleanNode;
import de.xima.fc.form.expression.node.ASTBreakClauseNode;
import de.xima.fc.form.expression.node.ASTContinueClauseNode;
import de.xima.fc.form.expression.node.ASTDoWhileLoopNode;
import de.xima.fc.form.expression.node.ASTExceptionNode;
import de.xima.fc.form.expression.node.ASTExpressionNode;
import de.xima.fc.form.expression.node.ASTForLoopNode;
import de.xima.fc.form.expression.node.ASTFunctionNode;
import de.xima.fc.form.expression.node.ASTHashNode;
import de.xima.fc.form.expression.node.ASTIfClauseNode;
import de.xima.fc.form.expression.node.ASTLogNode;
import de.xima.fc.form.expression.node.ASTNullNode;
import de.xima.fc.form.expression.node.ASTNumberNode;
import de.xima.fc.form.expression.node.ASTPropertyExpressionNode;
import de.xima.fc.form.expression.node.ASTReturnClauseNode;
import de.xima.fc.form.expression.node.ASTStatementListNode;
import de.xima.fc.form.expression.node.ASTStringNode;
import de.xima.fc.form.expression.node.ASTSwitchClauseNode;
import de.xima.fc.form.expression.node.ASTThrowClauseNode;
import de.xima.fc.form.expression.node.ASTTryClauseNode;
import de.xima.fc.form.expression.node.ASTUnaryExpressionNode;
import de.xima.fc.form.expression.node.ASTVariableNode;
import de.xima.fc.form.expression.node.ASTWhileLoopNode;

public class UnparseVisitor implements IFormExpressionParserVisitor<String, Void>{

	private final int indentationLevel;

	public UnparseVisitor() {
		this(4);
	}

	public UnparseVisitor(final int indentationLevel) {
		this.indentationLevel = indentationLevel;
	}

	@Override
	public String visit(final ASTExpressionNode node, final Void data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(final ASTNumberNode node, final Void data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(final ASTArrayNode node, final Void data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(final ASTHashNode node, final Void data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(final ASTNullNode node, final Void data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(final ASTBooleanNode node, final Void data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(final ASTVariableNode node, final Void data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(final ASTStringNode node, final Void data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(final ASTStatementListNode astStatementListNode, final Void data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(final ASTIfClauseNode node, final Void data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(final ASTForLoopNode node, final Void data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(final ASTWhileLoopNode node, final Void data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(final ASTTryClauseNode node, final Void data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(final ASTSwitchClauseNode node, final Void data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(final ASTDoWhileLoopNode node, final Void data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(final ASTAssignmentExpressionNode node, final Void data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(final ASTExceptionNode node, final Void data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(final ASTThrowClauseNode node, final Void data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(final ASTBreakClauseNode node, final Void data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(final ASTContinueClauseNode node, final Void data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(final ASTReturnClauseNode node, final Void data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(final ASTLogNode node, final Void data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(final ASTFunctionNode node, final Void data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(final ASTUnaryExpressionNode node, final Void data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(final ASTPropertyExpressionNode node, final Void data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

}
