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
import de.xima.fc.form.expression.util.CmnCnst;

public class ASTAssignmentExpressionNode extends ANode {
	private static final long serialVersionUID = 1L;
	public ASTAssignmentExpressionNode(@Nonnull final FormExpressionParser parser, final int id) {
		super(parser, id);
	}

	@Override
	public void init(final EMethod method) throws ParseException {
		assertChildrenAtLeast(2);
		assertChildrenAssignable(0, jjtGetNumChildren()-1, CmnCnst.Name.ASSIGNMENT);
		super.init(method);
	}

	@Override
	protected final Node replacementOnChildRemoval(final int i) throws ArrayIndexOutOfBoundsException {
		if (i != jjtGetNumChildren() - 1 || jjtGetNumChildren() < 3)
			throw new ArrayIndexOutOfBoundsException();
		return null;
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

	/**
	 * @return Number of assignable nodes. Eg. for <code> a = b = 2+3</code>,
	 *         this return <code>2</code>.
	 * @see #getAssignableNode(int)
	 * @see #getAssignMethod(int)
	 */
	public int getAssignableNodeCount() {
		return jjtGetNumChildren() - 1;
	}

	/**
	 * @param i
	 *            Number of the node. Valid values are
	 *            [0,{@link #getAssignableNodeCount()}).
	 * @return Node to which a value is to be assigned. For
	 *         <code> a = b = 2+3</code>, this can be called with indices
	 *         <code>0</code> and <code>1</code>, returning the nodes for
	 *         <code>a</code> and <code>b</code>, respectively.
	 */
	@Nonnull
	public Node getAssignableNode(final int i) {
		return jjtGetChild(i);
	}

	/**
	 * @param i Number of the node. Valid values are [0,{@link #getAssignableNodeCount()}).
	 * @return Assignment method for the given node, eg. <code>=</code>, <code>+=</code> etc.
	 */
	@Nonnull
	public EMethod getAssignMethod(final int i) {
		return children[i+1].getSiblingMethod();
	}

	/**
	 * @return The node with the value for the assignment. For example, consider
	 *         <code>a = b = 2+3</code>, this would be the
	 *         {@link ASTExpressionNode} for <code>2+3<code>.
	 */
	@Nonnull
	public Node getAssignValueNode() {
		return jjtGetChild(jjtGetNumChildren()-1);
	}
}
