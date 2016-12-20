package de.xima.fc.form.expression.node;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionReturnDataVisitor;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionReturnVoidVisitor;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionVoidDataVisitor;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionVoidVoidVisitor;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public class ASTPostUnaryExpressionNode extends ANode {
	private static final long serialVersionUID = 1L;
	private EMethod unaryMethod = EMethod.PLUS_UNARY;

	public ASTPostUnaryExpressionNode(final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}

	public final void init(final EMethod method, final EMethod unary) throws ParseException {
		assertChildrenExactly(1);
		NullUtil.checkNotNull(unary);
		if (unary.isAssigning())
			assertChildrenAssignable(0, 1, CmnCnst.Name.SUFFIX_OPERATION);
		super.init(method);
		unaryMethod = unary;
	}

	@Nullable
	@Override
	protected final Node replacementOnChildRemoval(final int i) throws ArrayIndexOutOfBoundsException {
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
}