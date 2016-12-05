package de.xima.fc.form.expression.node;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.enums.ELangObjectType;
import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionReturnDataVisitor;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionReturnVoidVisitor;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionVoidDataVisitor;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionVoidVoidVisitor;
import de.xima.fc.form.expression.iface.parse.ILabelled;
import de.xima.fc.form.expression.iface.parse.IVariableTyped;
import de.xima.fc.form.expression.util.CmnCnst;

public class ASTForLoopNode extends ASourceResolvableNode implements ILabelled, IVariableTyped {
	private static final long serialVersionUID = 1L;

	@Nullable
	private String label;

	private boolean isEnhancedLoop;

	private boolean hasType;

	public ASTForLoopNode(@Nonnull final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}

	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionReturnDataVisitor<R, T, E> visitor,
			final T data) throws E {
		return visitor.visit(this, data);
	}

	@Override
	public <R, E extends Throwable> R jjtAccept(final IFormExpressionReturnVoidVisitor<R, E> visitor) throws E {
		return visitor.visit(this);
	}

	@Override
	public <T, E extends Throwable> void jjtAccept(final IFormExpressionVoidDataVisitor<T, E> visitor, final T data)
			throws E {
		visitor.visit(this, data);
	}

	@Override
	public <E extends Throwable> void jjtAccept(final IFormExpressionVoidVoidVisitor<E> visitor) throws E {
		visitor.visit(this);
	}

	public void init(@Nullable final EMethod method, @Nullable final String iteratingLoopVariable,
			@Nullable final String label, final boolean hasType) throws ParseException {
		assertChildrenExactly(iteratingLoopVariable != null ? (hasType ? 3 : 2) : 4);
		super.init(method,
				iteratingLoopVariable != null ? iteratingLoopVariable : CmnCnst.NonnullConstant.STRING_EMPTY);
		this.isEnhancedLoop = iteratingLoopVariable != null;
		this.label = label;
		this.hasType = hasType;
	}

	@Override
	protected final Node replacementOnChildRemoval(final int i) throws ArrayIndexOutOfBoundsException {
		if (isEnhancedLoop) {
			if (i == (hasType ? 1 : 0))
				throw new ArrayIndexOutOfBoundsException();
			if (hasType && i == 0)
				return new ASTVariableTypeNode(jjtGetChild(0), ELangObjectType.NULL);
		}
		return nullNode();
	}

	/**
	 * @return Whether this is an enhanced or plain loop.
	 * @see #isPlainLoop()
	 */
	public boolean isEnhancedLoop() {
		return isEnhancedLoop;
	}

	/**
	 * @return Whether this is a plain or enhanced loop.
	 * @see #isEnhancedLoop()
	 */
	public boolean isPlainLoop() {
		return !isEnhancedLoop;
	}

	@Override
	public boolean hasType() {
		return hasType;
	}

	@Override
	public Node getTypeNode() {
		return jjtGetChild(0);
	}

	@Nonnull
	public Node getBodyNode() {
		return isEnhancedLoop ? jjtGetChild(hasType ? 2 : 1) : jjtGetChild(3);
	}

	@Nonnull
	public Node getEnhancedIteratorNode() {
		return jjtGetChild(hasType ? 1 : 0);
	}

	@Nonnull
	public Node getPlainInitializerNode() {
		return jjtGetChild(0);
	}

	@Nonnull
	public Node getPlainConditionNode() {
		return jjtGetChild(1);
	}

	@Nonnull
	public Node getPlainIncrementNode() throws ArrayIndexOutOfBoundsException {
		return jjtGetChild(2);
	}

	@Override
	protected void additionalToStringFields(final StringBuilder sb) {
		super.additionalToStringFields(sb);
		sb.append(getVariableName()).append(',');
		sb.append(label).append(',');
	}

	@Override
	public String getNodeName() {
		return isEnhancedLoop ? CmnCnst.Name.FOR_ITERATING_NODE : CmnCnst.Name.FOR_PLAIN_NODE;
	}

	@Override
	@Nullable
	public String getLabel() {
		return label;
	}
}