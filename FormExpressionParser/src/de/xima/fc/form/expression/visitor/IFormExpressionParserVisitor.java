package de.xima.fc.form.expression.visitor;

import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.node.ASTArrayNode;
import de.xima.fc.form.expression.node.ASTBooleanNode;
import de.xima.fc.form.expression.node.ASTDotExpressionNode;
import de.xima.fc.form.expression.node.ASTExpressionNode;
import de.xima.fc.form.expression.node.ASTHashNode;
import de.xima.fc.form.expression.node.ASTNullNode;
import de.xima.fc.form.expression.node.ASTNumberNode;
import de.xima.fc.form.expression.node.ASTParenthesesFunction;
import de.xima.fc.form.expression.node.ASTPlainFunction;
import de.xima.fc.form.expression.node.ASTStringNode;


@SuppressWarnings("all")
public interface IFormExpressionParserVisitor<R,T>
{
	public R visit(ASTExpressionNode node, T data) throws EvaluationException;
	public R visit(ASTDotExpressionNode node, T data) throws EvaluationException;
	public R visit(ASTParenthesesFunction node, T data) throws EvaluationException;
	public R visit(ASTNumberNode node, T data) throws EvaluationException;
	public R visit(ASTArrayNode node, T data) throws EvaluationException;
	public R visit(ASTHashNode node, T data) throws EvaluationException;
	public R visit(ASTNullNode node, T data) throws EvaluationException;
	public R visit(ASTBooleanNode node, T data) throws EvaluationException;
	public R visit(ASTPlainFunction node, T data) throws EvaluationException;
	public R visit(ASTStringNode node, T data) throws EvaluationException;
}
