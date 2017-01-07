package de.xima.fc.form.expression.node;

import javax.annotation.Nullable;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionReturnDataVisitor;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionReturnVoidVisitor;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionVoidDataVisitor;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionVoidVoidVisitor;

@NonNullByDefault
public class ASTPropertyExpressionNode extends ANode {
	private static final long serialVersionUID = 1L;

	public ASTPropertyExpressionNode(final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}

	@Override
	public void init(@Nullable final EMethod method) throws ParseException {
		assertChildrenAtLeast(2);
		super.init(method);
	}

	@Nullable
	@Override
	protected Node replacementOnChildRemoval(final int i) throws ArrayIndexOutOfBoundsException {
		if (jjtGetNumChildren() < 3)
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

	/** @return The node beginning the property chain. */
	public Node getStartNode() {
		return jjtGetChild(0);
	}

	/** @return The number of chained property nodes. */
	public int getPropertyNodeCount() {
		return jjtGetNumChildren() - 1;
	}

	/**
	 * @param i Index of the node to retrieve.
	 * @return The i-th property node.
	 * @throws ArrayIndexOutOfBoundsException
	 * @see {@link #getPropertyNodeCount()}
	 * @throws ArrayIndexOutOfBoundsException When the index is out of range.
	 */
	public Node getPropertyNode(final int i) throws ArrayIndexOutOfBoundsException {
		return jjtGetChild(i+1);
	}

	/**
	 *
	 * @param i Index of the node to access.
	 * @return The property type of the i-th node. One of {@link EMethod#DOT}, {@link EMethod#BRACKET}, or {@link EMethod#PARENTHESIS}.
	 * @see #getPropertyNodeCount()
	 * @throws ArrayIndexOutOfBoundsException When the index is out of range.
	 */
	public EMethod getPropertyType(final int i) {
		return jjtGetChild(i+1).getSiblingMethod();
	}

	/**
	 * For <code>array[0].size</code>, the property count is <code>2</code>,
	 * a valid index is <code>1</code> and this method returns <code>"size"</code>.
	 * @param i Index of the node to access. Valid indices are only those for which {@link #getPropertyType(int)} returns {@link EMethod#DOT}.
	 * @return Name of the dot property.
	 * @see #getPropertyNodeCount()
	 * @throws ArrayIndexOutOfBoundsException When the index is out of range.
	 * @throws ClassCastException When the node is not a dot property type node.
	 */
	public String getDotPropertyName(final int i) throws ClassCastException {
		return ((ASTDotPropertyNode)jjtGetChild(i+1)).getName();
	}

	public int getDotPropertyVariableTypeCount(final int i) throws ClassCastException {
		return ((ASTDotPropertyNode)jjtGetChild(i+1)).getVariableTypeCount();
	}

	public Node getDotPropertyVariableTypeNode(final int i, final int j) throws ClassCastException {
		return ((ASTDotPropertyNode)jjtGetChild(i+1)).getVariableTypeNode(j);
	}

	public int getParenthesisArgNodeCount(final int i) {
		return ((ASTArrayNode)jjtGetChild(i+1)).jjtGetNumChildren();
	}

	public Node getParenthesisArgNode(final int i, final int j) {
		return jjtGetChild(i+1).jjtGetChild(j);
	}
}