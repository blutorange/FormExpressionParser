package de.xima.fc.form.expression.node;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionReturnDataVisitor;
import de.xima.fc.form.expression.visitor.IFormExpressionReturnVoidVisitor;
import de.xima.fc.form.expression.visitor.IFormExpressionVoidDataVisitor;
import de.xima.fc.form.expression.visitor.IFormExpressionVoidVoidVisitor;

public class ASTReturnClauseNode extends ANode {
	private static final long serialVersionUID = 1L;
	public ASTReturnClauseNode(@Nonnull final FormExpressionParser parser, final int nodeId) {
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
		assertChildrenBetween(0, 1);
		super.init(method);
	}
	
	@Override
	protected final Node replacementOnChildRemoval(final int i) throws ArrayIndexOutOfBoundsException {
		return null;
	}

	@Nonnull
	public Node getReturnNode() {
		return jjtGetChild(0);
	}

	public boolean hasReturn() {
		return jjtGetNumChildren() != 0;
	}
}