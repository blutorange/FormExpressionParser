package de.xima.fc.form.expression.node;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.visitor.IFormExpressionReturnDataVisitor;
import de.xima.fc.form.expression.visitor.IFormExpressionReturnVoidVisitor;
import de.xima.fc.form.expression.visitor.IFormExpressionVoidDataVisitor;
import de.xima.fc.form.expression.visitor.IFormExpressionVoidVoidVisitor;

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

	public int getAssignableNodeCount() {
		return jjtGetNumChildren() - 1;
	}
	
	@Nonnull
	public Node getAssignableNode(final int i) {
		return jjtGetChild(i);
	}
	
	@Nonnull
	public Node getAssignValueNode() {
		return jjtGetChild(jjtGetNumChildren()-1);
	}
}
