package de.xima.fc.form.expression.visitor;

import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.node.ASTArrayNode;
import de.xima.fc.form.expression.node.ASTBooleanNode;
import de.xima.fc.form.expression.node.ASTDotExpressionNode;
import de.xima.fc.form.expression.node.ASTExpressionNode;
import de.xima.fc.form.expression.node.ASTForLoopNode;
import de.xima.fc.form.expression.node.ASTHashNode;
import de.xima.fc.form.expression.node.ASTIfClauseNode;
import de.xima.fc.form.expression.node.ASTNullNode;
import de.xima.fc.form.expression.node.ASTNumberNode;
import de.xima.fc.form.expression.node.ASTParenthesesFunction;
import de.xima.fc.form.expression.node.ASTPlainFunction;
import de.xima.fc.form.expression.node.ASTProgramNode;
import de.xima.fc.form.expression.node.ASTStatementListNode;
import de.xima.fc.form.expression.node.ASTStringNode;
import de.xima.fc.form.expression.node.ASTSwitchClauseNode;
import de.xima.fc.form.expression.node.ASTTryClauseNode;
import de.xima.fc.form.expression.node.ASTWhileLoopNode;

public class UnparseVisitor implements IFormExpressionParserVisitor<String, Void>{

	@Override
	public String visit(final ASTExpressionNode node, final Void data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(final ASTDotExpressionNode node, final Void data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(final ASTParenthesesFunction node, final Void data) throws EvaluationException {
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
	public String visit(final ASTPlainFunction node, final Void data) throws EvaluationException {
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
	public String visit(final ASTProgramNode node, final Void data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

}