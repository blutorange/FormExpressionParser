package de.xima.fc.form.expression.node;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionReturnDataVisitor;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionReturnVoidVisitor;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionVoidDataVisitor;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionVoidVoidVisitor;

public class ASTIfClauseNode extends ANode {
	private static final long serialVersionUID = 1L;

	public ASTIfClauseNode(@Nonnull final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}

	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionReturnDataVisitor<R, T, E> visitor, final T data) throws E {
		return visitor.visit(this, data);
	}

	@Override
	public <R, E extends Throwable> R jjtAccept(final IFormExpressionReturnVoidVisitor<R, E> visitor) throws E {
		return visitor.visit(this);
	}

	@Override
	public <T, E extends Throwable> void jjtAccept(final IFormExpressionVoidDataVisitor<T, E> visitor, final T data) throws E {
		visitor.visit(this, data);
	}

	@Override
	public <E extends Throwable> void jjtAccept(final IFormExpressionVoidVoidVisitor<E> visitor) throws E {
		visitor.visit(this);
	}

	@Override
	public void init(final EMethod method) throws ParseException {
		assertChildrenBetween(2, 3);
		super.init(method);
	}

	@Override
	protected final Node replacementOnChildRemoval(final int i) throws ArrayIndexOutOfBoundsException {
		return i == 3 ? null : nullNode();
	}

	@Nonnull
	public Node getConditionNode() {
		return jjtGetChild(0);
	}

	@Nonnull
	public Node getIfNode() {
		return jjtGetChild(1);
	}

	/**
	 * @return The else node of this node.
	 * @throws ArrayIndexOutOfBoundsException When there is no else node. Use {@link #hasElseNode()} to check.
	 */
	@Nonnull
	public Node getElseNode() throws ArrayIndexOutOfBoundsException {
		return jjtGetChild(2);
	}

	public boolean hasElseNode() {
		return jjtGetNumChildren() == 3;
	}
}
