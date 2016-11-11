package de.xima.fc.form.expression.node;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTVariableNode extends SimpleNode {
	private static final long serialVersionUID = 1L;

	public ASTVariableNode(@Nonnull final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}

	@Nonnull
	private String name = CmnCnst.EMPTY_STRING;
	@Nullable
	private String scope;

	@Nonnull
	public String getName() {
		return name;
	}

	/**
	 * @return The scope, or <code>null</code> when it is a local variable.
	 */
	@Nullable
	public String getScope() {
		return scope;
	}

	public boolean hasScope() {
		return scope != null;
	}

	public void init(@Nullable final EMethod method, @Nullable final String scope, @Nonnull final String name) throws ParseException {
		assertChildrenAtMost(1);
		assertNonNull(name, CmnCnst.Error.VARIABLE_NODE_NULL_NAME);
		super.init(method);
		this.name =  name;
		this.scope = scope;
	}

	@Override
	protected void additionalToStringFields(final StringBuilder sb) {
		sb.append(scope).append(',').append(name).append(',');
	}

	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionParserVisitor<R, T, E> visitor, final T data) throws E {
		return visitor.visit(this, data);
	}
}