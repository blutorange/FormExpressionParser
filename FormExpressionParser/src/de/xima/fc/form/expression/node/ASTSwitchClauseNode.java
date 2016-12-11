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
import de.xima.fc.form.expression.iface.parse.ILabelled;

@ParametersAreNonnullByDefault
public class ASTSwitchClauseNode extends ANode  implements ILabelled {
	private static final long serialVersionUID = 1L;
	@Nullable private String label;

	public ASTSwitchClauseNode(final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}

	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionReturnDataVisitor<R, T, E> visitor, final T data) throws E {
		return visitor.visit(this, data);
	}

	@Override
	protected void additionalToStringFields(final StringBuilder sb) {
		sb.append(label).append(',');
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

	public void init(@Nullable final EMethod method, @Nullable final String label) throws ParseException {
		assertChildrenAtLeast(1);
		super.init(method);
		this.label = label;
	}

	@Nullable
	@Override
	protected Node replacementOnChildRemoval(final int i) throws ArrayIndexOutOfBoundsException {
		if (i == 0) return nullNode();
		if (i == jjtGetNumChildren() - 1 && jjtGetNumChildren() % 2 != 0) return null;
		removeChildUnconditionally(i % 2 == 0 ? i - 1 : i + 1);
		return null;
	}

	@Override
	@Nullable
	public String getLabel() {
		return label;
	}

	public Node getSwitchValueNode() {
		return jjtGetChild(0);
	}

	public int getCaseCount() {
		return jjtGetNumChildren() - 1;
	}
	
	public EMethod getCaseType(final int i) {
		return jjtGetChild(i+1).getSiblingMethod();
	}

	public Node getCaseNode(final int i) {
		return jjtGetChild(i+1);
	}

	public boolean hasDefaultCase() {
		return jjtGetChild(jjtGetNumChildren()).getSiblingMethod() == EMethod.SWITCHDEFAULT;
	}
}