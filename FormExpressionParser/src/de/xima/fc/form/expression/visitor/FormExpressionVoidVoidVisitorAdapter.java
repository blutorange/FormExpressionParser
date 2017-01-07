package de.xima.fc.form.expression.visitor;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionVoidVoidVisitor;
import de.xima.fc.form.expression.node.ASTArrayNode;
import de.xima.fc.form.expression.node.ASTAssignmentExpressionNode;
import de.xima.fc.form.expression.node.ASTBooleanNode;
import de.xima.fc.form.expression.node.ASTBreakClauseNode;
import de.xima.fc.form.expression.node.ASTComparisonExpressionNode;
import de.xima.fc.form.expression.node.ASTContinueClauseNode;
import de.xima.fc.form.expression.node.ASTDoWhileLoopNode;
import de.xima.fc.form.expression.node.ASTDotPropertyNode;
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

public abstract class FormExpressionVoidVoidVisitorAdapter<E extends Throwable>
implements IFormExpressionVoidVoidVisitor<E> {
	protected void visitChildren(@Nonnull final Node node) throws E {
		for (int i = 0; i < node.jjtGetNumChildren(); ++i) {
			node.jjtGetChild(i).jjtAccept(this);
		}
	}

	@Override
	public void visit(final ASTExpressionNode node) throws E {
		visitChildren(node);
	}

	@Override
	public void visit(final ASTAssignmentExpressionNode node) throws E {
		visitChildren(node);
	}

	@Override
	public void visit(final ASTNumberNode node) throws E {
		visitChildren(node);
	}

	@Override
	public void visit(final ASTArrayNode node) throws E {
		visitChildren(node);
	}

	@Override
	public void visit(final ASTHashNode node) throws E {
		visitChildren(node);

	}

	@Override
	public void visit(final ASTNullNode node) throws E {
		visitChildren(node);
	}

	@Override
	public void visit(final ASTBooleanNode node) throws E {
		visitChildren(node);
	}

	@Override
	public void visit(final ASTVariableNode node) throws E {
		visitChildren(node);
	}

	@Override
	public void visit(final ASTStringNode node) throws E {
		visitChildren(node);
	}

	@Override
	public void visit(final ASTStringCharactersNode node) throws E {
		visitChildren(node);
	}

	@Override
	public void visit(final ASTStatementListNode node) throws E {
		visitChildren(node);
	}

	@Override
	public void visit(final ASTIfClauseNode node) throws E {
		visitChildren(node);
	}

	@Override
	public void visit(final ASTForLoopNode node) throws E {
		visitChildren(node);
	}

	@Override
	public void visit(final ASTWhileLoopNode node) throws E {
		visitChildren(node);
	}

	@Override
	public void visit(final ASTTryClauseNode node) throws E {
		visitChildren(node);
	}

	@Override
	public void visit(final ASTSwitchClauseNode node) throws E {
		visitChildren(node);
	}

	@Override
	public void visit(final ASTDoWhileLoopNode node) throws E {
		visitChildren(node);
	}

	@Override
	public void visit(final ASTExceptionNode node) throws E {
		visitChildren(node);
	}

	@Override
	public void visit(final ASTThrowClauseNode node) throws E {
		visitChildren(node);
	}

	@Override
	public void visit(final ASTBreakClauseNode node) throws E {
		visitChildren(node);
	}

	@Override
	public void visit(final ASTContinueClauseNode node) throws E {
		visitChildren(node);

	}

	@Override
	public void visit(final ASTReturnClauseNode node) throws E {
		visitChildren(node);
	}

	@Override
	public void visit(final ASTLogNode node) throws E {
		visitChildren(node);
	}

	@Override
	public void visit(final ASTFunctionNode node) throws E {
		visitChildren(node);
	}

	@Override
	public void visit(final ASTUnaryExpressionNode node) throws E {
		visitChildren(node);
	}

	@Override
	public void visit(final ASTPropertyExpressionNode node) throws E {
		visitChildren(node);
	}

	@Override
	public void visit(final ASTIdentifierNameNode node) throws E {
		visitChildren(node);
	}

	@Override
	public void visit(final ASTWithClauseNode node) throws E {
		visitChildren(node);
	}

	@Override
	public void visit(final ASTFunctionClauseNode node) throws E {
		visitChildren(node);
	}

	@Override
	public void visit(final ASTEmptyNode node) throws E {
		visitChildren(node);
	}

	@Override
	public void visit(final ASTLosNode node) throws E {
		visitChildren(node);
	}

	@Override
	public void visit(final ASTRegexNode node) throws E {
		visitChildren(node);
	}

	@Override
	public void visit(final ASTTernaryExpressionNode node) throws E {
		visitChildren(node);
	}

	@Override
	public void visit(final ASTParenthesisExpressionNode node) throws E {
		visitChildren(node);
	}

	@Override
	public void visit(final ASTEqualExpressionNode node) throws E {
		visitChildren(node);
	}

	@Override
	public void visit(final ASTPostUnaryExpressionNode node) throws E {
		visitChildren(node);
	}

	@Override
	public void visit(final ASTComparisonExpressionNode node) throws E {
		visitChildren(node);
	}

	@Override
	public void visit(final ASTScopeExternalNode node) throws E {
		visitChildren(node);
	}

	@Override
	public void visit(final ASTVariableDeclarationClauseNode node) throws E {
		visitChildren(node);
	}

	@Override
	public void visit(final ASTScopeManualNode node) throws E {
		visitChildren(node);
	}

	@Override
	public void visit(final ASTScopeGlobalNode node) throws E {
		visitChildren(node);
	}

	@Override
	public void visit(final ASTVariableTypeNode node) throws E {
		visitChildren(node);
	}

	@Override
	public void visit(final ASTFunctionArgumentNode node) throws E {
		visitChildren(node);
	}

	@Override
	public void visit(final ASTDotPropertyNode node) throws E {
		visitChildren(node);
	}
}