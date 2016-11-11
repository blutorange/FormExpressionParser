package de.xima.fc.form.expression.node;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTSwitchClauseNode extends SimpleNode {
	private static final long serialVersionUID = 1L;
	@Nullable private String label;

	public ASTSwitchClauseNode(@Nonnull final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}

	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionParserVisitor<R, T, E> visitor, final T data) throws E {
		return visitor.visit(this, data);
	}

	@Override
	protected void additionalToStringFields(final StringBuilder sb) {
		sb.append(label).append(',');
	}

	public void init(@Nullable final EMethod method, @Nullable final String label) throws ParseException {
		assertChildrenAtLeast(1);
		super.init(method);
		this.label = label;
	}

	@Nullable
	public String getLabel() {
		return label;
	}
}
