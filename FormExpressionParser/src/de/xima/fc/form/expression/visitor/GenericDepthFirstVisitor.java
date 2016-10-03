package de.xima.fc.form.expression.visitor;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.EvaluationException;
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

public abstract class GenericDepthFirstVisitor<R, T> implements IFormExpressionParserVisitor<R, T> {

	protected static class Node {
		private de.xima.fc.form.expression.grammar.Node node;
		public int getId() {
			return node.getId();
		}
		public int jjtGetNumChildren() {
			return node.jjtGetNumChildren();
		}
		public Class<?> getNodeClass() {
			return node.getClass();
		}
		public EMethod getSiblingMethod() {
			return node.getSiblingMethod();
		}
		/**
		 * @param clazz Class to which this node should be casted to.
		 * @return This node casted to the specified class, or <code>null</code> when it cannot be casted.
		 */
		public <T extends de.xima.fc.form.expression.grammar.Node> T cast(final Class<T> clazz) {
			if (!clazz.isAssignableFrom(node.getClass())) return null;
			return clazz.cast(node);
		}
		@Override
		public String toString() {
			return node.toString();
		}
		@Override
		public int hashCode() {
			return node.hashCode();
		}
		@Override
		public boolean equals(final Object obj) {
			return node.equals(obj);
		}
	}

	private final Node wrapper;
	public GenericDepthFirstVisitor() {
		wrapper = new Node();
	}

	/**
	 * Should process only the given node, but not any children. Iteration
	 * is take care of by this class.
	 * @param node
	 * @param data
	 * @return
	 */
	protected abstract R visitNode(Node node, T data);

	/**
	 * Reduces the return values from the parent node and its children.
	 * By default, this returns the value of the parent.
	 * @param reducedValue Current reduced value.
	 * @param newValue New value to be added.
	 * @return The reduced value.
	 */
	protected R reduce(final R reducedValue, final R newValue) {
		return reducedValue;
	}
	protected T map(final T value) {
		return value;
	}

	private R processNode(final de.xima.fc.form.expression.grammar.Node node, final T data) {
		wrapper.node = node;
		final T childData = map(data);
		R reduced = visitNode(wrapper, data);
		for (final de.xima.fc.form.expression.grammar.Node child : node.getChildArray()) {
			final R result = child.jjtAccept(this, childData);
			reduced = reduce(reduced, result);
		}
		return reduced;
	}

	@Override
	public final R visit(final ASTExpressionNode node, final T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(final ASTAssignmentExpressionNode node, final T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(final ASTNumberNode node, final T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(final ASTArrayNode node, final T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(final ASTHashNode node, final T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(final ASTNullNode node, final T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(final ASTBooleanNode node, final T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(final ASTVariableNode node, final T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(final ASTStringNode node, final T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(final ASTStatementListNode node, final T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(final ASTIfClauseNode node, final T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(final ASTForLoopNode node, final T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(final ASTWhileLoopNode node, final T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(final ASTTryClauseNode node, final T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(final ASTSwitchClauseNode node, final T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(final ASTDoWhileLoopNode node, final T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(final ASTExceptionNode node, final T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(final ASTThrowClauseNode node, final T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(final ASTBreakClauseNode node, final T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(final ASTContinueClauseNode node, final T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(final ASTReturnClauseNode node, final T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(final ASTLogNode node, final T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(final ASTFunctionNode node, final T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(final ASTUnaryExpressionNode node, final T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(final ASTPropertyExpressionNode node, final T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(final ASTIdentifierNameNode node, final T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(final ASTWithClauseNode node, final T data) throws EvaluationException {
		return processNode(node, data);
	}
	@Override
	public final R visit(final ASTFunctionClauseNode node, final T data) throws EvaluationException {
		return processNode(node, data);
	}
	@Override
	public final R visit(final ASTEmptyNode node, final T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public R visit(final ASTLosNode node, final T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public R visit(final ASTTemplateNode node, final T data) throws EvaluationException {
		return processNode(node, data);
	}
}
