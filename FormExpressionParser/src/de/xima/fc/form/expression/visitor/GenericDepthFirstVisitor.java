package de.xima.fc.form.expression.visitor;

import de.xima.fc.form.expression.enums.EMethod;
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
import de.xima.fc.form.expression.node.ASTIdentifierNameNode;
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
		public <T extends de.xima.fc.form.expression.grammar.Node> T cast(Class<T> clazz) {
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
		public boolean equals(Object obj) {
			return node.equals(obj);
		}
	}
	
	private Node wrapper;	
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
	protected R reduce(R reducedValue, R newValue) {
		return reducedValue;
	}
	protected T map(T value) {
		return value;
	}
	
	private R processNode(de.xima.fc.form.expression.grammar.Node node, T data) {
		wrapper.node = node;
		T childData = map(data);
		R reduced = visitNode(wrapper, data);
		for (de.xima.fc.form.expression.grammar.Node child : node.getChildArray()) {
			final R result = child.jjtAccept(this, childData);
			reduced = reduce(reduced, result);
		}
		return reduced;
	}
	
	@Override
	public final R visit(ASTExpressionNode node, T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(ASTAssignmentExpressionNode node, T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(ASTNumberNode node, T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(ASTArrayNode node, T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(ASTHashNode node, T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(ASTNullNode node, T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(ASTBooleanNode node, T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(ASTVariableNode node, T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(ASTStringNode node, T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(ASTStatementListNode node, T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(ASTIfClauseNode node, T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(ASTForLoopNode node, T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(ASTWhileLoopNode node, T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(ASTTryClauseNode node, T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(ASTSwitchClauseNode node, T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(ASTDoWhileLoopNode node, T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(ASTExceptionNode node, T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(ASTThrowClauseNode node, T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(ASTBreakClauseNode node, T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(ASTContinueClauseNode node, T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(ASTReturnClauseNode node, T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(ASTLogNode node, T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(ASTFunctionNode node, T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(ASTUnaryExpressionNode node, T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(ASTPropertyExpressionNode node, T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(ASTIdentifierNameNode node, T data) throws EvaluationException {
		return processNode(node, data);
	}

	@Override
	public final R visit(ASTWithClauseNode node, T data) throws EvaluationException {
		return processNode(node, data);
	}
}
