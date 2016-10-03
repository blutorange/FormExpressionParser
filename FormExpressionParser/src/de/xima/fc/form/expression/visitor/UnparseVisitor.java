package de.xima.fc.form.expression.visitor;

import org.apache.commons.lang3.StringUtils;

import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
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
import de.xima.fc.form.expression.util.CmnCnst;

public class UnparseVisitor implements IFormExpressionParserVisitor<StringBuilder, Integer, EvaluationException>{

	private StringBuilder sb;
	private final String indentPrefix;

	public UnparseVisitor() {
		this(2);
	}

	public UnparseVisitor(final int indentationLevel) {
		this.indentPrefix = StringUtils.repeat(' ', indentationLevel);
		reinit();
	}

	public void reinit() {
		sb = new StringBuilder();
	}

	@Override
	public StringBuilder visit(final ASTExpressionNode node, final Integer level) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuilder visit(final ASTNumberNode node, final Integer level) throws EvaluationException {
		sb.append(node.getDoubleValue());
		return sb;
	}

	@Override
	public StringBuilder visit(final ASTArrayNode node, final Integer level) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuilder visit(final ASTHashNode node, final Integer level) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuilder visit(final ASTNullNode node, final Integer level) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuilder visit(final ASTBooleanNode node, final Integer level) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuilder visit(final ASTVariableNode node, final Integer level) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuilder visit(final ASTStringNode node, final Integer level) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuilder visit(final ASTStatementListNode node, final Integer level) throws EvaluationException {
		for (final Node n : node.getChildArray()) {
			indent(level);
			n.jjtAccept(this, level);
			sb.append(CmnCnst.SYNTAX_SEMI_COLON).append(StringUtils.LF);
		}
		return sb;
	}

	private void indent(final Integer level) {
		for (int i = level; i --> 0;) sb.append(indentPrefix);
	}

	private void blockOrClause(final Node node, final Integer level) {
		if (node.jjtGetNumChildren() > 1) {
			sb.append(CmnCnst.SYNTAX_BRACE_OPEN).append(StringUtils.LF);
			node.jjtAccept(this, level).append(StringUtils.LF);
			sb.append(CmnCnst.SYNTAX_BRACE_CLOSE);
		}
		else {
			node.jjtAccept(this, level);
		}
	}

	@Override
	public StringBuilder visit(final ASTIfClauseNode node, final Integer level) throws EvaluationException {
		final Integer next = level+1;
		// <if (>
		indent(level);
		sb.append(CmnCnst.SYNTAX_IF).append(StringUtils.EMPTY).append(CmnCnst.SYNTAX_PAREN_OPEN);
		// <...>
		node.jjtGetChild(0).jjtAccept(this, next);
		// <) >
		sb.append(CmnCnst.SYNTAX_PAREN_CLOSE).append(StringUtils.SPACE);
		// blockOrClause
		blockOrClause(node.jjtGetChild(1), next);
		if (node.jjtGetNumChildren() > 2) {
			sb.append(StringUtils.LF);
			indent(level);
			sb.append(CmnCnst.SYNTAX_ELSE).append(StringUtils.LF);
			node.jjtGetChild(2).jjtAccept(this, next);
		}
		return sb;
	}

	@Override
	public StringBuilder visit(final ASTForLoopNode node, final Integer level) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuilder visit(final ASTWhileLoopNode node, final Integer level) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuilder visit(final ASTTryClauseNode node, final Integer level) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuilder visit(final ASTSwitchClauseNode node, final Integer level) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuilder visit(final ASTDoWhileLoopNode node, final Integer level) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuilder visit(final ASTAssignmentExpressionNode node, final Integer level) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuilder visit(final ASTExceptionNode node, final Integer level) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuilder visit(final ASTThrowClauseNode node, final Integer level) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuilder visit(final ASTBreakClauseNode node, final Integer level) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuilder visit(final ASTContinueClauseNode node, final Integer level) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuilder visit(final ASTReturnClauseNode node, final Integer level) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuilder visit(final ASTLogNode node, final Integer level) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuilder visit(final ASTFunctionNode node, final Integer level) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuilder visit(final ASTUnaryExpressionNode node, final Integer level) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuilder visit(final ASTPropertyExpressionNode node, final Integer level) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuilder visit(final ASTIdentifierNameNode node, final Integer level) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuilder visit(final ASTWithClauseNode node, final Integer level) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuilder visit(final ASTFunctionClauseNode node, final Integer level) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuilder visit(final ASTEmptyNode node, final Integer data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuilder visit(final ASTLosNode node, final Integer data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuilder visit(final ASTTemplateNode node, final Integer data) throws EvaluationException {
		// TODO Auto-generated method stub
		return null;
	}

}
