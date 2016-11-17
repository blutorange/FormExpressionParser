package de.xima.fc.form.expression.node;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.visitor.IFormExpressionReturnVoidVisitor;
import de.xima.fc.form.expression.visitor.IFormExpressionReturnDataVisitor;
import de.xima.fc.form.expression.visitor.IFormExpressionVoidDataVisitor;
import de.xima.fc.form.expression.visitor.IFormExpressionVoidVoidVisitor;

public class ASTForLoopNode extends ANode {

	private static final long serialVersionUID = 1L;
	private String iteratingLoopVariable;

	@Nullable private String label;

	public ASTForLoopNode(@Nonnull final FormExpressionParser parser, final int nodeId) {
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

	public void init(@Nullable final EMethod method, @Nullable final String iteratingLoopVariable, @Nullable final String label) throws ParseException {
		assertChildrenExactly(iteratingLoopVariable != null ? 2 : 4);
		super.init(method);
		this.iteratingLoopVariable = iteratingLoopVariable;
		this.label = label;
	}

	@Nullable
	public String getIteratingLoopVariable() {
		return iteratingLoopVariable;
	}

	@Override
	protected void additionalToStringFields(final StringBuilder sb) {
		sb.append(iteratingLoopVariable).append(',');
		sb.append(label).append(',');
	}

	@Override
	protected String nodeName() {
		return iteratingLoopVariable != null ? CmnCnst.Name.FOR_ITERATING_NODE : CmnCnst.Name.FOR_PLAIN_NODE;
	}

	@Nullable
	public String getLabel() {
		return label;
	}

}
