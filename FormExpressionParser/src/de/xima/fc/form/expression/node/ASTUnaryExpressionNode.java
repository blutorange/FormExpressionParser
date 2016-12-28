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
import de.xima.fc.form.expression.util.CmnCnst;

@NonNullByDefault
public class ASTUnaryExpressionNode extends ANode {
	private static final long serialVersionUID = 1L;

	public ASTUnaryExpressionNode(final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}

	private EMethod unaryMethod = EMethod.PLUS_UNARY;

	public final void init(@Nullable final EMethod method, final EMethod unary) throws ParseException {
		assertChildrenExactly(1);
		assertNonNull(unary, CmnCnst.Error.NULL_METHOD);
		if (unary.isAssigning())
			assertChildrenAssignable(0, 1, CmnCnst.Name.PREFIX_OPERATION);
		super.init(method);
		unaryMethod = unary;
	}

	@Nullable
	@Override
	protected Node replacementOnChildRemoval(final int i) throws ArrayIndexOutOfBoundsException {
		throw new ArrayIndexOutOfBoundsException();
	}

	@Override
	protected void additionalToStringFields(final StringBuilder sb) {
		sb.append(unaryMethod).append(',');
	}

	public EMethod getUnaryMethod() {
		return unaryMethod;
	}

	public Node getUnaryNode() {
		return jjtGetChild(0);
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
}