package de.xima.fc.form.expression.visitor;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.grammar.Node;
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
import de.xima.fc.form.expression.node.ASTWhileLoopNode;
import de.xima.fc.form.expression.node.ASTWithClauseNode;
import de.xima.fc.form.expression.util.Void;

public abstract class FormExpressionVoidDataVisitorAdapter<T, E extends Throwable>
implements IFormExpressionReturnDataVisitor<Void, T, E> {

	@Nonnull
	protected Void visitChildren(@Nonnull final Node node, @Nonnull final T data) throws E {
		for (int i = 0; i < node.jjtGetNumChildren(); ++i) {
			node.jjtGetChild(i).jjtAccept(this, data);
		}
		return Void.NULL;
	}

	@Override
	public Void visit(final ASTExpressionNode node, final T data) throws E {
		return visitChildren(node, data);
	}

	@Override
	public Void visit(final ASTAssignmentExpressionNode node, final T data) throws E {
		return visitChildren(node, data);
	}

	@Override
	public Void visit(final ASTNumberNode node, final T data) throws E {
		return visitChildren(node, data);
	}

	@Override
	public Void visit(final ASTArrayNode node, final T data) throws E {
		return visitChildren(node, data);

	}

	@Override
	public Void visit(final ASTHashNode node, final T data) throws E {
		return visitChildren(node, data);

	}

	@Override
	public Void visit(final ASTNullNode node, final T data) throws E {
		return visitChildren(node, data);

	}

	@Override
	public Void visit(final ASTBooleanNode node, final T data) throws E {
		return visitChildren(node, data);

	}

	@Override
	public Void visit(final ASTVariableNode node, final T data) throws E {
		return visitChildren(node, data);

	}

	@Override
	public Void visit(final ASTStringNode node, final T data) throws E {
		return visitChildren(node, data);

	}

	@Override
	public Void visit(final ASTStatementListNode node, final T data) throws E {
		return visitChildren(node, data);

	}

	@Override
	public Void visit(final ASTIfClauseNode node, final T data) throws E {
		return visitChildren(node, data);

	}

	@Override
	public Void visit(final ASTForLoopNode node, final T data) throws E {
		return visitChildren(node, data);

	}

	@Override
	public Void visit(final ASTWhileLoopNode node, final T data) throws E {
		return visitChildren(node, data);
	}

	@Override
	public Void visit(final ASTTryClauseNode node, final T data) throws E {
		return visitChildren(node, data);
	}

	@Override
	public Void visit(final ASTSwitchClauseNode node, final T data) throws E {
		return visitChildren(node, data);
	}

	@Override
	public Void visit(final ASTDoWhileLoopNode node, final T data) throws E {
		return visitChildren(node, data);
	}

	@Override
	public Void visit(final ASTExceptionNode node, final T data) throws E {
		return visitChildren(node, data);
	}

	@Override
	public Void visit(final ASTThrowClauseNode node, final T data) throws E {
		return visitChildren(node, data);
	}

	@Override
	public Void visit(final ASTBreakClauseNode node, final T data) throws E {
		return visitChildren(node, data);
	}

	@Override
	public Void visit(final ASTContinueClauseNode node, final T data) throws E {
		return visitChildren(node, data);

	}

	@Override
	public Void visit(final ASTReturnClauseNode node, final T data) throws E {
		return visitChildren(node, data);
	}

	@Override
	public Void visit(final ASTLogNode node, final T data) throws E {
		return visitChildren(node, data);
	}

	@Override
	public Void visit(final ASTFunctionNode node, final T data) throws E {
		return visitChildren(node, data);
	}

	@Override
	public Void visit(final ASTUnaryExpressionNode node, final T data) throws E {
		return visitChildren(node, data);
	}

	@Override
	public Void visit(final ASTPropertyExpressionNode node, final T data) throws E {
		return visitChildren(node, data);
	}

	@Override
	public Void visit(final ASTIdentifierNameNode node, final T data) throws E {
		return visitChildren(node, data);
	}

	@Override
	public Void visit(final ASTWithClauseNode node, final T data) throws E {
		return visitChildren(node, data);
	}

	@Override
	public Void visit(final ASTFunctionClauseNode node, final T data) throws E {
		return visitChildren(node, data);
	}

	@Override
	public Void visit(final ASTEmptyNode node, final T data) throws E {
		return visitChildren(node, data);

	}

	@Override
	public Void visit(final ASTLosNode node, final T data) throws E {
		return visitChildren(node, data);
	}

	@Override
	public Void visit(final ASTRegexNode node, final T data) throws E {
		return visitChildren(node, data);

	}

	@Override
	public Void visit(final ASTTernaryExpressionNode node, final T data) throws E {
		return visitChildren(node, data);
	}

	@Override
	public Void visit(final ASTParenthesisExpressionNode node, final T data) throws E {
		return visitChildren(node, data);
	}

	@Override
	public Void visit(final ASTEqualExpressionNode node, final T data) throws E {
		return visitChildren(node, data);
	}

	@Override
	public Void visit(final ASTPostUnaryExpressionNode node, final T data) throws E {
		return visitChildren(node, data);
	}

	@Override
	public Void visit(final ASTComparisonExpressionNode node, final T data) throws E {
		return visitChildren(node, data);
	}

	@Override
	public Void visit(final ASTScopeExternalNode node, final T data) throws E {
		return visitChildren(node, data);
	}

	@Override
	public Void visit(final ASTVariableDeclarationClauseNode node, final T data) throws E {
		return visitChildren(node, data);
	}

	@Override
	public Void visit(final ASTScopeManualNode node, final T data) throws E {
		return visitChildren(node, data);
	}

	@Override
	public Void visit(final ASTScopeGlobalNode node, final T data) throws E {
		return visitChildren(node, data);
	}
}
